package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {

    private int hp;
    private int maxHP;
    private int score;
    private int gold;
    private Vector2 position;

    public Hero() {
        this.maxHP = 1000;
        this.hp = maxHP;
        this.score = 0;
        this.gold = 500;
        this.position = new Vector2(40, 120);
    }

    public void restart(){
        this.hp = maxHP;
        this.score = 0;
        this.gold = 500;
        this.position.set(40, 120);
    }

    public void takeDamage(int damage){
        this.hp-= damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isMoneyEnough(int amount) {
        return gold >= amount;
    }

    public void changeGold(int gold) {
        this.gold += gold;
    }

    public int getGold() {
        return gold;
    }

    public int getHp() {
        return hp;
    }

    public int getScore() {
        return score;
    }

    public void changeScore(int score) {
        this.score += score;
    }

    public void renderInfo(SpriteBatch batch, BitmapFont font){
        font.draw(batch, "HP: " + this.hp + "/" + this.maxHP, 20, 700);
        font.draw(batch, "SCORE: " + this.getScore(), 20, 670);
        font.draw(batch, "GOLD: " + this.getGold(), 20, 640);
    }
}
