package com.geekbrains.td;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Monster implements Poolable {
    private GameScreen gameScreen;
    private Map map;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position;
    private Vector2 destination;

    private int currentPoint;
    private Vector2 velocity;

    private int hp;
    private int hpMax;
    private int theCost;
    private int damage;

    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Monster(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2(640, 360);
        this.destination = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.theCost = 50;
        this.damage = 200;
        this.active = false;
    }

    public void activate(float x, float y) {
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(-100.0f, 0.0f);
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.getNextPoint();
        this.active = true;
    }

    public void getNextPoint() {
//        List<Vector2> path = new ArrayList<>();
//        path.add(position);
//        for (int i = 0; i < 5; i++) {
//            Vector2 tmp = path.get(path.size() - 1);
//            int tmpCX = (int) (tmp.x / 80);
//            int tmpCY = (int) (tmp.y / 80);
//            if (tmpCX > 0 && map.isCellEmpty(tmpCX - 1, tmpCY)) {
//                path.add(new Vector2((tmpCX - 1) * 80 + 40, tmpCY * 80 + 40));
//            } else if (tmpCY < 8 && map.isCellEmpty(tmpCX, tmpCY + 1)) {
//                path.add(new Vector2(tmpCX * 80 + 40, (tmpCY + 1) * 80 + 40));
//            } else if (tmpCY > 0 && map.isCellEmpty(tmpCX, tmpCY - 1)) {
//                path.add(new Vector2(tmpCX * 80 + 40, (tmpCY - 1) * 80 + 40));
//            } else {
//                path.add(tmp);
//            }
//        }
//        destination.set(path.get(1));
        destination.set(waveMethod((int)position.x/80, (int)position.y/80,(int)Hero.getInstance().getPosition().x/80,(int)Hero.getInstance().getPosition().y/80));
    }

    private Vector2 waveMethod(int sourceX, int sourceY, int destX, int destY){
        int[][] mass = new int[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        Queue<PointOfTheWay> wave = new LinkedList<>();
        wave.add(new PointOfTheWay(sourceX,sourceY,null,0));
        PointOfTheWay checkDest=null;
        PointOfTheWay current=null;
        while (!wave.isEmpty()){
            if (wave.peek().getX()==destX && wave.peek().getY()==destY){
                checkDest = wave.peek();
                break;
            }
            cellsCheck(wave.peek().getX(), wave.peek().getY(), mass, wave);
            wave.poll();
        }
        if (checkDest!=null){
            current = checkDest;
            while (current.getPrevious().getPrevious()!=null){
                current = current.getPrevious();
            }
        }
        return current!=null ? new Vector2(current.getX()*80+40,current.getY()*80+40) : new Vector2(sourceX*80+40, sourceY*80+40);
    }

    private void cellsCheck(int x, int y, int[][] mass, Queue<PointOfTheWay> wave){
        for (int i = -1; i < 2 ; i++) {
            for (int j = -1; j < 2; j++) {
                if (Math.abs(i+j)==1){
                    if (map.isCellEmpty(x+i,y+j) && mass[x+i][y+j]==0){
                        mass[x+i][y+j] = mass[x][y] +1;
                        wave.offer(new PointOfTheWay(x+i, y+j, wave.peek(), mass[x+i][y+j]));
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40);
        batch.draw(textureHp, position.x - 40, position.y + 40 - 12, 56 * ((float) hp / hpMax), 12);
    }

    public void update(float dt) {
        velocity.set(destination).sub(position).nor().scl(100.0f);
        position.mulAdd(velocity, dt);
        if (position.dst(Hero.getInstance().getPosition())<5){
            this.active = false;
            Hero.getInstance().setHp(Hero.getInstance().getHp()-this.damage);
        }
        if (Hero.getInstance().getHp()<=0){
            gameScreen.show();
            Hero.getInstance().restart();
        }
        for (int i = 0; i < gameScreen.getBulletEmitter().activeList.size(); i++) {
            if (this.position.dst(gameScreen.getBulletEmitter().activeList.get(i).getPosition()) < 30){
                gameScreen.getBulletEmitter().activeList.get(i).deactivate();
                this.hp -= gameScreen.getBulletEmitter().activeList.get(i).getDamage();
                if (this.hp<=0){
                    this.active = false;
                    Hero.getInstance().setScore(Hero.getInstance().getScore()+this.theCost);
                    Hero.getInstance().setGold(Hero.getInstance().getGold()+theCost*2);
                }
            }
        }

        if (position.dst(destination) < 2.0f) {
            getNextPoint();
        }
    }
}
