package com.geekbrains.td;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TowerDefenseGame extends Game {
    private SpriteBatch batch;

    // Домашнее задание:
    // - Сделайте аккуратную расстановку турелей разного типа
    // - В зависимости от нажатой кнопки выбирается тип турели
    // - Сделайте рабочими кнопки улучшения и уничтожения пушек
    // - * Продумайте как вы хотите хранить информацию о разных типах и уровнях пушек


    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
