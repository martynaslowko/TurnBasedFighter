package org.mslowko.turnbasedfighter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.engine.Action;
import org.mslowko.turnbasedfighter.engine.BattleHandler;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;
import org.mslowko.turnbasedfighter.pojo.exceptions.*;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterActionRequest;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mslowko.turnbasedfighter.Constants.neo4jImage;
import static org.mslowko.turnbasedfighter.TestData.*;
import static org.mslowko.turnbasedfighter.engine.Action.ATTACK;
import static org.mslowko.turnbasedfighter.engine.Action.HEAL;

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

    @SpyBean
    private CharacterRepository characterRepository;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private BattleHandler battleHandler;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void cleanUp() {
        dungeonRepository.deleteAll();
        characterRepository.deleteAll();
    }

    @Test
    void createDungeonTest() {
        ArgumentCaptor<Dungeon> dungeonArgumentCaptor = ArgumentCaptor.forClass(Dungeon.class);
        DungeonCreateResponse response = dungeonService.createDungeon(3, 3);

        verify(dungeonRepository).save(dungeonArgumentCaptor.capture());

        Dungeon capturedDungeon = dungeonArgumentCaptor.getValue();
        DungeonDto dungeonDto = modelMapper.map(capturedDungeon, DungeonDto.class);

        assertThat(dungeonDto).isEqualTo(response.getDungeon());
        assertThat(capturedDungeon.getJoinKey()).isEqualTo(response.getJoinKey());
    }

    @Test
    void joinDungeonTest_General() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        String playerId = "p3";

        Character character = prepareExistingPlayerCharacter(playerId, "c3").getCharacters().get(0);
        CharacterJoinRequest request = assembleRequest(dungeon.getJoinKey(), character);

        when(characterService.fetchCharacter(any())).thenReturn(character);

        dungeonService.joinDungeon(dungeonId, playerId, request);

        dungeon = dungeonRepository.findById(dungeonId).get();

        character.setVersion(0L);
        assertThat(dungeon.getLobby()).contains(character);
    }

    @Test
    void joinDungeonTest_CharacterAlreadyDeployedException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        Character character = dungeon.getLobby().get(0);
        CharacterJoinRequest request = assembleRequest(dungeon.getJoinKey(), character);

        when(characterService.fetchCharacter(any())).thenReturn(character);
        when(dungeonRepository.fetchDungeonByCharacterName(any())).thenReturn(Optional.of(dungeon));

        assertThrows(CharacterAlreadyDeployedException.class,
                () -> dungeonService.joinDungeon(dungeonId, "p1", request));
    }

    @Test
    void joinDungeonTest_InvalidJoinKeyException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        String playerId = "p3";

        Character character = prepareExistingPlayerCharacter(playerId, "c3").getCharacters().get(0);
        CharacterJoinRequest request = assembleRequest("XXXXX", character);

        when(characterService.fetchCharacter(any())).thenReturn(character);

        assertThrows(InvalidJoinKeyException.class,
                () -> dungeonService.joinDungeon(dungeonId, playerId, request));
    }

    @Test
    void joinDungeonTest_DungeonAlreadyStartedException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeon.setStarted(true);
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        String playerId = "p3";

        Character character = prepareExistingPlayerCharacter(playerId, "c3").getCharacters().get(0);
        CharacterJoinRequest request = assembleRequest(dungeon.getJoinKey(), character);

        when(characterService.fetchCharacter(any())).thenReturn(character);

        assertThrows(DungeonAlreadyStartedException.class,
                () -> dungeonService.joinDungeon(dungeonId, playerId, request));
    }

    @Test
    void joinDungeonTest_DungeonAlreadyFullException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        String playerId = "p3";
        Character character = prepareExistingPlayerCharacter(playerId, "c3").getCharacters().get(0);
        CharacterJoinRequest request = assembleRequest(dungeon.getJoinKey(), character);

        when(characterService.fetchCharacter(any())).thenReturn(character);

        dungeonService.joinDungeon(dungeonId, playerId, request);

        assertThrows(DungeonAlreadyFullException.class,
                () -> dungeonService.joinDungeon(dungeonId, playerId, request));
    }

    @Test
    void startDungeonTest() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();

        dungeonService.startDungeon(dungeonId);

        Dungeon updatedDungeon = dungeonRepository.findById(dungeonId).get();
        assertThat(updatedDungeon.isStarted()).isTrue();

        assertThrows(DungeonAlreadyStartedException.class,
                () -> dungeonService.startDungeon(dungeonId));
    }

    @ParameterizedTest
    @MethodSource("actionStream")
    void handleActionTest(Action action) {
        Dungeon dungeon = prepareSingleDungeon();
        dungeon.setStarted(true);
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        Character character = dungeon.getLobby().get(0);

        CharacterActionRequest characterActionRequest = new CharacterActionRequest();
        characterActionRequest.setAction(action);
        characterActionRequest.setCharacterId(character.getName());

        when(characterService.fetchCharacter(any())).thenReturn(character);

        dungeonService.handleAction(dungeonId, "p1", characterActionRequest);

        verify(battleHandler).nextTurn(dungeon, character, action);
    }

    @Test
    void leaveDungeonTest_CharacterNotFoundException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();

        assertThrows(CharacterNotFoundException.class,
                () -> dungeonService.leaveDungeon(dungeonId, "p3","c3"));
    }

    @Test
    void leaveDungeonTest_DungeonAlreadyStartedException() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeon.setStarted(true);
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();

        assertThrows(DungeonAlreadyStartedException.class,
                () -> dungeonService.leaveDungeon(dungeonId, "p1", "c1"));
    }

    @Test
    void leaveDungeonTest() {
        Dungeon dungeon = prepareJoinableDungeon();
        dungeonRepository.save(dungeon);

        String dungeonId = dungeon.getId();
        String removedCharacterId = "c1";

        dungeonService.leaveDungeon(dungeonId, "p1", removedCharacterId);

        Dungeon updatedDungeon = dungeonRepository.findById(dungeonId).get();

        assertThat(updatedDungeon.getLobby()).hasSize(1);
        assertThat(updatedDungeon.getLobby().get(0).getName()).isNotEqualTo(removedCharacterId);
    }

    static Stream<Action> actionStream() {
        return Stream.of(ATTACK, HEAL);
    }

    private CharacterJoinRequest assembleRequest(String dungeonKey, Character character) {
        CharacterJoinRequest request = new CharacterJoinRequest();
        request.setKey(dungeonKey);
        request.setCharacterId(character.getName());
        return request;
    }
}
