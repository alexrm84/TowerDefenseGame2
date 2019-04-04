package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.allTextures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets")).split(80, 80);
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen, allTextures);
    }

    public boolean setup(TurretType type, int cellX, int cellY) {
        if (!canIDeployItHere(cellX, cellY)) {
            return false;
        }
        Turret turret = getActiveElement();
        turret.setup(type, cellX, cellY);
        return true;
    }

    public void buildTurret(Hero hero, TurretType type, int cellX, int cellY){
        if (hero.isMoneyEnough(type.price)){
            if (setup(type,cellX,cellY)){
                hero.changeGold(-type.price);
                gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "-" + type.price);
            }
        }
    }

    public void upgradeTurret(Hero hero, int cellX, int cellY){
        Turret turretForUpgrade = findeTurretInCell(cellX, cellY);
        if (turretForUpgrade == null){
            return;
        }
        TurretType nextLevel = turretForUpgrade.getType().child;
        if (nextLevel == null){
            gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "[ERROR] Top turret");
            return;
        }
        if (hero.isMoneyEnough(nextLevel.price)){
            turretForUpgrade.setup(nextLevel, cellX, cellY);
            hero.changeGold(-nextLevel.price);
            gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "-" + nextLevel.price);
        }
    }

    public void removeTurret(Hero hero, int cellX, int cellY){
        Turret turretForDelete = findeTurretInCell(cellX, cellY);
        if (turretForDelete == null){
            return;
        }
        turretForDelete.deactivate();
        hero.changeGold(turretForDelete.getType().destroyPrice);
        gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "+" + turretForDelete.getType().destroyPrice);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public boolean canIDeployItHere(int cellX, int cellY) {
        if (!gameScreen.getMap().isCellEmpty(cellX, cellY)) {
            return false;
        }
        Turret turret;
        for (int i = 0; i < activeList.size(); i++) {
            turret = activeList.get(i);
            if (turret.getCellX() == cellX && turret.getCellY() == cellY) {
                return false;
            }
        }
        return true;
    }

    public Turret findeTurretInCell(int cellX, int cellY){
        for (int i = 0; i < activeList.size(); i++) {
            Turret turret = activeList.get(i);
            if (turret.isActive() && turret.getCellX() == cellX && turret.getCellY() == cellY) {
                return turret;
            }
        }
        return null;
    }
}
