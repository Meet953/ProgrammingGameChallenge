package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMoveException extends RuntimeException {
    private static String error = "INVALID MOVE. TRY AGAIN ! ";

    public InvalidMoveException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}
