package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {

    private int hp;
    private int maxHP;
    private Vector2 position;
    private Vector2 velocity;
    private TextureRegion[] texture;
    private float animationTimer, timePerFrame;
    private TextureRegion textureHp;
    private TextureRegion textureBackHp;
    private GameScreen gameScreen;

    public Hero(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.maxHP = 1000;
        this.hp = maxHP;
        this.position = new Vector2(40, 120);
        this.velocity = new Vector2(0, 100);
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("animatedKing")).split(80, 80)[0];
        this.timePerFrame = 0.2f;
    }

    public void render(SpriteBatch batch,BitmapFont font) {
        int index = (int) (animationTimer / timePerFrame) % texture.length;
        batch.draw(texture[index], position.x - 40, position.y - 40);
        batch.setColor(1, 1, 1, 0.8f);
        batch.draw(textureBackHp, position.x - 30, position.y + 50 - 16);
        batch.draw(textureHp, position.x - 30 + 2, position.y + 50 - 14, 56 * ((float) hp / maxHP), 12);
        font.draw(batch, "" + hp, position.x - 30, position.y + 52 + 4 * (float) Math.sin(animationTimer * 5), 60, 1, false);
        batch.setColor(1, 1, 1, 1);


    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        animationTimer += dt;
        if (position.x< 20.0f || position.x> 160.0f || position.y< 40.0f || position.y> 670.0f) {
//            velocity.x = MathUtils.random(-100,100);
//            velocity.y = MathUtils.random(-100,100);
            velocity.y = -velocity.y;
        }
    }

    public void takeDamage(int damage){
        this.hp-= damage;
        gameScreen.getInfoEmitter().setup(position.x, position.y, "DMG " + damage);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHp() {
        return hp;
    }

}
