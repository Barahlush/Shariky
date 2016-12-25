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
                    batch.draw(ball.getColor(), ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
                }

                if (game.score < 10)
                    font.draw(batch, "" + game.score, 460, 45);
                else if (game.score < 100)
                    font.draw(batch, "" + game.score, 440, 45);
                else if (game.score < 1000)
                    font.draw(batch, "" + game.score, 420, 45);
                else if (game.score < 10000)
                    font.draw(batch, "" + game.score, 400, 45);
                else if (game.score < 100000)
                    font.draw(batch, "" + game.score, 380, 45);
                if (state != GameWorld.State.PAUSE) {
                    MyWorld.pauseBtn.draw(batch);
                    MyWorld.soundBtn.draw(batch);
                }
                batch.draw(game.loader.bg, 0, 0, game.width, game.height);
                if (MyWorld.lives - 1 >= 0)
                    batch.draw(game.loader.hp.get(MyWorld.lives - 1), 0, 0, game.width, game.height);
                batch.end();

        if (state == GameWorld.State.PAUSE) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            MyWorld.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            MyWorld.shapeRenderer.setColor(new Color(0, 0, 0, 0.6f));
            MyWorld.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getBackBufferHeight());
            MyWorld.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
            MyWorld.playBtn.draw(batch);
            batch.end();
        }
    }
}
