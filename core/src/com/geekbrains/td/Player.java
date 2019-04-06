package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private int score;
    private int gold;

    public Player() {
        this.score = 0;
        this.gold = 500;
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

    public int getScore() {
        return score;
    }

    public void changeScore(int score) {
        this.score += score;
    }

    public void renderInfo(SpriteBatch batch, BitmapFont font){
        font.draw(batch, "SCORE: " + this.getScore(), 20, 710);
        font.draw(batch, "GOLD: " + this.getGold(), 20, 680);
    }
}
