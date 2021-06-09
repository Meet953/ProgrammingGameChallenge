package com.example.programming.model;

public class Player {

    private boolean chance;
    private Sign sign;
    private String name;
    private boolean winner;

    public Player(){}
    public Player(String name) {
        this.name = name;
    }

    public boolean isChance() {
        return chance;
    }

    public void setChance(boolean chance) {
        this.chance = chance;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Player [ " +
                "Name =" + name +
                ", Sig =" + sign +
                ", Chance ='" + chance + '\'' +
                ']';
    }
}
