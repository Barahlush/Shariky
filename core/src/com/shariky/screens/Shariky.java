package com.shariky.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

    SpriteBatch batch;
    BitmapFont font;

    public float gameWidth = 480;
    public float gameHeight = 800;

    @Override
    public void create() {
        AssetLoader.load();
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
        AssetLoader.dispose();
    }
}
