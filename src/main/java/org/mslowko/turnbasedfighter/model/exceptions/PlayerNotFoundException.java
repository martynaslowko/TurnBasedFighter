package org.mslowko.turnbasedfighter.model.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    private static final String ERR_MESSAGE = "Player %s not found.";
    public PlayerNotFoundException(String name) {
        super(String.format(ERR_MESSAGE, name));
    }
}
