package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Mob;
import org.mslowko.turnbasedfighter.util.MobBuilderClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MobService {
    private final MobBuilderClient mobBuilderClient;

    public void assignMobToDungeon(Dungeon dungeon) {
        Mob mob = mobBuilderClient.fetchMob(calculateLobbyLevel(dungeon.getLobby()), calculateBossTurn(dungeon));
        dungeon.setCurrentOpponent(mob);
    }

    private int calculateLobbyLevel(List<Character> lobby) {
        return lobby.stream().mapToInt(Character::getLevel).sum();
    }

    private boolean calculateBossTurn(Dungeon dungeon) {
        int current = dungeon.getCurrentWave();
        int last = dungeon.getWaves();
        if (current == last)
            return true;
        else return current % 3 == 0;
    }

}
