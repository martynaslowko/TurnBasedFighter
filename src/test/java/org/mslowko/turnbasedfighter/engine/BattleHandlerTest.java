package org.mslowko.turnbasedfighter.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mslowko.turnbasedfighter.TestData;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.model.repository.MobRepository;
import org.mslowko.turnbasedfighter.service.MobService;
import org.mslowko.turnbasedfighter.util.MobBuilderClient;
import org.mslowko.turnbasedfighter.util.ResponseHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mslowko.turnbasedfighter.Constants.neo4jImage;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BattleHandlerTest {
    @Container
    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(neo4jImage).withStartupTimeout(Duration.ofMinutes(5));

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @SpyBean
    private BattleHandler battleHandler;

    @SpyBean
    private MobService mobService;

    @MockBean
    private MobBuilderClient mobBuilderClient;

    @SpyBean
    private DungeonRepository dungeonRepository;

    @SpyBean
    private MobRepository mobRepository;

    @SpyBean
    private CharacterRepository characterRepository;

    @SpyBean
    private ResponseHelper responseHelper;

    @SpyBean
    private QueueCache queueCache;

    @BeforeEach
    void cleanUp() {
        dungeonRepository.deleteAll();
        characterRepository.deleteAll();
        mobRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({"1,1", "2,4", "10,8"})
    void victoriousFlowTest(int waves, int slots) {
        Dungeon dungeon = TestData.prepareDungeon(waves, slots);
        dungeon.getLobby().forEach(c -> c.setHp(1000));
        when(mobBuilderClient.fetchMob(anyInt(), anyBoolean())).thenReturn(TestData.prepareImp());

        battleHandler.handleCurrentCharacter(dungeon);
        battleHandler.nextWave(dungeon);
        do {
            battleHandler.nextTurn(dungeon, dungeon.getCurrentCharacter(), Action.ATTACK);
        } while (dungeonRepository.findById(dungeon.getId()).isPresent());

        verify(battleHandler, times(waves)).nextWave(dungeon);
        assertThat(dungeon.getCurrentWave()).isEqualTo(waves);
        assertThat(dungeon.getLobby()).allMatch(c -> (c.getExp() > 0 || c.getLevel() > 0));
    }

    @ParameterizedTest
    @CsvSource({"1,1", "2,4", "10,8"})
    void defeatedFlowTest(int waves, int slots) {
        Dungeon dungeon = TestData.prepareDungeon(waves, slots);
        dungeon.getLobby().forEach(c -> {
            c.setHp(1);
            c.setDamage(1);
        });
        when(mobBuilderClient.fetchMob(anyInt(), anyBoolean())).thenReturn(TestData.prepareImp());

        battleHandler.handleCurrentCharacter(dungeon);
        battleHandler.nextWave(dungeon);
        do {
            battleHandler.nextTurn(dungeon, dungeon.getCurrentCharacter(), Action.ATTACK);
        } while (dungeonRepository.findById(dungeon.getId()).isPresent());

        assertThat(dungeon.getLobby()).allMatch(c -> !characterRepository.existsById(c.getName()));
    }
}
