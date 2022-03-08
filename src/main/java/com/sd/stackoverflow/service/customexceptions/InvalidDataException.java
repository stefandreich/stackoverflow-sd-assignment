package com.sd.stackoverflow.service.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidDataException extends RuntimeException {
    private static final int serialVersionID = 1;

    public InvalidDataException(String message) {
        super(message);
    }
}
