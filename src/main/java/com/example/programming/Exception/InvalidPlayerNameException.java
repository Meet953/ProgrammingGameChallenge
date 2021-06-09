package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class InvalidPlayerNameException extends RuntimeException {

    private static String error = "INVALID PLAYER NAME";

    public InvalidPlayerNameException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}
