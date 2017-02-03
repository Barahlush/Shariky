package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import static com.shariky.objects.Ball.ballMix;


/**
 * Created by User on 17.12.2016.
 */

public class GameWorld {

    final Shariky game;

    public enum State {
        RUN,
        PAUSE,
        FAIL
    }
    public State game_state;

    private Vector3 touch_pos;

    static final int ball_size = 60;
    private boolean ball_removed, sound_pressed;
    public boolean tap_to_begin;
    int repeat_is_out;

    public Array<Ball> balls;
    int lives;

    long spawn_time, last_ball_time, last_kill_ball_time, time;
    static long startSpawnTime;
    OrthographicCamera camera;

    public com.shariky.objects.Button pause_btn, play_btn, sound_btn;
    boolean is_btn_presd, is_record, clicked;
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

        game_state = RUN;

        touch_pos = new Vector3();

        balls = new Array<Ball>();

        startSpawnTime = 1500000000L;
        last_kill_ball_time = 3000000000L;
        spawn_time = startSpawnTime;
        time = TimeUtils.nanoTime();

        last_ball_time = TimeUtils.nanoTime() + 10000000L;
        ball_removed = false;
        sound_pressed = false;
        is_record = false;
        tap_to_begin = false;

        repeat_is_out = 0;


        if (game.sound_ON)
            sound_btn = new com.shariky.objects.Button(game.loader.buttons.get("sound_on"), 6, 760, 45, 45);
        else
            sound_btn = new com.shariky.objects.Button(game.loader.buttons.get("sound_off"), 6, 760, 45, 45);

        play_btn = new com.shariky.objects.Button(game.loader.buttons.get("play"), 190, 400);
        pause_btn = new com.shariky.objects.Button(game.loader.buttons.get("pause"), 445, 758, 35, 35);
        repeat = new com.shariky.objects.Button(game.loader.buttons.get("repeat"), 170, 900, 150, 150);
        repeat.setSpeed(2780);

        is_btn_presd = false;
        clicked = false;

        if (game.sound_ON)
            musicBall.play();


        game.score = 0;
        lives = 6;
    }

    // Find color by Y-coord

    private String fieldColor(int y) {
        if (y < 250) return "grey";
        else if (y < 500) return "blue";
        else return "green";
    }

    // Balls generator

    public void spawnBall() {
        Ball ball = new Ball(
                ballMix(),
                MathUtils.random(40, 440 - ball_size),
                800,
                (int) (90 + game.score / 5)
        );
        balls.add(ball);
        last_ball_time = TimeUtils.nanoTime();
    }


    public Array<Ball> getBalls() {
        return balls;
    }

    public void update(float delta) {

        // Touch actions

        if (Gdx.input.isTouched()) {
            touch_pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch_pos);
        } else
            ball_removed = false;



        // Game statements

        switch (game_state){

            case PAUSE:

                // Play button

                if (play_btn.clickCheck(touch_pos.x, touch_pos.y)) {
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
                        && !sound_btn.clickCheck(touch_pos.x, touch_pos.y)
                        && !pause_btn.clickCheck(touch_pos.x, touch_pos.y)
                        && TimeUtils.nanoTime() - time >= 1000000000L
                ) {
                    if (game.sound_ON)
                        click.play();
                    tap_to_begin = true;
                }
                 // Sound button

                if (sound_btn.clickCheck(touch_pos.x, touch_pos.y)) {
                    if (!sound_pressed) {
                        if (game.sound_ON) {
                            sound_btn.setButtonTexture(game.loader.buttons.get("sound_off"));
                            game.sound_ON = false;
                            musicBall.pause();
                        } else {
                            sound_btn.setButtonTexture(game.loader.buttons.get("sound_on"));
                            game.sound_ON = true;
                            musicBall.play();
                        }
                        sound_pressed = true;
                    }
                } else {
                    sound_pressed = false;
                }

            // Pause button

            if (pause_btn.clickCheck(touch_pos.x, touch_pos.y)) {
                game_state = PAUSE;

                if (game.sound_ON)
                    game.loader.click.play();
                musicBall.pause();
            }

        // Game model
            if (!tap_to_begin) {
                touch_pos.set(0, 0, 0);
                break;
            }
                // Balls generation on time

                if (TimeUtils.nanoTime() - last_ball_time > spawn_time) spawnBall();

                // Balls moving

                Iterator<Ball> iter = balls.iterator();
                boolean kill_ball_clicked = false;
                while (iter.hasNext()) {
                    Ball ball = iter.next();
                    ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));
                    if (ball.getY() == 0)
                        ball.setSpeed(200);

                    // Balls falling under the screen

                    if (ball.getY() + ball_size < 0) {
                        lives -= 2;
                        iter.remove();
                    }
                    if (!ball_removed) {

                        // Ball clicked

                        if (ball.clickCheck(touch_pos.x, touch_pos.y)) {
                            if (fieldColor((int) touch_pos.y) == ball.getColor() /*||
                                    (fieldColor((int) touch_pos.y) == "green"
                                            && ball.getColor() == bl_killBall )*/) {
                                game.score += 10;
                                spawn_time = startSpawnTime - game.score * 1000000;
                              /*  if (ball.getColor() == bl_killBall) {
                                    game.score += 20;
                                    last_score += 30;
                                    kill_ball_clicked = true;
                                }  */
                            } else {
                                lives -= 1;
                            }
                            if (game.sound_ON)
                                game.loader.click.play();
                            ball_removed = true;
                            iter.remove();
                        }
                    }
                }
                // Kill ball model

              /*if (kill_ball_clicked) {
                    Iterator<Ball> iterat = balls.iterator();
                    while (iterat.hasNext()) {
                        Ball ball = iterat.next();
                        if (ball.getColor() == "blue" || ball.getColor() == bl_killBall) {
                            game.score += 10;
                            if (ball.getColor() == bl_killBall)
                                game.score += 20;
                            iterat.remove();
                        }

                    }
                    kill_ball_clicked = false;
                } */

                touch_pos.set(0, 0, 0);

                // Failing

                if (lives <= 0) {
                    game_state = FAIL;
                    musicBall.pause();
                }

            break;

            case FAIL:

                // Failing animation

                Iterator<Ball> iterat = balls.iterator();
                while (iterat.hasNext()) {
                    Ball ball = iterat.next();
                    ball.setSpeed(ball.getSpeed() + 20);
                    ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));

                    // Balls fall under the screen

                    if (ball.getY() + ball_size < 0) {
                        iterat.remove();
                    }
                }

                if (balls.size == 0) {
                    balls.clear();
                    if (game.score > game.record) {
                        game.record = game.score;
                        is_record = true;
                    }
                    if (repeat.getY() >= 360 && repeat_is_out == 0) {
                        repeat.setY(repeat.getY() - repeat.getSpeed() * Gdx.graphics.getDeltaTime());
                        repeat.setSpeed(repeat.getSpeed() - 120);
                    } else {
                        repeat_is_out = 1;
                    }
                    if (repeat.getY() < 400 && repeat_is_out == 1) {
                        repeat.setY(repeat.getY() - repeat.getSpeed() * Gdx.graphics.getDeltaTime());
                        repeat.setSpeed(repeat.getSpeed() - 120);
                    } else if (repeat_is_out != 0){
                        repeat_is_out = 2;
                        if (Gdx.input.isTouched()) {
                            touch_pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                            camera.unproject(touch_pos);
                            if (repeat.clickCheck(touch_pos.x, touch_pos.y)) {
                                is_btn_presd = true;
                                if (game.sound_ON)
                                    game.loader.click.play();
                                if (!clicked) {
                                    clicked = true;
                                }
                            }
                        }
                        if (is_btn_presd && !Gdx.input.isTouched()) {

                            // New game initialisation

                            game_state = RUN;
                            startSpawnTime = 1500000000L;
                            last_kill_ball_time = 3000000000L;
                            spawn_time = startSpawnTime;
                            repeat.setSpeed(2780);
                            balls = new Array<Ball>();
                            repeat_is_out = 0;
                            tap_to_begin = false;
                            ball_removed = false;
                            sound_pressed = false;
                            is_record = false;
                            is_btn_presd = false;
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
