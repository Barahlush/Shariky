package com.shariky.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;

import com.shariky.gamefield.GameRenderer;
import com.shariky.gamefield.GameWorld;

import static com.shariky.helpers.AssetLoader.musicBall;

public class GameScreen implements Screen {

    final Shariky game;
    private GameWorld world;
    private GameRenderer renderer;




	public GameScreen (final Shariky gam) {
        this.game = gam;
        world = new GameWorld(this.game);
        renderer = new GameRenderer(world, this.game);
        world.setCamera(renderer.getCamera());


        musicBall.setLooping(true);
		musicBall.play();

        Gdx.gl.glClearColor(1, 1, 1, 1);
	}

    // Отрисовка
	@Override
	public void render (float delta) {
        world.update(delta);
        renderer.render();
	}

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        musicBall.stop();
    }

    // Очистка от мусора после закрытия
    @Override
	public void dispose () {
        musicBall.dispose();
	}

    @Override
    public void show() {
        musicBall.play();
    }
}
