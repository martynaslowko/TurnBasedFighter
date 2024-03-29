package org.mslowko.turnbasedfighter.api;

import lombok.extern.slf4j.Slf4j;
import org.mslowko.turnbasedfighter.pojo.exceptions.*;
import org.mslowko.turnbasedfighter.pojo.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({PlayerNotFoundException.class, CharacterNotFoundException.class, DungeonNotFoundException.class})
    public ResponseEntity<Object> handlePlayerNotFoundException(RuntimeException e) {
        return parseError(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, CharacterAlreadyDeployedException.class,
            DungeonAlreadyFullException.class, DungeonAlreadyStartedException.class,
            CharacterTurnDeniedException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(RuntimeException e) {
        return parseError(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<Object> parseError(HttpStatus status, RuntimeException e) {
        log.info("GlobalExceptionHandler has caught and handled an error: {}", e.getMessage());
        return ResponseEntity.status(status).body(new ErrorResponse(status, e));
    }
}
