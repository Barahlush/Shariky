package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shariky.objects.Ball;
import com.shariky.screens.Shariky;

import java.util.Iterator;

import static com.shariky.gamefield.GameWorld.State.PAUSE;
import static com.shariky.gamefield.GameWorld.State.RUN;
import static com.shariky.helpers.AssetLoader.musicBall;


/**
 * Created by User on 17.12.2016.
 */

public class GameWorld {

    final Shariky game;

    public enum State {
        RUN,
        PAUSE,
        RESUME,
        STOPPED
    }
    public State game_state;

    Vector3 touchPos;
    Rectangle touch;

    static final int ballSize = 60;
    boolean ballRemoved, darken, sound_pressed;
    Array<Ball> balls;

    int lives;

    private TextureRegion yBall, rBall, gBall;

    long spawnTime, lastBallTime;
    static long startSpawnTime;
    OrthographicCamera camera;

    public com.shariky.objects.Button pauseBtn, playBtn, soundBtn;

    public void setCamera(OrthographicCamera cam) {
        camera = cam;
    }
    ShapeRenderer shapeRenderer;

    public GameWorld(final Shariky gam) {
        game = gam;
        shapeRenderer = new ShapeRenderer();

        yBall = game.loader.yl_bl;
        rBall = game.loader.rd_gr;
        gBall = game.loader.grn;
        game_state = RUN;

        touchPos = new Vector3();
        touch = new Rectangle();

        startSpawnTime = 1500000000L;
        spawnTime = startSpawnTime;

        balls = new Array<Ball>();
        spawnBall();
        ballRemoved = false;
        sound_pressed = false;

        darken = false;
        pauseBtn = new com.shariky.objects.Button(game.loader.pause, 445, 758, 35, 35);
        if (game.sound_ON)
            soundBtn = new com.shariky.objects.Button(game.loader.sound_on, 6, 760, 45, 45);
        else
            soundBtn = new com.shariky.objects.Button(game.loader.sound_off, 6, 760, 45, 45);
        playBtn = new com.shariky.objects.Button(game.loader.playup, 180, 400);


        if (game.sound_ON)
            musicBall.play();


        game.score = 0;
        lives = 6;
    }

    // Определение цвета поля по касанию
    private TextureRegion fieldColor(int y) {
        if (y < 250) return rBall;
        else if (y < 500) return yBall;
        else return gBall;
    }

    // Генератор шариков
    private void spawnBall() {
        Ball ball = new Ball(
                ballMix(),
                MathUtils.random(40, 440 - ballSize),
                800,
                (int) (60 + game.score / 5)
        );
        balls.add(ball);
        lastBallTime = TimeUtils.nanoTime();
    }

    // Рандомизация цвета для генератора

    private TextureRegion ballMix() {
        int color = MathUtils.random(0, 2);
        switch (color) {
            case 0:
                return rBall;
            case 1:
                return gBall;
            case 2:
                return yBall;
        }
        return ballMix();
    }

    public Array<Ball> getBalls() {
        return balls;
    }

    public void update(float delta) {

        // Действия при касании
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        } else
            ballRemoved = false;


        if(game_state != PAUSE) {
            // SOUND BTN
                if (soundBtn.clickCheck(touchPos.x, touchPos.y)) {
                    if (!sound_pressed) {
                        if (game.sound_ON) {
                            soundBtn.setButtonTexture(game.loader.sound_off);
                            game.sound_ON = false;
                            musicBall.pause();
                        } else {
                            soundBtn.setButtonTexture(game.loader.sound_on);
                            game.sound_ON = true;
                            musicBall.play();
                        }
                        sound_pressed = true;
                    }
                } else {
                    sound_pressed = false;
                }
            // PAUSE BTN
            if (pauseBtn.clickCheck(touchPos.x, touchPos.y)) {
                game_state = PAUSE;

                if (game.sound_ON)
                    game.loader.click.play();
                musicBall.pause();
            }
        } else
        // PLAY BTN
        if (playBtn.clickCheck(touchPos.x, touchPos.y)) {
            game_state = RUN;
            darken = false;
            if (game.sound_ON) {
                game.loader.click.play();
                musicBall.play();
            }
        }

        // MODEL
        if (game_state == RUN) {

            // Генерирование шариков по времени
            if (TimeUtils.nanoTime() - lastBallTime > spawnTime) spawnBall();

            // Изменение модели

            Iterator<Ball> iter = balls.iterator();
            while (iter.hasNext()) {
                Ball ball = iter.next();
                ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));
                if (ball.getY() == 0)
                    ball.setSpeed(200);

                // Проваливание шарика вниз
                if (ball.getY() + ballSize < 0) {
                    lives -= 2;
                    iter.remove();
                }
                if (!ballRemoved) {

                    // Попадание в шарик
                    if (ball.clickCheck(touchPos.x, touchPos.y)) {
                        if (fieldColor((int) touchPos.y) == ball.getColor()) {
                            game.score += 10;
                            spawnTime = startSpawnTime - game.score * 1000000;
                        } else {
                            lives -= 1;
                        }
                        if (game.sound_ON)
                            game.loader.click.play();
                        ballRemoved = true;
                        iter.remove();
                    }
                }
            }

            touchPos.set(0, 0, 0);

            // Конец жизней
            if (lives <= 0) {
                game.setScreen(new com.shariky.screens.FailScreen(game));
            }
        }
    }
}
