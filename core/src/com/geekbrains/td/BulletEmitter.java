package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BulletEmitter extends ObjectPool<Bullet> {

    private GameScreen gameScreen;
    private TextureRegion bulletTexture;
    private Vector2 tmp;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.tmp = new Vector2(0, 0);
    }

    public void setup(float x, float y, float vx, float vy, Monster target, int damage) {
        Bullet b = getActiveElement();
        b.setup(x, y, vx, vy, target, damage);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1,0,0,1);
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(bulletTexture, b.getPosition().x - 8, b.getPosition().y - 8);
        }
        batch.setColor(1,1,1,1);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            tmp.set(b.getVelocity());
//            if (b.getTarget().isActive()) {
//                tmp.set(b.getTarget().getPosition()).sub(b.getPosition()).nor().scl(150.0f);
//            }
            b.getPosition().mulAdd(tmp, dt);
            gameScreen.getParticleEmitter().setup(b.getPosition().x, b.getPosition().y, MathUtils.random(-25, 25), MathUtils.random(-25, 25), 0.1f,1.2f,0.2f,1,0,0,1,1,1,0,1);
        }
    }

}
