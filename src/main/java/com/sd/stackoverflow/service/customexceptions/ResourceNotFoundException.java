package com.sd.stackoverflow.service.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final int serialVersionID = 1;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
