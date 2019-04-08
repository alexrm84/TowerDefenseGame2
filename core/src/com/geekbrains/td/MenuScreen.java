package com.geekbrains.td;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font24;
    private BitmapFont font72;
    private Stage stage;

    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/zorque72.ttf");
        createGUI();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.77f, 0.48f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/zorque72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font72;
        skin.add("simpleSkin", textButtonStyle);

        Button btnStartGame = new TextButton("Start Game", skin, "simpleSkin");
        Button btnExitGame = new TextButton("Exit Game", skin, "simpleSkin");
        btnStartGame.setPosition(500, 310);
        btnExitGame.setPosition(500, 210);

        btnStartGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);

            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnStartGame);
        stage.addActor(btnExitGame);

        skin.dispose();
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
