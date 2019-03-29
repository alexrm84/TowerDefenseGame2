package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret {
    private GameScreen gameScreen;

    private TextureRegion texture;
    private Vector2 position;
    private Vector2 tmp;
    private int cellX, cellY;
    private float angle;
    private float rotationSpeed;
    private float fireRadius;

    private float fireRate;
    private float fireTime;

    private Monster target;

    public Turret(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 0, 0, 80, 80);
        this.cellX = 8;
        this.cellY = 4;
        this.position = new Vector2(cellX * 80 + 40, cellY * 80 + 40);
        this.rotationSpeed = 180.0f;
        this.target = null;
        this.fireRadius = 100.0f;
        this.tmp = new Vector2(0, 0);
        this.fireRate = 0.4f;
        this.fireTime = 0.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }

    public void update(float dt) {
        if (target != null && !checkMonsterInRange(target)) {
            target = null;
        }
        if (target == null) {
            float maxDst = 10000.0f;
            for (int i = 0; i < gameScreen.getMonsterEmitter().getActiveList().size(); i++) {
                Monster m = gameScreen.getMonsterEmitter().getActiveList().get(i);
                float dst = position.dst(m.getPosition());
                if (dst < maxDst) {
                    target = m;
                    maxDst = dst;
                }
            }
        }
        if (target != null) {
            checkRotation(dt);
            tryToFire(dt);
        }
    }

    public boolean checkMonsterInRange(Monster monster) {
        return Vector2.dst(position.x, position.y, monster.getPosition().x, monster.getPosition().y) < fireRadius;
    }

    public float getAngleToTarget() {
        return tmp.set(target.getPosition()).sub(position).angle();
    }

    public void checkRotation(float dt) {
        if (target != null) {
            float angleTo = getAngleToTarget();
            if (angle > angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle -= rotationSpeed * dt;
                } else {
                    angle += rotationSpeed * dt;
                }
            }
            if (angle < angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle += rotationSpeed * dt;
                } else {
                    angle -= rotationSpeed * dt;
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }
        }
    }

    public void tryToFire(float dt) {
        fireTime += dt;
        if (fireTime > fireRate) {
            fireTime = 0.0f;
            float rad = (float)Math.toRadians(angle);
            gameScreen.getBulletEmitter().setup(position.x, position.y, 250.0f * (float)Math.cos(rad), 250.0f * (float)Math.sin(rad));
        }
    }
}
