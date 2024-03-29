package org.mslowko.turnbasedfighter.pojo.exceptions;

public class CharacterIncapacitatedException extends RuntimeException {
    private static final String ERR_MESSAGE = "Character %s is incapacitated.";
    public CharacterIncapacitatedException(String name) {
        super(String.format(ERR_MESSAGE, name));
    }
}
