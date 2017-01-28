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
import com.shariky.objects.Button;
import com.shariky.screens.Shariky;

import java.util.Iterator;

import static com.shariky.gamefield.GameWorld.State.FAIL;
import static com.shariky.gamefield.GameWorld.State.PAUSE;
import static com.shariky.gamefield.GameWorld.State.RUN;
import static com.shariky.helpers.AssetLoader.click;
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
        STOPPED,
        FAIL
    }
    public State game_state;

    Vector3 touchPos;
    Rectangle touch;



    static final int ballSize = 60;
    boolean ballRemoved, sound_pressed;
    public boolean tap_to_begin;
    int repeatIsOut;
    public Array<Ball> balls;

    int lives;

    private TextureRegion yBall, rBall, gBall, bl_killBall;

    long spawnTime, lastBallTime, lastKillBallTime, time;
    static long startSpawnTime;
    OrthographicCamera camera;

    public com.shariky.objects.Button pauseBtn, playBtn, soundBtn;
    boolean isBtnPresd, isRecord, clicked;
    Button repeat;

    int last_score;

    public void setCamera(OrthographicCamera cam) {
        camera = cam;
    }
    ShapeRenderer shapeRenderer;

    public GameWorld(final Shariky gam) {
        game = gam;
        if (game.sound_ON)
            game.loader.click.play();
        shapeRenderer = new ShapeRenderer();

        yBall = game.loader.yl_bl;
        rBall = game.loader.rd_gr;
        gBall = game.loader.grn;
        bl_killBall = game.loader.bl_kill;
        game_state = RUN;

        touchPos = new Vector3();
        touch = new Rectangle();

        startSpawnTime = 1500000000L;
        lastKillBallTime = 3000000000L;
        spawnTime = startSpawnTime;
        time = TimeUtils.nanoTime();


        balls = new Array<Ball>();
        lastBallTime = TimeUtils.nanoTime() + 10000000L;
        ballRemoved = false;
        sound_pressed = false;
        isRecord = false;
        tap_to_begin = false;

        repeatIsOut = 0;


        pauseBtn = new com.shariky.objects.Button(game.loader.pause, 445, 758, 35, 35);

        if (game.sound_ON)
            soundBtn = new com.shariky.objects.Button(game.loader.sound_on, 6, 760, 45, 45);
        else
            soundBtn = new com.shariky.objects.Button(game.loader.sound_off, 6, 760, 45, 45);

        playBtn = new com.shariky.objects.Button(game.loader.playup, 190, 400);

        repeat = new com.shariky.objects.Button(game.loader.repeat, 170, 900, 150, 150);
        repeat.setSpeed(2780);

        isBtnPresd = false;
        clicked = false;

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
    public void spawnBall() {
        Ball ball = new Ball(
                ballMix(),
                MathUtils.random(40, 440 - ballSize),
                800,
                (int) (90 + game.score / 5)
        );
        balls.add(ball);
        lastBallTime = TimeUtils.nanoTime();
    }

    // Рандомизация цвета для генератора

    public TextureRegion ballMix() {
        int color = MathUtils.random(0, 3);
        switch (color) {
            case 0:
                return rBall;
            case 1:
                return gBall;
            case 2:
                return yBall;
            case 3:
                if (game.score - last_score > 100) {
                    last_score = game.score;
                    return bl_killBall;
                }
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


        switch (game_state){

            case PAUSE:

                // PLAY BTN
                if (playBtn.clickCheck(touchPos.x, touchPos.y)) {
                    game_state = RUN;
                    time = TimeUtils.nanoTime();
                    if (game.sound_ON) {
                        game.loader.click.play();
                        musicBall.play();
                    }
                }
                break;

            case RUN:
                if (Gdx.input.isTouched()
                        &&!tap_to_begin
                        && !soundBtn.clickCheck(touchPos.x, touchPos.y)
                        && !pauseBtn.clickCheck(touchPos.x, touchPos.y)
                        && TimeUtils.nanoTime() - time >= 1500000000L
                ) {
                    if (game.sound_ON)
                        click.play();
                    tap_to_begin = true;
                }
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

        // MODEL
            if (!tap_to_begin) {
                touchPos.set(0, 0, 0);
                break;
            }
                // Генерирование шариков по времени
                if (TimeUtils.nanoTime() - lastBallTime > spawnTime) spawnBall();

                // Передвижение шариков

                Iterator<Ball> iter = balls.iterator();
                boolean kill_ball_clicked = false;
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
                            if (fieldColor((int) touchPos.y) == ball.getColor() ||
                                    (fieldColor((int) touchPos.y) == yBall
                                            && ball.getColor() == bl_killBall)) {
                                game.score += 10;
                                spawnTime = startSpawnTime - game.score * 1000000;
                                if (ball.getColor() == bl_killBall) {
                                    game.score += 20;
                                    last_score += 30;
                                    kill_ball_clicked = true;
                                }
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
                // Шарик-убийца

                if (kill_ball_clicked) {
                    Iterator<Ball> iterat = balls.iterator();
                    while (iterat.hasNext()) {
                        Ball ball = iterat.next();
                        if (ball.getColor() == yBall || ball.getColor() == bl_killBall) {
                            game.score += 10;
                            if (ball.getColor() == bl_killBall)
                                game.score += 20;
                            iterat.remove();
                        }

                    }
                    kill_ball_clicked = false;
                }

                touchPos.set(0, 0, 0);

                // Конец жизней
                if (lives <= 0) {
                    game_state = FAIL;
                    musicBall.pause();
                }

            break;

            case FAIL:
                Iterator<Ball> iterat = balls.iterator();
                while (iterat.hasNext()) {
                    Ball ball = iterat.next();
                    ball.setSpeed(ball.getSpeed() + 20);
                    ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));
                    // Проваливание шарика вниз
                    if (ball.getY() + ballSize < 0) {
                        iterat.remove();
                    }
                }
                if (balls.size == 0) {
                    balls.clear();
                    if (game.score > game.record) {
                        game.record = game.score;
                        isRecord = true;
                    }
                    if (repeat.getY() >= 360 && repeatIsOut == 0) {
                        repeat.setY(repeat.getY() - repeat.getSpeed() * Gdx.graphics.getDeltaTime());
                        repeat.setSpeed(repeat.getSpeed() - 120);
                    } else {
                        repeatIsOut = 1;
                    }
                    if (repeat.getY() < 400 && repeatIsOut == 1) {
                        repeat.setY(repeat.getY() - repeat.getSpeed() * Gdx.graphics.getDeltaTime());
                        repeat.setSpeed(repeat.getSpeed() - 120);
                    } else if (repeatIsOut != 0){
                        repeatIsOut = 2;
                        if (Gdx.input.isTouched()) {
                            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                            camera.unproject(touchPos);
                            if (repeat.clickCheck(touchPos.x, touchPos.y)) {
                                isBtnPresd = true;
                                if (game.sound_ON)
                                    game.loader.click.play();
                                if (!clicked) {
                                    clicked = true;
                                }
                            }
                        }
                        if (isBtnPresd && !Gdx.input.isTouched()) {
                            // NEW GAME
                            game_state = RUN;
                            startSpawnTime = 1500000000L;
                            lastKillBallTime = 3000000000L;
                            spawnTime = startSpawnTime;
                            repeat.setSpeed(2780);
                            balls = new Array<Ball>();
                            repeatIsOut = 0;
                            tap_to_begin = false;
                            ballRemoved = false;
                            sound_pressed = false;
                            isRecord = false;
                            isBtnPresd = false;
                            repeat.setX(180);
                            repeat.setY(900);
                            game.score = 0;
                            lives = 6;
                            if (game.sound_ON)
                                musicBall.play();
                        }
                    }
                }
        }
    }
}
