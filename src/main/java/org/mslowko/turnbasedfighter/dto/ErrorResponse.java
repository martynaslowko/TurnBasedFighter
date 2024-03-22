package org.mslowko.turnbasedfighter.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    public final String message;
    public final String cause;
    public final HttpStatus status;
    public final int code;

    public ErrorResponse(HttpStatus status, Exception e) {
        this.status = status;
        this.code = status.value();
        this.cause = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }
}
