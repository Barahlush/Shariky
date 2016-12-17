package com.shariky.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by User on 19.11.2016.
 */

public class MainMenuScreen implements Screen {

    final Shariky game;
    OrthographicCamera camera;
    Vector3 touchPos;
    boolean isBtnPresd, isBtnPresdOccur, clicked;
    Texture newGameBtn, newGameBtnPrsd;
    Sound click;

    public MainMenuScreen(Shariky gam) {
        this.game = gam;

        isBtnPresd = false;
        clicked = false;
        isBtnPresdOccur = false;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        touchPos = new Vector3();

        game.record = 0;

        newGameBtn = new Texture("ngbtn.png");
        newGameBtnPrsd = new Texture("ngbtnpressed.png");
        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
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
        game.font.draw(game.batch, "SHARIKY 0.1", 200, 795);
        if (!isBtnPresd)
            game.batch.draw(newGameBtn, 132, 400);
        else {
            game.batch.draw(newGameBtnPrsd, 132, 400);
        }
        game.batch.end();

        // Нажатие на кнопку

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.x > 132 && touchPos.x < 358 && touchPos.y < 480 && touchPos.y > 400) {
                isBtnPresd = true;
                if (!clicked) {
                    click.play();
                    clicked = true;
                }
            } else
                isBtnPresd = false;
        }
        if (!Gdx.input.isTouched() && isBtnPresd) {
            game.setScreen(new GameScreen(game));
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
        newGameBtn.dispose();
        newGameBtnPrsd.dispose();
        click.dispose();
    }
}
