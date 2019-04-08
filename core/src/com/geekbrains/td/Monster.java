package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Monster implements Poolable {
    private GameScreen gameScreen;
    private Map map;
    private Hero hero;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private TextureRegion textureBackHp;
    private Vector2 position;
    private Vector2 destination;
    private Vector2 velocity;

    private int hp;
    private int hpMax;
    private int theCost;

    private boolean active;

    private int[][] mass;
    private Queue<PointOfTheWay> wave;

    public Monster(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
        this.position = new Vector2(640, 360);
        this.destination = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.theCost = 10;
        this.active = false;
        this.hero = gameScreen.getHero();
        this.mass = new int[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        this.wave = new LinkedList<>();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHp() {
        return hp;
    }

    public int getTheCost() {
        return theCost;
    }

    public void activate(float x, float y) {
//        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
//        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
//        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(-100.0f, 0.0f);
        this.hpMax = 100+(int)gameScreen.getLevel()*30;
        this.hp = this.hpMax;
        this.theCost = 10+(int)gameScreen.getLevel();
        this.getNextPoint();
        this.active = true;
    }

    public void deactivate(){
        this.active = false;
    }

    public void getNextPoint() {
        for (int i = 0; i < map.getMAP_WIDTH(); i++) {
            for (int j = 0; j < map.getMAP_HEIGHT(); j++) {
                mass[i][j] = 0;
            }
        }
        wave.clear();
        destination.set(waveMethod((int)hero.getPosition().x/80,(int)hero.getPosition().y/80, (int)position.x/80, (int)position.y/80));
    }

    private Vector2 waveMethod(int sourceX, int sourceY, int destX, int destY){
        wave.add(new PointOfTheWay(sourceX,sourceY,null,0));
        PointOfTheWay checkDest=null;
        while (!wave.isEmpty()){
            if (wave.peek().getX()==destX && wave.peek().getY()==destY){
                checkDest = wave.peek();
                break;
            }
            cellsCheck(wave.peek().getX(), wave.peek().getY());
            wave.poll();
        }
        return (checkDest!=null && checkDest.getPrevious()!=null) ? new Vector2(checkDest.getPrevious().getX()*80+40,checkDest.getPrevious().getY()*80+40) : new Vector2(destX*80+40, destY*80+40);
    }

    private void cellsCheck(int x, int y){
        if (map.isCellEmpty(x,y+1) && mass[x][y+1]==0){
            wave.offer(new PointOfTheWay(x,y+1, wave.peek(), mass[x][y+1]=mass[x][y]+1));
            mass[x][y+1]=mass[x][y]+1;
        }
        if (map.isCellEmpty(x,y-1) && mass[x][y-1]==0){
            wave.offer(new PointOfTheWay(x, y-1, wave.peek(), mass[x][y-1]=mass[x][y]+1));
            mass[x][y-1]=mass[x][y]+1;
        }
        if (map.isCellEmpty(x+1,y) && mass[x+1][y]==0){
            wave.offer(new PointOfTheWay(x+1, y, wave.peek(), mass[x+1][y]=mass[x][y]+1));
            mass[x+1][y]=mass[x][y]+1;
        }
        if (map.isCellEmpty(x-1,y) && mass[x-1][y]==0){
            wave.offer(new PointOfTheWay(x-1, y, wave.peek(), mass[x-1][y]=mass[x][y]+1));
            mass[x-1][y]=mass[x][y]+1;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40);
        batch.draw(textureBackHp, position.x - 30, position.y + 40 - 16);
        batch.draw(textureHp, position.x - 30 + 2, position.y + 40 - 14, 56 * ((float) hp / hpMax), 12);
    }

    public boolean takeDamage(int damage){
        this.hp-=damage;
        if (this.hp<=0){
            this.deactivate();
            gameScreen.getInfoEmitter().setup(position.x+10, position.y, "+" + theCost);
            return true;
        }
        return false;
    }

    public void update(float dt) {
        velocity.set(destination).sub(position).nor().scl(100.0f+gameScreen.getLevel()*10);
        position.mulAdd(velocity, dt);
        if (position.dst(destination) < 2.0f) {
            getNextPoint();
        }
    }
}
