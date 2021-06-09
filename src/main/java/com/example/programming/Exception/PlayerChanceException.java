package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PlayerChanceException extends RuntimeException {

    private static String error = "PLAYER HAS ALREADY MADE A MOVE. WAITING FOR OTHER PLAYER";

    public PlayerChanceException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}

