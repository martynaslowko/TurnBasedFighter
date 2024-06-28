package org.mslowko.turnbasedfighter.service;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Mob;
import org.mslowko.turnbasedfighter.util.MobBuilderClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mslowko.turnbasedfighter.TestData.prepareCharacterList;

@SpringBootTest(classes = {MobService.class})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MobServiceTest {
    @SpyBean
    private MobService mobService;

    @MockBean
    private MobBuilderClient mobBuilderClient;

    @ParameterizedTest
    @CsvSource({"3,3,3", "2,5,6", "1,1,1"})
    void assignMobToDungeonTest(int lobbySize, int currentWave, int maxWaves){
        Dungeon dungeon = new Dungeon(lobbySize, maxWaves);
        List<Character> lobby = prepareCharacterList(lobbySize);
        dungeon.setLobby(lobby);
        dungeon.setCurrentWave(currentWave);

        ArgumentCaptor<Integer> lobbyLevelCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> bossTurnCaptor = ArgumentCaptor.forClass(Boolean.class);

        Mob mob = Mockito.mock(Mob.class);
        when(mobBuilderClient.fetchMob(anyInt(), anyBoolean())).thenReturn(mob);

        mobService.assignMobToDungeon(dungeon);

        verify(mobBuilderClient).fetchMob(lobbyLevelCaptor.capture(), bossTurnCaptor.capture());
        assertThat(dungeon.getCurrentOpponent()).isEqualTo(mob);
        assertThat(lobbyLevelCaptor.getValue()).isEqualTo(lobbySize);
        if (currentWave == maxWaves)
            assertThat(bossTurnCaptor.getValue()).isTrue();
        else
            assertThat(bossTurnCaptor.getValue()).isEqualTo(currentWave % 3 == 0);
    }
}
