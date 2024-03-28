package org.mslowko.turnbasedfighter.pojo.exceptions;

public class InvalidJoinKeyException extends RuntimeException {
    private static final String ERR_MESSAGE = "Invalid join key provided.";
    public InvalidJoinKeyException() {
        super(ERR_MESSAGE);
    }
}
