package org.mslowko.turnbasedfighter.pojo.exceptions;

public class DungeonNotStartedException extends RuntimeException {
    private static final String ERR_MESSAGE = "Dungeon (id: %s) hasn't started yet.";
    public DungeonNotStartedException(String id) {
        super(String.format(ERR_MESSAGE, id));
    }
}
