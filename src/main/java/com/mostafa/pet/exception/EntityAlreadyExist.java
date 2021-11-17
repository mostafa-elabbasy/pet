package com.mostafa.pet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class EntityAlreadyExist extends RuntimeException{

    public EntityAlreadyExist(String message) {
        super(message);
    }
}
