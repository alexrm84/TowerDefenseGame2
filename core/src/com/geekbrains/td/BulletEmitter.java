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

    public void setup(BulletType type, float x, float y, float angleRed, Monster target) {
        Bullet b = getActiveElement();
        b.setup(type, x, y, angleRed, target);
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
            if (b.isAutoaim()){
                if (b.getTarget().isActive()) {
                    tmp.set(b.getTarget().getPosition()).sub(b.getPosition()).nor().scl(b.getSpeed());
                }else{
                    b.setAutoaim(false);
                }
            }
            b.getPosition().mulAdd(tmp, dt);
            gameScreen.getParticleEmitter().setup(b.getPosition().x, b.getPosition().y, MathUtils.random(-25, 25), MathUtils.random(-5, 5), 0.3f, 0.6f, 2.4f, 0, 0, 0, 1, 0, 0, 0, 0.2f);
            gameScreen.getParticleEmitter().setup(b.getPosition().x, b.getPosition().y, MathUtils.random(-25, 25), MathUtils.random(-25, 25), 0.04f, 1.0f, 0.4f, 1, 0, 0, 1, 1, 1, 0, 1);
        }
    }
}
