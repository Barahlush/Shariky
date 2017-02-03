package com.shariky.screens;

import com.badlogic.gdx.Screen;
import com.shariky.gamefield.GameRenderer;
import com.shariky.gamefield.GameWorld;

import static com.shariky.gamefield.GameWorld.State.FAIL;
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
        if (world.getGoBack())
            game.setScreen(new MainMenuScreen(game));
        renderer.render(world.game_state);
	}

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        if(world.game_state == RUN && world.tap_to_begin)
            world.game_state = PAUSE;
        musicBall.pause();
    }

    @Override
    public void resume() {
        if (world.game_state == RUN && world.tap_to_begin) {
            world.game_state = PAUSE;
        }
        if (world.game_state != PAUSE && world.game_state != FAIL && game.sound_ON)
            musicBall.play();
    }

    @Override
    public void hide() {
        if (world.game_state == RUN) {
            world = new GameWorld(this.game);
        }
        musicBall.pause();
    }

    // Очистка от мусора после закрытия
    @Override
	public void dispose () {
        musicBall.dispose();
	}

    @Override
    public void show() {
        world.game_state = RUN;
        if (game.sound_ON)
            musicBall.play();
    }
}
