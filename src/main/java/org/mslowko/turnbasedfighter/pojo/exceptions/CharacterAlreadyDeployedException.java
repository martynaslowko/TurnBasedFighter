package org.mslowko.turnbasedfighter.pojo.exceptions;

public class CharacterAlreadyDeployedException extends RuntimeException {
    private static final String ERR_MESSAGE = "Character %s has already been deployed to a Dungeon (id: %s).";
    public CharacterAlreadyDeployedException(String name, String dungeonId) {
        super(String.format(ERR_MESSAGE, name, dungeonId));
    }
}
