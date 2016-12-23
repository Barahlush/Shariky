package com.shariky.screens;

import com.badlogic.gdx.Screen;
import com.shariky.gamefield.GameRenderer;
import com.shariky.gamefield.GameWorld;

import static com.shariky.helpers.AssetLoader.musicBall;

public class GameScreen implements Screen {

    final Shariky game;
    private GameWorld world;
    private GameRenderer renderer;
    public enum State {
        RUN,
        PAUSE,
        RESUME,
        STOPPED
    }
    private State gameState = State.RUN;


	public GameScreen (final Shariky gam) {
        this.game = gam;
        world = new GameWorld(this.game);
        renderer = new GameRenderer(world, this.game);
        world.setCamera(renderer.getCamera());

        musicBall.setLooping(true);
		musicBall.play();
	}

    // Отрисовка
	@Override
	public void render (float delta) {

        switch(gameState) {
            case RUN:
                world.update(delta);
                renderer.render(gameState);
                break;
            case PAUSE:
                renderer.render(gameState);
        }
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
