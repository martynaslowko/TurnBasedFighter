package org.mslowko.turnbasedfighter.pojo.exceptions;

import lombok.Getter;
import org.mslowko.turnbasedfighter.model.Dungeon;

@Getter
public class CharacterTurnDeniedException extends RuntimeException {
    private static final String ERR_MESSAGE = "Character %s is not yet eligible for action. Wait for it's turn.";
    private final String dungeonId;
    private final String currentTurn;
    public CharacterTurnDeniedException(String name, String dungeonId, String currentTurn) {
        super(String.format(ERR_MESSAGE, name));
        this.dungeonId = dungeonId;
        this.currentTurn = currentTurn;
    }

    public CharacterTurnDeniedException(String name, Dungeon dungeon) {
        super(String.format(ERR_MESSAGE, name));
        this.dungeonId = dungeon.getId();
        this.currentTurn = dungeon.getCurrentCharacter().getName();
    }
}
