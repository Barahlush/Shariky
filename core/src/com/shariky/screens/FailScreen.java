package com.shariky.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.shariky.objects.Button;

/**
 * Created by User on 20.11.2016.
 */

public class FailScreen implements Screen {

    final Shariky game;
    OrthographicCamera camera;
    Vector3 touchPos;
    boolean isBtnPresd, isRecord, clicked;
    Button repeat;

    public FailScreen(Shariky gam) {
        this.game = gam;

        isBtnPresd = false;
        clicked = false;

        if (game.score > game.record)
            isRecord = true;
        else
            isRecord = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);
        touchPos = new Vector3();
        repeat = new com.shariky.objects.Button(game.loader.repeat, 170, 330, 150, 150);

        if (game.score > game.record)
            game.record = game.score;
    }


    @Override
    public void show() {

    }

    // Отрисовка

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Your score: " + game.score, 150, 750);
        if (isRecord)
            game.font.draw(game.batch, "NEW RECORD!", 150, 650);
        repeat.draw(game.batch);
        game.batch.end();

    // Нажатие на кнопку

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (repeat.clickCheck(touchPos.x, touchPos.y)) {
                isBtnPresd = true;
                if (!clicked) {
                    if (game.sound_ON)
                        game.loader.click.play();
                    clicked = true;
                }
            }
        }
        if (isBtnPresd && !Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            isBtnPresd = false;
            dispose();
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

    }

    @Override
    public void dispose() {
    }
}
