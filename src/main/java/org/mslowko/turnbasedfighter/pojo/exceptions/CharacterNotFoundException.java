package org.mslowko.turnbasedfighter.pojo.exceptions;

public class CharacterNotFoundException extends RuntimeException {
    private static final String ERR_MESSAGE = "Character %s not found.";
    public CharacterNotFoundException(String name) {
        super(String.format(ERR_MESSAGE, name));
    }
}
