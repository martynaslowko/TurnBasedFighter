package org.mslowko.turnbasedfighter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.engine.BattleHandler;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterAlreadyDeployedException;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterJoinRequest;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mslowko.turnbasedfighter.Constants.neo4jImage;
import static org.mslowko.turnbasedfighter.TestData.prepareJoinableDungeon;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DungeonServiceTest {

    @Container
    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(neo4jImage).withStartupTimeout(Duration.ofMinutes(5));

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @SpyBean
    private DungeonService dungeonService;

    @SpyBean
    private DungeonRepository dungeonRepository;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private BattleHandler battleHandler;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void cleanUp() {
        dungeonRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({"3,3","1,2"})
    void createDungeonTest(int waves, int slots) {
        ArgumentCaptor<Dungeon> dungeonArgumentCaptor = ArgumentCaptor.forClass(Dungeon.class);
        DungeonCreateResponse response = dungeonService.createDungeon(waves, slots);

        verify(dungeonRepository).save(dungeonArgumentCaptor.capture());

        Dungeon capturedDungeon = dungeonArgumentCaptor.getValue();
        DungeonDto dungeonDto = modelMapper.map(capturedDungeon, DungeonDto.class);

        assertThat(dungeonDto).isEqualTo(response.getDungeon());
        assertThat(capturedDungeon.getJoinKey()).isEqualTo(response.getJoinKey());
    }

    @Test
    void joinDungeonTest_CharacterAlreadyDeployedException(){
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();

        Character busyCharacter = dungeon.getLobby().get(0);

        CharacterJoinRequest request = new CharacterJoinRequest();
        request.setKey(dungeon.getJoinKey());
        request.setCharacterId(busyCharacter.getName());

        when(characterService.fetchCharacter(any())).thenReturn(busyCharacter);
        when(dungeonRepository.fetchDungeonByCharacterName(any())).thenReturn(Optional.of(dungeon));

        assertThrows(CharacterAlreadyDeployedException.class,
                () -> dungeonService.joinDungeon(dungeonId, request));
    }


}
