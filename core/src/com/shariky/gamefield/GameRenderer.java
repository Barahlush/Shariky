package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shariky.objects.Ball;

/**
 * Created by User on 17.12.2016.
 */

public class GameRenderer {

    private GameWorld MyWorld;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    final com.shariky.screens.Shariky game;
    boolean darken = false;
    public GameRenderer(GameWorld world, final com.shariky.screens.Shariky gam) {
        this.game = gam;
        MyWorld = world;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);
        batch = game.batch;
        font = game.font;
    }



    public OrthographicCamera getCamera() {
        return camera;
    }

    public void render(GameWorld.State state) {

                Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
                Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

                batch.setProjectionMatrix(camera.combined);

                batch.begin();

                for (Ball ball : MyWorld.getBalls()) {
                    batch.draw(game.loader.balls.get(ball.getColor()),
                            ball.getX(),
                            ball.getY(),
                            ball.getWidth(),
                            ball.getHeight());
                }

        // Score
                if (state == GameWorld.State.RUN) {

                    if (game.score < 10)
                        font.draw(batch, "" + game.score, 460, 40);
                    else if (game.score < 100)
                        font.draw(batch, "" + game.score, 440, 40);
                    else if (game.score < 1000)
                        font.draw(batch, "" + game.score, 420, 40);
                    else if (game.score < 10000)
                        font.draw(batch, "" + game.score, 400, 40);
                    else if (game.score < 100000)
                        font.draw(batch, "" + game.score, 380, 40);

                    MyWorld.pause_btn.draw(batch);
                    MyWorld.sound_btn.draw(batch);


                // Tap to begin label ****** Better to do as a asset

                    if (!MyWorld.tap_to_begin) {
                        batch.end();

                    // Gray rectangle

                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        MyWorld.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        MyWorld.shapeRenderer.setColor(new Color(0.9f, 0.9f, 0.9f, 0.7f));
                        MyWorld.shapeRenderer.rect(
                                137 * game.width_ratio,
                                600 * game.height_ratio,
                                215 * game.width_ratio,
                                68 * game.height_ratio
                        );
                        MyWorld.shapeRenderer.end();
                        Gdx.gl.glDisable(GL20.GL_BLEND);

                        batch.begin();

                        font.draw(batch, "Tap to begin...", 150, 650);

                    }
                }

                batch.draw(game.loader.bg, 0, 0, game.width, game.height);

                if (MyWorld.lives - 1 >= 0)
                    batch.draw(game.loader.hp.get(MyWorld.lives - 1), 0, 0, game.width, game.height);

                batch.end();
    // Pause menu

        if (state == GameWorld.State.PAUSE) {

            // Gray rectangle
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            MyWorld.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            MyWorld.shapeRenderer.setColor(new Color(0.6f, 0.6f, 0.6f, 0.4f));
            MyWorld.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getBackBufferHeight());
            MyWorld.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();

            game.font.draw(game.batch, "Score: " + game.score, 182, 700);
            game.font.draw(game.batch, "P A U S E", 180, 765);
            MyWorld.play_btn.draw(batch);

            batch.end();

    // Fail menu

        } else if (state == GameWorld.State.FAIL) {

            if (MyWorld.balls.size != 0) {

                Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
                Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

                batch.setProjectionMatrix(camera.combined);
                batch.begin();

                for (Ball ball : MyWorld.getBalls()) {
                    batch.draw(game.loader.balls.get(ball.getColor()),
                            ball.getX(),
                            ball.getY(),
                            ball.getWidth(),
                            ball.getHeight());
                }
                batch.draw(game.loader.bg, 0, 0, game.width, game.height);

                batch.end();

            } else {

            // Gray rectangle
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                MyWorld.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                MyWorld.shapeRenderer.setColor(new Color(0.9f, 0.9f, 0.9f, 0.6f));
                MyWorld.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getBackBufferHeight());
                MyWorld.shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                batch.begin();

                MyWorld.repeat.draw(batch);

                if (MyWorld.repeat_is_out == 2) {
                    game.font.draw(game.batch, "Your score: " + game.score, 145, 750);
                    if (MyWorld.is_record)
                        game.font.draw(game.batch, "NEW RECORD!", 140, 680);
                }

                batch.end();
            }
        }
    }
}
