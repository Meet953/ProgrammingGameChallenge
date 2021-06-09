package com.example.programming.model;

public enum Sign {
    X('X'),
    O('O'),
    E('~');

    public char symbol;

    Sign(char symbol) {
        this.symbol = symbol;
    }

}
