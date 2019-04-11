package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret implements Poolable{
    private GameScreen gameScreen;
    private String turretTemplateName;
    private String bulletTemplateName;
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
    private int maxHP;
    private int currentHP;
    private TextureRegion textureHp;
    private TextureRegion textureBackHp;
    private boolean active;

    private Monster target;

    public Turret(GameScreen gameScreen, TextureRegion[][] allTextures) {
        this.gameScreen = gameScreen;
        this.allTextures = allTextures;
        this.position = new Vector2(0, 0);
        this.tmp = new Vector2(0, 0);
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
        this.active = false;
    }

    public void deactivate(){
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public String getTurretTemplateName() {
        return turretTemplateName;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setup(TurretTemplate template, int cellX, int cellY){
        this.turretTemplateName = template.getName();
        this.bulletTemplateName = template.getBulletName();
        this.imageX = template.getImageX();
        this.imageY = template.getImageY();
        this.fireRadius = template.getFireRadius();
        this.rotationSpeed = template.getRotationSpeed();
        this.chargeTime = template.getChargeTime();
        this.cellX = cellX;
        this.cellY = cellY;
        this.position.set(cellX * 80 + 40, cellY * 80 + 40);
        this.active = true;
        this.maxHP = template.getMaxHP();
        this.currentHP = maxHP;

    }

    public void render(SpriteBatch batch) {
        batch.draw(allTextures[imageY][imageX], cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
        batch.draw(textureBackHp, cellX * 80+10, cellY * 80+60);
        batch.draw(textureHp, cellX * 80+10, cellY * 80+60, 56 * ((float) currentHP / maxHP), 14);
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
            gameScreen.getBulletEmitter().setup(bulletTemplateName, position.x + 32 * (float)Math.cos(angleRadian), position.y + 32 * (float)Math.sin(angleRadian), angleRadian, target);
        }
    }

    public void takeDamage(int damage){
        currentHP -= damage;
        if (currentHP <= 0){
            deactivate();
        }
    }

}
