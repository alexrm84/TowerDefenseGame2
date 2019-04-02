package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen);
    }

    public boolean setup(int cellX, int cellY, String type) {
        if (!canIDeployItHere(cellX, cellY)) {
            return false;
        }
        Turret turret = getActiveElement();
        turret.setup(cellX, cellY, type);
        return true;
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

    public String checkTheTurret(int cellX, int cellY){
        Turret t;
        for (int i = 0; i < activeList.size(); i++) {
            t = activeList.get(i);
            if (t.getCellX() == cellX && t.getCellY() == cellY){
                return t.getType();
            }
        }
        return "";
    }

    public int destroyTurret(int cellX, int cellY){
        Turret t;
        for (int i = 0; i < activeList.size(); i++) {
            t = activeList.get(i);
            if (t.getCellX() == cellX && t.getCellY() == cellY){
                t.deactivate();
                return t.getTheCoast()/2;
            }
        }
        return -1;
    }

    public boolean canIDeployItHere(int cellX, int cellY) {
        if (!gameScreen.getMap().isCellEmpty(cellX, cellY)) {
            return false;
        }
        Turret t;
        for (int i = 0; i < activeList.size(); i++) {
            t = activeList.get(i);
            if (t.getCellX() == cellX && t.getCellY() == cellY) {
                System.out.println("2");
                return false;
            }
        }
        return true;
    }

    public int getTheCoast(String type) {
        switch (type){
            case "blue0":
                return 200;
            case "blue1":
                return 400;
            case "red0":
                return 50;
            case "red1":
                return 100;
        }
        return -1;
    }
}
