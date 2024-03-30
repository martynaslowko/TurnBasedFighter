package org.mslowko.turnbasedfighter.pojo.responses;

import lombok.Getter;
import org.mslowko.turnbasedfighter.config.Constants;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.pojo.dto.CharacterPreviewDto;
import org.mslowko.turnbasedfighter.pojo.dto.MobDto;

import java.util.List;

@Getter
public class BattleResponse {

    private final Constants.BattleCode code;
    private final String characterAction;
    private final String mobAction;
    private final String postTurnMessage;
    private final String wave;
    private final MobDto currentOpponent;
    private final List<CharacterPreviewDto> characters;

    public BattleResponse(Constants.BattleCode code, Dungeon dungeon, MobDto mobDto, String characterAction, String mobAction, String postTurnMessage) {
        this.code = code;
        this.characterAction = characterAction;
        this.mobAction = mobAction;
        this.postTurnMessage = postTurnMessage;
        this.wave = String.format("%d/%d", dungeon.getCurrentWave(), dungeon.getWaves());
        this.currentOpponent = mobDto;
        this.characters = dungeon.getLobby().stream()
                .filter(c -> c.getHp() > 0)
                .map(c -> new CharacterPreviewDto(c.getName(), c.getHp(), c.getLevel()))
                .toList();
    }
}
