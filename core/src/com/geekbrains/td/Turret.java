package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret implements Poolable{
    private GameScreen gameScreen;
    private TurretType type;
    private TextureRegion texture;
    private BulletType bulletType;
    private TextureRegion[][] allTextures;
    private Vector2 position;
    private Vector2 tmp;
    private int cellX, cellY;
    private int imageX, imageY;
    private float angle;
    private float rotationSpeed;
    private float fireRadius;
    private float chargeTime;
    private float fireTime;
    private boolean active;
    private int destroyPrice;

    private Monster target;

    public Turret(GameScreen gameScreen, TextureRegion[][] allTextures) {
        this.type = TurretType.RED;
        this.bulletType = BulletType.RED;
        this.gameScreen = gameScreen;
        this.allTextures = allTextures;
        this.cellX = 8;
        this.cellY = 4;
        this.position = new Vector2(cellX * 80 + 40, cellY * 80 + 40);
        this.rotationSpeed = 180.0f;
        this.target = null;
        this.fireRadius = 400.0f;
        this.tmp = new Vector2(0, 0);
        this.chargeTime = 0.5f;
        this.fireTime = 0.0f;
        this.active = false;
    }

    public TurretType getType() {
        return type;
    }

    public void deactivate(){
        this.active = false;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setup(TurretType type, int cellX, int cellY){
        this.type = type;
        this.destroyPrice = type.destroyPrice;
        this.bulletType = type.bulletType;
        this.imageX = type.image_x;
        this.imageY = type.image_y;
        this.fireRadius = type.fireRadius;
        this.rotationSpeed = type.rotationSpeed;
        this.chargeTime = type.chargeTime;
        this.cellX = cellX;
        this.cellY = cellY;
        this.position.set(cellX * 80 + 40, cellY * 80 + 40);
        this.active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(allTextures[imageY][imageX], cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }

    public void update(float dt) {
        if (target != null){
            if (!checkMonsterInRange(target) || ! target.isActive()) {
                target = null;
            }
        }
        if (target == null) {
            float maxDst = fireRadius+100;
            for (int i = 0; i < gameScreen.getMonsterEmitter().getActiveList().size(); i++) {
                Monster m = gameScreen.getMonsterEmitter().getActiveList().get(i);
                float dst = position.dst(m.getPosition());
                if (dst < fireRadius && dst < maxDst) {
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
        if (fireTime > chargeTime) {
            fireTime = 0.0f;
            float angleRadian = (float)Math.toRadians(angle) + MathUtils.random(-0.2f, 0.2f);
            gameScreen.getBulletEmitter().setup(bulletType, position.x + 32 * (float)Math.cos(angleRadian), position.y + 32 * (float)Math.sin(angleRadian), angleRadian, target);
        }
    }


}
