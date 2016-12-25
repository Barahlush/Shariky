package com.shariky.screens;

import com.badlogic.gdx.Screen;
import com.shariky.gamefield.GameRenderer;
import com.shariky.gamefield.GameWorld;

import static com.shariky.gamefield.GameWorld.State.PAUSE;
import static com.shariky.gamefield.GameWorld.State.RUN;
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

	}

    // Отрисовка
	@Override
	public void render (float delta) {
        world.update(delta);
        renderer.render(world.game_state);
	}

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        world.game_state = PAUSE;
    }

    @Override
    public void resume() {
        world.game_state = PAUSE;
        musicBall.pause();
    }

    @Override
    public void hide() {
        world = new GameWorld(this.game);
        musicBall.stop();
    }

    // Очистка от мусора после закрытия
    @Override
	public void dispose () {
        musicBall.dispose();
	}

    @Override
    public void show() {
        world.game_state = RUN;
    }
}
