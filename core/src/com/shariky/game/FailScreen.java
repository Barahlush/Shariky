package com.shariky.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by User on 20.11.2016.
 */

public class FailScreen implements Screen {

    final Shariky game;
    OrthographicCamera camera;
    Vector3 touchPos;
    boolean isBtnPresd, isRecord, clicked;
    Texture repeatBtn, repeatBtnPrsd, bg;
    Sound click;

    public FailScreen(Shariky gam) {
        this.game = gam;

        isBtnPresd = false;
        clicked = false;

        if (game.score > game.record)
            isRecord = true;
        else
            isRecord = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        touchPos = new Vector3();

        repeatBtn = new Texture("repeat.png");
        repeatBtnPrsd = new Texture("repeatpressed.png");
        bg = new Texture("bgfailpic.png");

        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));

        if (game.score > game.record)
            game.record = game.score;
    }


    @Override
    public void show() {

    }

    // Отрисовка

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.7f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.font.draw(game.batch, "Failed :(", 200, 710);
        game.font.draw(game.batch, "Your score: " + game.score, 200, 690);
        if (isRecord)
            game.font.draw(game.batch, "NEW RECORD!", 200, 670);
        if (!isBtnPresd)
            game.batch.draw(repeatBtn, 171, 336);
        else {
            game.batch.draw(repeatBtnPrsd, 171, 336);
        }
        game.batch.end();

    // Нажатие на кнопку

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.x > 171 && touchPos.x < 299 && touchPos.y < 464 && touchPos.y > 336) {
                isBtnPresd = true;
                if (!clicked) {
                    click.play();
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
        repeatBtn.dispose();
        repeatBtnPrsd.dispose();
        click.dispose();
        bg.dispose();
    }
}
