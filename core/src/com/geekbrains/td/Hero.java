package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hero {

    private int hp;
    private int maxHP;
    private int score;
    private int gold;
    private Vector2 position;
    private Vector2 velocity;
    private TextureRegion textureHp;
    private TextureRegion textureBackHp;
    private Map map;

    public Hero() {
        this.maxHP = 1000;
        this.hp = maxHP;
        this.score = 0;
        this.gold = 500;
        this.position = new Vector2(40, 120);
        this.velocity = new Vector2(0, 100);
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
    }

    public void restart(){
        this.hp = maxHP;
        this.gold = 500;
        this.position.set(40, 120);
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureBackHp, position.x - 30, position.y + 40 - 16);
        batch.draw(textureHp, position.x - 30 + 2, position.y + 40 - 14, 56 * ((float) hp / maxHP), 12);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x< 20.0f || position.x> 160.0f || position.y< 20.0f || position.y> 680.0f) {
//            velocity.x = MathUtils.random(-100,100);
//            velocity.y = MathUtils.random(-100,100);
            velocity.y = -velocity.y;
        }
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
