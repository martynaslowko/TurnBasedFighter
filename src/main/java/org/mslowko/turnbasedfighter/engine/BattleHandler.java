package org.mslowko.turnbasedfighter.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.mslowko.turnbasedfighter.engine.units.CharacterEngine;
import org.mslowko.turnbasedfighter.engine.units.MobEngine;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Mob;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.model.repository.MobRepository;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterTurnDeniedException;
import org.mslowko.turnbasedfighter.service.MobService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleHandler {
    private final MobService mobService;
    private final DungeonRepository dungeonRepository;
    private final MobRepository mobRepository;
    private final CharacterRepository characterRepository;

    public void nextTurn(Dungeon dungeon, Character caller, Action action) {
        CharacterEngine character = new CharacterEngine(caller);
        MobEngine mob = new MobEngine(dungeon.getCurrentOpponent());

        if (!caller.equals(dungeon.getCurrentCharacter())) {
            if (!character.isAlive()) {
                handleCurrentCharacter(dungeon);
            } else throw new CharacterTurnDeniedException(caller.getName(), dungeon);
        }

        handleAction(action, character, mob);
        if (mob.isAlive()) {
            mob.attack(character);
        }

        handleEndOfTurn(dungeon, character, mob);
    }

    private void updateDungeonTree(Dungeon dungeon, CharacterEngine characterEngine, MobEngine mobEngine) {
        Character character = characterEngine.portToParent();
        Mob mob = mobEngine.portToParent();
        int charIdx = dungeon.getLobby().stream().map(Character::getName)
                .toList().indexOf(character.getName());
        dungeon.getLobby().set(charIdx, character);
        dungeon.setCurrentOpponent(mob);
    }

    private void handleEndOfTurn(Dungeon dungeon, CharacterEngine character, MobEngine mob) {
        if (dungeon.getLobby().stream().allMatch(c -> c.getHp() <= 0)) {
            mobRepository.delete(dungeon.getCurrentOpponent());
            dungeonRepository.delete(dungeon);
            characterRepository.deleteAll(dungeon.getLobby());
            return;
        }
        if (!mob.isAlive()) {
            mobRepository.deleteById(mob.getId());
            nextWave(dungeon);
            mob = new MobEngine(dungeon.getCurrentOpponent());
        }
        updateDungeonTree(dungeon, character, mob);
        handleCurrentCharacter(dungeon);
        dungeonRepository.save(dungeon);
    }

    public void nextWave(Dungeon dungeon) {
        int prevWave = dungeon.getCurrentWave();
        dungeon.setCurrentWave(prevWave+1);
        mobService.assignMobToDungeon(dungeon);
        handleCurrentCharacter(dungeon);
        log.debug("Starting next wave in {}: [currWave={}, charTurn={}]",
                dungeon.getId(), dungeon.getCurrentWave(), dungeon.getCurrentCharacter());
    }

    private void handleAction(Action action, CharacterEngine character, MobEngine mob) {
        switch(action) {
            case ATTACK -> character.attack(mob);
            case HEAL -> character.heal();
            case FLEE -> throw new NotImplementedException();
        }
    }

    private void handleCurrentCharacter(Dungeon dungeon) {
        Character currCharacter;
        List<Character> lobby = dungeon.getLobby();
        try {
            Character character = dungeon.getCurrentCharacter();
            int prevIndex = lobby.indexOf(character);
            currCharacter = lobby.get(prevIndex+1);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            currCharacter = lobby.get(0);
        }
        dungeon.setCurrentCharacter(currCharacter);
    }
}
