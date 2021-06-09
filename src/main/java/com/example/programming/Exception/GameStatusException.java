package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameStatusException extends RuntimeException {
    private static String error = "GAME HAS NOT STARTED YET";

    public GameStatusException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}
