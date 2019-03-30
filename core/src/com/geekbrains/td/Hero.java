package com.geekbrains.td;

import com.badlogic.gdx.math.Vector2;

public class Hero {
    private static final Hero ourInstance = new Hero();

    private int hp;
    private int score;
    private int gold;
    private Vector2 position;

    public static Hero getInstance() {
        return ourInstance;
    }

    private Hero() {
        this.hp = 1000;
        this.score = 0;
        this.gold = 0;
        this.position = new Vector2(40, 120);
    }

    public void restart(){
        this.hp = 1000;
        this.score = 0;
        this.gold = 0;
        this.position.set(40, 120);
    }

    public Vector2 getPosition() {
        return position;
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
