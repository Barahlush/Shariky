package com.shariky.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shariky.helpers.AssetLoader;

import static com.shariky.helpers.AssetLoader.basicBatch;
import static com.shariky.helpers.AssetLoader.basicFont;

/**
 * Created by User on 19.11.2016.
 */

public class Shariky extends Game {

    public int record, score;

    public SpriteBatch batch;
    public BitmapFont font;
    public AssetLoader loader;
    static public boolean sound_ON;
    public enum AppState {
        MAIN_MENU,
        GAME,
        FAILSCREEN,
        QUIT_MENU
    }

    public float width = 480;
    public float height = 800;

    @Override
    public void create() {
        sound_ON = true;
        loader = new AssetLoader();
        loader.load();
        batch = basicBatch;
        font = basicFont;
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        loader.dispose();
    }
}
