package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret implements Poolable{
    private GameScreen gameScreen;

    private TextureRegion texture;
    private Vector2 position;
    private Vector2 tmp;
    private int cellX, cellY;
    private float angle;
    private float rotationSpeed;
    private float fireRadius;
    private boolean active;
    private String type;
    private int damage;
    private int theCoast;

    private float fireRate;
    private float fireTime;

    private Monster target;

    public Turret(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.cellX = 8;
        this.cellY = 4;
        this.position = new Vector2(cellX * 80 + 40, cellY * 80 + 40);
        this.rotationSpeed = 180.0f;
        this.target = null;
        this.fireRadius = 300.0f;
        this.tmp = new Vector2(0, 0);

        this.fireTime = 0.0f;
        this.active = false;
    }

    public String getType() {
        return type;
    }

    public int getTheCoast() {
        return theCoast;
    }

    public void deactivate(){
        this.active = false;
    }

    private void textureSelection(String type){
        switch (type){
            case "blue0":
                this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 80, 0, 80, 80);
                this.fireRate = 1.4f;
                this.damage = 70;
                this.theCoast = 200;
                break;
            case "blue1":
                this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 80, 80, 80, 80);
                this.fireRadius = 500.0f;
                this.fireRate = 1.0f;
                this.rotationSpeed = 220;
                this.damage = 100;
                this.theCoast = 400;
                break;
            case "red0":
                this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 0, 0, 80, 80);
                this.fireRate = 0.4f;
                this.damage = 10;
                this.theCoast = 50;
                break;
            case "red1":
                this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 0, 80, 80, 80);
                this.fireRadius = 500.0f;
                this.fireRate = 0.2f;
                this.rotationSpeed = 220;
                this.damage = 20;
                this.theCoast = 100;
                break;
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setup(int cellX, int cellY, String type){
        this.type = type;
        textureSelection(type);
        this.cellX = cellX;
        this.cellY = cellY;
        this.position.set(cellX * 80 + 40, cellY * 80 + 40);
        this.active = true;
    }

    public int getDamage() {
        return damage;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }

    public void update(float dt) {
        if (target != null){
            if (!checkMonsterInRange(target) || ! target.isActive()) {
                target = null;
            }
        }
        if (target == null) {
            float maxDst = fireRadius+50;
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
        if (fireTime > fireRate) {
            fireTime = 0.2f;
            float rad = (float)Math.toRadians(angle);
            gameScreen.getBulletEmitter().setup(position.x, position.y, 500.0f * (float)Math.cos(rad), 500.0f * (float)Math.sin(rad),target, damage);
        }
    }


}
