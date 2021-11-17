package com.mostafa.pet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class PetIsNotAvailable extends RuntimeException{

    public PetIsNotAvailable(String message) {
        super(message);
    }
}
