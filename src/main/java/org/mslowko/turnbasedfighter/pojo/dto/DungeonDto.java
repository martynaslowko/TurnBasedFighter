package org.mslowko.turnbasedfighter.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Mob;

import java.util.List;

@Data
@NoArgsConstructor
public class DungeonDto {
    private String id;
    private int currentWave;
    private int waves;
    private int slots;
    private boolean started;
    private List<Character> lobby;
    private Character currentCharacter;
    private Mob currentOpponent;
}
