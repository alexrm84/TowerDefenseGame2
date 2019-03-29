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

    /// Домашнее задание:
    // - Добавить столкновение пуль с монстрами, у монстра должно отниматься здоровье,
    // если здоровье упало ниже нуля, то выключаем монстра. За уничтожение монстра
    // начисляем очки
    // - Создайте класс Игрок, и вынесите в него очки, здоровье, количество монет
    // За уничтожение монстра добавляем игроку монет. Если монстр добрался до левой
    // стороны экрана, то уменьшаем здоровье у игрока. (здоровье отображать текстом)
    // - * Реализовать волновой алгоритм для поиска пути

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
