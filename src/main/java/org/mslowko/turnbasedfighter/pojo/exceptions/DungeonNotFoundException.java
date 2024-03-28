package org.mslowko.turnbasedfighter.pojo.exceptions;

public class DungeonNotFoundException extends RuntimeException {
    private static final String ERR_MESSAGE = "Dungeon (id: %s) not found.";
    public DungeonNotFoundException(String id) {
        super(String.format(ERR_MESSAGE, id));
    }
}
