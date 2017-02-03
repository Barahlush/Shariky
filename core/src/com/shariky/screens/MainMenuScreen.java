package com.shariky.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shariky.objects.Ball;
import com.shariky.objects.Button;

import java.util.Iterator;

import static com.shariky.objects.Ball.Colors.blue;
import static com.shariky.objects.Ball.Colors.gray;
import static com.shariky.screens.Shariky.height_ratio;
import static com.shariky.screens.Shariky.width_ratio;

/**
 * Created by User on 19.11.2016.
 */

public class MainMenuScreen implements Screen {

    final Shariky game;
    OrthographicCamera camera;
    Vector3 touchPos;
    ShapeRenderer shapeRenderer;
    boolean isBtnPresd, isBtnPresdOccur, clicked, sound_pressed;
    Sound click;
    Button playBtn, soundBtn, blue_ball, grayBall;
    private Array<Ball> balls;

    long spawnTime, lastBallTime;
    static long startSpawnTime;
    public enum MenuState {
        MAIN_MENU,
        GAME_TYPE_MENU
    }
    MenuState state;

    public MainMenuScreen(Shariky gam) {
        this.game = gam;


        isBtnPresd = false;
        clicked = false;
        isBtnPresdOccur = false;
        sound_pressed = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);
        shapeRenderer = new ShapeRenderer();
        touchPos = new Vector3();

        state = MenuState.MAIN_MENU;

        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        playBtn = new com.shariky.objects.Button(game.loader.buttons.get("play"), 180, 400);
        soundBtn = new com.shariky.objects.Button(game.loader.buttons.get("sound_on"), 6, 750, 50, 50);
        blue_ball = new com.shariky.objects.Button(game.loader.balls.get(blue), 100, 450);
        grayBall = new com.shariky.objects.Button(game.loader.balls.get(gray), 310, 450);

        startSpawnTime = 200000000L;
        spawnTime = startSpawnTime;
        balls = new Array<Ball>();
        spawnBall();

        if (game.sound_ON)
            game.loader.musicBall.play();
        else
            soundBtn.setButtonTexture(game.loader.buttons.get("sound_off"));
    }

    public void spawnBall() {
        Ball ball = new Ball(
                Ball.ballColorMix(),
                MathUtils.random(0, 440),
                800,
                (int) (250)
        );
        balls.add(ball);
        lastBallTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    // Drawing
    @Override
    public void render(float delta) {

        if (TimeUtils.nanoTime() - lastBallTime > spawnTime) spawnBall();
        Iterator<Ball> iter = balls.iterator();
        while (iter.hasNext()) {
            Ball ball = iter.next();
            ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));

            // Проваливание шарика вниз

            if (ball.getY() + ball.getWidth() < 0) {
                iter.remove();
            }
        }

            Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        for (Ball ball : balls) {
            game.batch.draw(game.loader.balls.get(ball.getColor()),
                    ball.getX(),
                    ball.getY(),
                    ball.getWidth(),
                    ball.getHeight());
        }
        game.batch.end();

        if (state == MenuState.GAME_TYPE_MENU) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0.9f, 0.9f, 0.9f, 0.8f));
            shapeRenderer.rect(40 * width_ratio, 310 * height_ratio, 400 * width_ratio, 340 * height_ratio);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        game.batch.begin();
        soundBtn.draw(game.batch);
        if (state == MenuState.MAIN_MENU) {
            playBtn.draw(game.batch);
            game.font.draw(game.batch, "SHARIKY 1.9", 150, 785);
        } else {
            grayBall.draw(game.batch);
            blue_ball.draw(game.batch);
            game.font.draw(game.batch, "CHOOSE YOUR GAME", 75, 630);
            game.font.draw(game.batch, "CLASSIC", 80, 420);
            game.font.draw(game.batch, "COMING", 280, 425);
            game.font.draw(game.batch, "SOON", 295, 395);
        }
        game.batch.end();

        // Нажатие на кнопку

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            //SOUND
            if (soundBtn.clickCheck(touchPos.x, touchPos.y)) {
                if (!sound_pressed) {
                    if (game.sound_ON) {
                        soundBtn.setButtonTexture(game.loader.buttons.get("sound_off"));
                        game.sound_ON = false;
                        game.loader.musicBall.pause();
                    } else {
                        soundBtn.setButtonTexture(game.loader.buttons.get("sound_on"));
                        game.sound_ON = true;
                        game.loader.musicBall.play();
                    }
                    sound_pressed = true;
                }
            } else {
                sound_pressed = false;
            }

            if (playBtn.clickCheck(touchPos.x, touchPos.y) && state == MenuState.MAIN_MENU) {
                isBtnPresd = true;
                if (!clicked) {
                    if (game.sound_ON)
                        click.play();
                    clicked = true;
                }
            } else {
                isBtnPresd = false;
            }
            if (state == MenuState.GAME_TYPE_MENU) {
                if (blue_ball.clickCheck(touchPos.x, touchPos.y)) {
                    game.setScreen(new GameScreen(game));
                    if (game.sound_ON)
                        click.play();
                    dispose();

                }
            }
        } else {
            sound_pressed = false;
        }
        // PLAY
        if (!Gdx.input.isTouched() && isBtnPresd) {
            state = MenuState.GAME_TYPE_MENU;
            isBtnPresd = false;
        }

        touchPos.set(0, 0, 0);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.loader.musicBall.pause();
    }

    @Override
    public void resume() {
        if (game.sound_ON)
            game.loader.musicBall.play();
    }

    @Override
    public void hide() {
    game.loader.musicBall.pause();
    }

    @Override
    public void dispose() {
        click.dispose();
    }
}
