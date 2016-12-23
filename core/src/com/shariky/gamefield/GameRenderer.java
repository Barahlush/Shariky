package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shariky.objects.Ball;
import com.shariky.screens.GameScreen;

import static com.shariky.helpers.AssetLoader.basicBatch;
import static com.shariky.helpers.AssetLoader.basicFont;

/**
 * Created by User on 17.12.2016.
 */

public class GameRenderer {

    private GameWorld MyWorld;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    final com.shariky.screens.Shariky game;

    public GameRenderer(GameWorld world, final com.shariky.screens.Shariky gam) {
        this.game = gam;
        MyWorld = world;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.gameWidth, game.gameHeight);
        batch = basicBatch;
        font = basicFont;
    }


    public OrthographicCamera getCamera() {
        return camera;
    }
    public void render(GameScreen.State state) {
        switch (state) {
            case RUN:
                Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
                Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
                batch.setProjectionMatrix(camera.combined);

                batch.begin();

                batch.draw(MyWorld.getBg(), 0, -25);
                for (Ball ball : MyWorld.getBalls()) {
                    batch.draw(ball.getColor(), ball.getX(), ball.getY());
                }
                font.draw(batch, "Record: " + game.record, 0, 800);
                font.draw(batch, "Score: " + game.score, 190, 800);
                font.draw(batch, "Health: " + MyWorld.lives + "%", 190, 780);
                batch.end();
                break;
            case PAUSE:
                break;
            case STOPPED:
                break;
            case RESUME:
                break;
        }
    }
}
