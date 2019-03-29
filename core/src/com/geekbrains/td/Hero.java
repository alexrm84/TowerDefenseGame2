package com.geekbrains.td;

public class Hero {
    private static final Hero ourInstance = new Hero();

    private int hp;
    private int score;
    private int gold;

    public static Hero getInstance() {
        return ourInstance;
    }

    private Hero() {
        this.hp = 1000;
        this.score = 0;
        this.gold = 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
