package com.shariky.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.shariky.objects.Button;

/**
 * Created by User on 19.11.2016.
 */

public class MainMenuScreen implements Screen {

    final Shariky game;
    OrthographicCamera camera;
    Vector3 touchPos;
    boolean isBtnPresd, isBtnPresdOccur, clicked, sound_pressed;
    Sound click;
    Button playBtn, soundBtn;

    public MainMenuScreen(Shariky gam) {
        this.game = gam;

        isBtnPresd = false;
        clicked = false;
        isBtnPresdOccur = false;
        sound_pressed = false;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);
        touchPos = new Vector3();

        game.record = 0;

        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        playBtn = new com.shariky.objects.Button(game.loader.playup, 180, 400);
        soundBtn = new com.shariky.objects.Button(game.loader.sound_on, 6, 760, 45, 45);
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
        game.font.draw(game.batch, "SHARIKY 1.6", 130, 795);
        soundBtn.draw(game.batch);
        playBtn.draw(game.batch);
        game.batch.end();

        // Нажатие на кнопку

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (soundBtn.clickCheck(touchPos.x, touchPos.y)) {
                if (!sound_pressed) {
                    if (game.sound_ON) {
                        soundBtn.setButtonTexture(game.loader.sound_off);
                        game.sound_ON = false;
                    } else {
                        soundBtn.setButtonTexture(game.loader.sound_on);
                        game.sound_ON = true;
                    }
                    sound_pressed = true;
                }
            } else {
                sound_pressed = false;
            }

            if (playBtn.clickCheck(touchPos.x, touchPos.y)) {
                isBtnPresd = true;
                if (!clicked) {
                    if (game.sound_ON)
                        click.play();
                    clicked = true;
                }
            } else {
                isBtnPresd = false;
            }
        } else {
            sound_pressed = false;
        }
        touchPos.set(0, 0, 0);
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
        click.dispose();
    }
}
