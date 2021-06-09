package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class NoSignsAvailableException extends RuntimeException {

    private static String error = "GAME LOBBY LOBBY_FULL. WAIT FOR GAME TO FINISH";

    public NoSignsAvailableException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}
