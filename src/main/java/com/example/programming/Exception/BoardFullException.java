package com.example.programming.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class BoardFullException extends RuntimeException {
    private static String error = "BOARD IS FULL. CAN'T PROCEED ";

    public BoardFullException() {
        super(error);
    }

    public static String getError() {
        return error;
    }
}


