package com.geekbrains.td;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

//    Игровое окно.

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Map map;
    private TurretEmitter turretEmitter;
    private MonsterEmitter monsterEmitter;
    private BulletEmitter bulletEmitter;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private InfoEmitter infoEmitter;
    private int selectedCellX, selectedCellY;
    private BitmapFont font24;
    private float monsterTimer;
    private float monsterWave;
    private float waveTimer;
    private float level = 0;
    private Hero hero;
    private Player player;
    private Stage stage;
    private Group groupTurretAction;
    private Group groupTurretSelection;


    public Hero getHero() {
        return hero;
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap(){return map;}

    public float getLevel() {
        return level;
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public InfoEmitter getInfoEmitter() {
        return infoEmitter;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

//    Инициализация объектов
//    Создание интерфейса который считывает события ввода.


    @Override
    public void show() {
        this.hero = new Hero(this);
        this.player = new Player();
        this.mousePosition = new Vector2(0, 0);
        this.particleEmitter = new ParticleEmitter();
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.bulletEmitter = new BulletEmitter(this);
        this.map = new Map("level01.map");
        this.monsterEmitter = new MonsterEmitter(this);
        this.turretEmitter = new TurretEmitter(this);
        this.infoEmitter = new InfoEmitter(this);
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
        this.monsterWave = 1;
        createGUI();
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();
        map.render(batch);

        batch.setColor(1, 1, 0, 0.5f);
        batch.draw(selectedCellTexture, selectedCellX * 80, selectedCellY * 80);
        batch.setColor(1, 1, 1, 1);


        monsterEmitter.render(batch);
        turretEmitter.render(batch);
        bulletEmitter.render(batch);
        particleEmitter.render(batch);
        hero.render(batch);
        player.renderInfo(batch, font24);
        infoEmitter.render(batch, font24);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        map.update(dt);
        monsterEmitter.update(dt);
        turretEmitter.update(dt);
        particleEmitter.update(dt);
        generateMonsters(dt);
        bulletEmitter.update(dt);
        checkCollisions();
        infoEmitter.update(dt);
        hero.update(dt);

        nextLevel();

        monsterEmitter.checkPool();
        particleEmitter.checkPool();
        bulletEmitter.checkPool();
        turretEmitter.checkPool();
        infoEmitter.checkPool();
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition.set(screenX, screenY);
                ScreenManager.getInstance().getViewport().unproject(mousePosition);
                if (selectedCellX == (int) (mousePosition.x / 80) && selectedCellY == (int) (mousePosition.y / 80)) {
                    map.setWall((int) (mousePosition.x / 80), (int) (mousePosition.y / 80));
                }
                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);
                return true;
            }
        };

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnMenu = new TextButton("Menu", skin, "simpleSkin");
        btnMenu.setPosition(150, 630);

        groupTurretAction = new Group();
        groupTurretAction.setPosition(240, 620);
        Button btnSetTurret = new TextButton("Set", skin, "simpleSkin");
        Button btnUpgradeTurret = new TextButton("Upg", skin, "simpleSkin");
        Button btnDestroyTurret = new TextButton("Dst", skin, "simpleSkin");
        btnSetTurret.setPosition(10, 10);
        btnUpgradeTurret.setPosition(110, 10);
        btnDestroyTurret.setPosition(210, 10);
        groupTurretAction.addActor(btnSetTurret);
        groupTurretAction.addActor(btnUpgradeTurret);
        groupTurretAction.addActor(btnDestroyTurret);

        groupTurretSelection = new Group();
        groupTurretSelection.setVisible(false);
        groupTurretSelection.setPosition(240, 520);
        Button btnSetTurret1 = new TextButton("T1", skin, "simpleSkin");
        Button btnSetTurret2 = new TextButton("T2", skin, "simpleSkin");
        btnSetTurret1.setPosition(10, 10);
        btnSetTurret2.setPosition(110, 10);
        groupTurretSelection.addActor(btnSetTurret1);
        groupTurretSelection.addActor(btnSetTurret2);

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);

            }
        });

        btnSetTurret1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.buildTurret(TurretType.RED, selectedCellX, selectedCellY);

            }
        });

        btnSetTurret2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.buildTurret(TurretType.BLUE, selectedCellX, selectedCellY);
            }
        });

        btnDestroyTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.removeTurret(selectedCellX, selectedCellY);

            }
        });

        btnUpgradeTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.upgradeTurret(selectedCellX, selectedCellY);

            }
        });

        stage.addActor(groupTurretSelection);
        stage.addActor(groupTurretAction);
        stage.addActor(btnMenu);

//        upperPanel = new UpperPanel(playerInfo, stage, 0, 720 - 60);

        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                groupTurretSelection.setVisible(!groupTurretSelection.isVisible());
            }
        });
        skin.dispose();
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet bullet = bulletEmitter.getActiveList().get(i);
            if (bullet.getPosition().x < 0 || bullet.getPosition().x > 1280 ||
                    bullet.getPosition().y < 0 || bullet.getPosition().y > 720) {
                bullet.deactivate();
                continue;
            }
            if (!map.isCellEmpty((int) (bullet.getPosition().x / 80), (int) (bullet.getPosition().y / 80))) {
                bullet.deactivate();
                continue;
            }
            for (int j = 0; j < monsterEmitter.getActiveList().size(); j++) {
                Monster monster = monsterEmitter.getActiveList().get(j);
                if (monster.getPosition().dst(bullet.getPosition()) < 50){
                    bullet.deactivate();
                    if (monster.takeDamage(bullet.getPower())) {
                        player.changeGold(monster.getTheCost());
                        player.changeScore(monster.getTheCost());
                        particleEmitter.getEffectBuilder().buildMonsterSplash(monster.getPosition().x, monster.getPosition().y);
                    }
                }
            }
        }
        for (int i = 0; i < monsterEmitter.getActiveList().size(); i++) {
            if (monsterEmitter.getActiveList().get(i).getPosition().dst(hero.getPosition())<20){
                monsterEmitter.getActiveList().get(i).deactivate();
                hero.takeDamage(monsterEmitter.getActiveList().get(i).getHp());
                if (hero.getHp()<=0){
                    this.show();
                }
            }
        }
    }

    public void generateMonsters(float dt) {
        monsterTimer += dt;
        waveTimer += dt;
        if (monsterTimer > 2.0f-MathUtils.log(10, monsterWave)) {
            for (int i = 0; i < monsterWave+1; i++) {
                monsterEmitter.setup(15, MathUtils.random(0, 8));
            }
            monsterTimer = 0;
        }
        if (waveTimer>20){
            monsterWave +=1;
            waveTimer = 0;
        }
    }

    public void nextLevel(){
        if (monsterWave > 5){
            level += 1;
            this.hero = new Hero(this);
            this.bulletEmitter = new BulletEmitter(this);
            this.map = new Map("level01.map");
            this.monsterEmitter = new MonsterEmitter(this);
            this.turretEmitter = new TurretEmitter(this);
            this.monsterWave = 1;
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
