package org.mslowko.turnbasedfighter.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mslowko.turnbasedfighter.config.Constants;
import org.mslowko.turnbasedfighter.engine.units.CharacterEngine;
import org.mslowko.turnbasedfighter.engine.units.MobEngine;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Mob;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.model.repository.MobRepository;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterTurnDeniedException;
import org.mslowko.turnbasedfighter.pojo.responses.BattleResponse;
import org.mslowko.turnbasedfighter.service.MobService;
import org.mslowko.turnbasedfighter.util.ResponseHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.mslowko.turnbasedfighter.util.ResponseHelper.attackMessage;
import static org.mslowko.turnbasedfighter.util.ResponseHelper.healMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleHandler {
    private final MobService mobService;
    private final DungeonRepository dungeonRepository;
    private final MobRepository mobRepository;
    private final CharacterRepository characterRepository;
    private final ResponseHelper responseHelper;
    private final QueueCache queueCache;

    public BattleResponse nextTurn(Dungeon dungeon, Character caller, Action action) {
        CharacterEngine character = new CharacterEngine(caller);
        MobEngine mob = new MobEngine(dungeon.getCurrentOpponent());

        if (!caller.equals(dungeon.getCurrentCharacter()))
            throw new CharacterTurnDeniedException(caller.getName(), dungeon);

        Integer characterValue = handleAction(action, character, mob);
        Integer mobValue = null;
        if (mob.isAlive()) {
            mobValue = mob.attack(character);
        }

        return handleEndOfTurn(dungeon, character, mob, action, characterValue, mobValue);
    }

    private BattleResponse handleEndOfTurn(Dungeon dungeon, CharacterEngine character, MobEngine mob,
                                           Action action, Integer characterValue, Integer mobValue) {
        String characterAction = switch (action) {
            case HEAL -> healMessage(character.getName(), characterValue);
            case ATTACK -> attackMessage(character.getName(), mob.getName(), characterValue);
        };
        String mobAction = null;
        if (mobValue != null) {
            mobAction = attackMessage(mob.getName(), character.getName(), mobValue);
        }

        updateDungeonTree(dungeon, character, mob);

        BattleResponse battleResponse = responseHelper.battleResponse(Constants.BattleCode.TURN, dungeon, characterAction, mobAction);
        if (dungeon.getLobby().stream().allMatch(c -> c.getHp() <= 0)) {
            return handleDefeat(dungeon, characterAction, mobAction);
        }
        if (!mob.isAlive()) {
            mobRepository.deleteById(mob.getId());
            if (dungeon.getCurrentWave() == dungeon.getWaves()) {
                return handleVictory(dungeon, characterAction, mobAction);
            }
            String prevOpponent = dungeon.getCurrentOpponent().getName();
            handleNextWave(dungeon, character);
            battleResponse = responseHelper.waveResponse(dungeon, prevOpponent, characterAction);
        }
        if (!character.isAlive()) {
            battleResponse = responseHelper.battleResponse(Constants.BattleCode.DOWN, dungeon, characterAction, mobAction);
        }

        handleCurrentCharacter(dungeon);
        dungeonRepository.save(dungeon);
        return battleResponse;
    }

    private void handleNextWave(Dungeon dungeon, CharacterEngine character) {
        character.gainExp(dungeon.getCurrentOpponent().getExp());
        character.levelUp();
        distributeExp(dungeon);
        nextWave(dungeon);
        MobEngine mob = new MobEngine(dungeon.getCurrentOpponent());
        updateDungeonTree(dungeon, character, mob);
    }


    private BattleResponse handleVictory(Dungeon dungeon, String characterAction, String mobAction) {
        distributeExp(dungeon);
        reviveAllCharacters(dungeon);
        characterRepository.saveAll(dungeon.getLobby());
        dungeonRepository.delete(dungeon);
        return responseHelper.battleResponse(Constants.BattleCode.VICTORY, dungeon, characterAction, mobAction);
    }

    private BattleResponse handleDefeat(Dungeon dungeon, String characterAction, String mobAction) {
        queueCache.cleanUpQueue(dungeon);
        mobRepository.delete(dungeon.getCurrentOpponent());
        dungeonRepository.delete(dungeon);
        characterRepository.deleteAll(dungeon.getLobby());
        return responseHelper.battleResponse(Constants.BattleCode.DEFEAT, dungeon, characterAction, mobAction);
    }

    public void nextWave(Dungeon dungeon) {
        int prevWave = dungeon.getCurrentWave();
        dungeon.setCurrentWave(prevWave+1);
        mobService.assignMobToDungeon(dungeon);
        log.debug("Starting next wave in {}: [currWave={}, charTurn={}]",
                dungeon.getId(), dungeon.getCurrentWave(), dungeon.getCurrentCharacter());
    }

    private int handleAction(Action action, CharacterEngine character, MobEngine mob) {
        return switch (action) {
            case HEAL -> character.heal();
            case ATTACK -> character.attack(mob);
        };
    }

    private void distributeExp(Dungeon dungeon) {
        int exp = dungeon.getCurrentOpponent().getExp();
        List<CharacterEngine> characterEngines = dungeon.getLobby().stream().map(CharacterEngine::new).toList();
        characterEngines.forEach(c -> {
            c.gainExp(exp);
            c.levelUp();
        });
        List<Character> lobby = characterEngines.stream().map(CharacterEngine::portToParent).toList();
        dungeon.setLobby(new ArrayList<>(lobby));
    }

    private void updateDungeonTree(Dungeon dungeon, CharacterEngine characterEngine, MobEngine mobEngine) {
        Character character = characterEngine.portToParent();
        Mob mob = mobEngine.portToParent();
        int charIdx = dungeon.getLobby().stream().map(Character::getName)
                .toList().indexOf(character.getName());
        dungeon.getLobby().set(charIdx, character);
        dungeon.setCurrentOpponent(mob);
    }

    private void reviveAllCharacters(Dungeon dungeon) {
        List<Character> lobby = dungeon.getLobby().stream().toList();
        lobby.forEach(c -> c.setHp(c.getMaxHP()));
    }

    public void handleCurrentCharacter(Dungeon dungeon) {
        while (true) {
            Character character = queueCache.getNextCharacter(dungeon);
            if (character.getHp() > 0) {
                dungeon.setCurrentCharacter(character);
                break;
            }
        }
    }
}
