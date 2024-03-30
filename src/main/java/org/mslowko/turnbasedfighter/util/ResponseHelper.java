package org.mslowko.turnbasedfighter.util;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.config.Constants;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.pojo.dto.MobDto;
import org.mslowko.turnbasedfighter.pojo.responses.BattleResponse;
import org.springframework.stereotype.Component;

import static org.mslowko.turnbasedfighter.config.Constants.BattleMessages.*;

@Component
@RequiredArgsConstructor
public class ResponseHelper {
    private final ModelMapper modelMapper;

    public BattleResponse battleResponse(Constants.BattleCode battleCode, Dungeon dungeon, String characterAction, String mobAction) {
        String postTurnMessage =
                switch (battleCode) {
                    case TURN -> null;
                    case DOWN -> String.format(UNIT_DEAD, dungeon.getCurrentCharacter().getName());
                    case VICTORY -> VICTORY;
                    case DEFEAT -> GAME_OVER;
                    default -> throw new IllegalStateException("Unexpected value: " + battleCode);
                };
        MobDto mobDto = battleCode == Constants.BattleCode.VICTORY ? null :
                modelMapper.map(dungeon.getCurrentOpponent(), MobDto.class);
        return new BattleResponse(battleCode, dungeon, mobDto, characterAction, mobAction, postTurnMessage);
    }

    public BattleResponse waveResponse(Dungeon dungeon, String prevOpponent, String characterAction) {
        String postTurnMessage = String.format(NEXT_WAVE, prevOpponent);
        MobDto mobDto = modelMapper.map(dungeon.getCurrentOpponent(), MobDto.class);
        return new BattleResponse(Constants.BattleCode.WAVE, dungeon, mobDto, characterAction, null, postTurnMessage);
    }

    public static String attackMessage(String source, String target, int damage) {
        return damage > 0 ?
                String.format(ATTACK, source, target, damage) :
                String.format(MISS, source);
    }

    public static String healMessage(String source, int hp) {
        return String.format(HEAL, source, hp);
    }
}
