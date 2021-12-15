package ru.spin.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BricksetNotFoundException extends RuntimeException {
    public BricksetNotFoundException(String message) {
        super(message);
    }
}
