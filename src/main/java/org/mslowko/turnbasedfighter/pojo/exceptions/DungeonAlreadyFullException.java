package org.mslowko.turnbasedfighter.pojo.exceptions;

public class DungeonAlreadyFullException extends RuntimeException {
    private static final String ERR_MESSAGE = "Dungeon (id: %s) is already full - %d out of %d characters.";
    public DungeonAlreadyFullException(String id, int size) {
        super(String.format(ERR_MESSAGE, id, size, size));
    }
}
