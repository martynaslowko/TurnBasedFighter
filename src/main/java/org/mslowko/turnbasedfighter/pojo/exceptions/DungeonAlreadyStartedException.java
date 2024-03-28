package org.mslowko.turnbasedfighter.pojo.exceptions;

public class DungeonAlreadyStartedException extends RuntimeException {
    private static final String ERR_MESSAGE = "Dungeon (id: %s) has already started.";
    public DungeonAlreadyStartedException(String id) {
        super(String.format(ERR_MESSAGE, id));
    }
}
