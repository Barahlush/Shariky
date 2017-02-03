package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shariky.objects.Ball;
import com.shariky.objects.BonusBalls.BallKill;
import com.shariky.objects.BonusBalls.BallSlow;
import com.shariky.objects.BonusBalls.DeathBall;
import com.shariky.objects.BonusBalls.HealthBall;
import com.shariky.objects.BonusBalls.MulticolorBall;
import com.shariky.objects.Button;
import com.shariky.screens.Shariky;

import java.util.Iterator;

import static com.shariky.gamefield.GameWorld.State.FAIL;
import static com.shariky.gamefield.GameWorld.State.PAUSE;
import static com.shariky.gamefield.GameWorld.State.RUN;
import static com.shariky.helpers.AssetLoader.click;
import static com.shariky.helpers.AssetLoader.musicBall;
import static com.shariky.objects.Ball.Colors.blue;
import static com.shariky.objects.Ball.Colors.gray;
import static com.shariky.objects.Ball.Colors.green;
import static com.shariky.objects.Ball.Types.death;
import static com.shariky.objects.Ball.Types.health;
import static com.shariky.objects.Ball.Types.multicolor;
import static com.shariky.objects.Ball.ballColorMix;
import static com.shariky.objects.Ball.ballTypeMix;


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
    private boolean go_back;
    public boolean tap_to_begin;
    int repeat_is_out;

    public Array<Ball> balls;
    int lives;

    long spawn_time, last_ball_time, last_kill_ball_time, time;
    static long startSpawnTime;
    OrthographicCamera camera;

    public com.shariky.objects.Button pause_btn, play_btn, sound_btn, menu_button;
    boolean is_btn_presd, is_record, clicked;
    Button repeat;

    BallSlow slow_ball;

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
        slow_ball = new BallSlow();

        startSpawnTime = 1500000000L;
        last_kill_ball_time = 3000000000L;
        spawn_time = startSpawnTime;
        time = TimeUtils.nanoTime();

        last_ball_time = TimeUtils.nanoTime() + 10000000L;
        ball_removed = false;
        sound_pressed = false;
        is_record = false;
        tap_to_begin = false;
        go_back = false;

        repeat_is_out = 0;


        if (game.sound_ON)
            sound_btn = new com.shariky.objects.Button(game.loader.buttons.get("sound_on"), 6, 760, 45, 45);
        else
            sound_btn = new com.shariky.objects.Button(game.loader.buttons.get("sound_off"), 6, 760, 45, 45);

        play_btn = new com.shariky.objects.Button(game.loader.buttons.get("play"), 190, 400);
        pause_btn = new com.shariky.objects.Button(game.loader.buttons.get("pause"), 445, 758, 35, 35);
        repeat = new com.shariky.objects.Button(game.loader.buttons.get("repeat"), 170, 900, 150, 150);
        menu_button = new com.shariky.objects.Button(game.loader.buttons.get("repeat"), 220, 200, 60, 60);
        repeat.setSpeed(2780);

        is_btn_presd = false;
        clicked = false;

        if (game.sound_ON)
            musicBall.play();


        game.score = 0;
        lives = 6;
    }

    // Find color by Y-coord

    private Ball.Colors fieldColor(int y) {
        if (y < 260) return gray;
        else if (y < 500) return blue;
        else return green;
    }

    // Balls generator

    public void spawnBall() {
        Ball.Types ball_type = ballTypeMix();
        Ball ball = new Ball(
                ballColorMix(),
                MathUtils.random(40, 440 - ball_size),
                800,
                (int) (100 + game.score / 20)
        );
        switch (ball_type) {
            case ball:
                balls.add(ball);
                break;
            case slow:
                BallSlow slow_ball = new BallSlow(ball);
                balls.add(slow_ball);
                break;
            case kill:
                BallKill kill_ball = new BallKill(ball);
                balls.add(kill_ball);
                break;
            case health:
                HealthBall health_ball = new HealthBall(ball);
                balls.add(health_ball);
                break;
            case death:
                DeathBall death_ball = new DeathBall(ball);
                balls.add(death_ball);
                break;
            case multicolor:
                MulticolorBall mult_ball = new MulticolorBall(ball);
                balls.add(mult_ball);
                break;
        }

        last_ball_time = TimeUtils.nanoTime();
    }

    public boolean getGoBack() {
        return go_back;
    }

    public Array<Ball> getBalls() {
        return balls;
    }

    private boolean is_missed(Ball ball) {
        if (ball.clickCheck(touch_pos.x, touch_pos.y)) {
            if (fieldColor((int) touch_pos.y) != ball.getColor()
                    && ball.getType() != health
                    && ball.getType() != death) {
                return true;
            }
        }
        return false;
    }

    private boolean good_color(float y, Ball ball){
        if (ball.getType() != multicolor)
            return (fieldColor((int) touch_pos.y) == ball.getColor()
                || ball.getType() == health
                || ball.getType() == death);
        else
            switch (ball.getColor()){
                case blue_gray:
                    return fieldColor((int) touch_pos.y) == blue
                            || fieldColor((int) touch_pos.y) == gray;
                case green_blue:
                    return fieldColor((int) touch_pos.y) == blue
                            || fieldColor((int) touch_pos.y) == green;
                case green_gray:
                    return fieldColor((int) touch_pos.y) == green ||
                            fieldColor((int) touch_pos.y) == gray;
                case tricolor:
                    return fieldColor((int) touch_pos.y) == green ||
                            fieldColor((int) touch_pos.y) == gray ||
                            fieldColor((int) touch_pos.y) == blue;
            }
        return false;
    }

    private boolean good_color(Ball.Colors field, Ball ball){
        if (ball.getType() != multicolor)
            return (field == ball.getColor());
        else
            switch (ball.getColor()){
                case blue_gray:
                    return field == blue
                            || field == gray;
                case green_blue:
                    return field == blue
                            || field == green;
                case green_gray:
                    return field == green ||
                            field == gray;
                case tricolor:
                    return field == green ||
                            field == gray ||
                            field == blue;
            }
        return false;
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


               // Menu button

                if (menu_button.clickCheck(touch_pos.x, touch_pos.y)) {
                    go_back = true;
                    if (game.sound_ON) {
                        game.loader.click.play();
                    }
                }
                break;

            case RUN:

                if (Gdx.input.isTouched()
                        &&!tap_to_begin
                        && !sound_btn.clickCheck(touch_pos.x, touch_pos.y)
                        && !pause_btn.clickCheck(touch_pos.x, touch_pos.y)
                        && TimeUtils.nanoTime() - time >= 500000000L
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
                BallSlow.updateSlowTime(TimeUtils.nanoTime());

                // Balls moving

                boolean is_killed = false;
                Iterator<Ball> iter = balls.iterator();
                while (iter.hasNext()) {
                    Ball ball = iter.next();
                    if (ball.isSlowed()) {
                        ball.setY((int) (ball.getY() - 0.2 * ball.getSpeed() * Gdx.graphics.getDeltaTime()));
                    } else {
                        ball.setY((int) (ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));
                    }
                    if (ball.getY() == 0)
                        ball.setSpeed(200);

                    // Balls falling under the screen

                    if (ball.getY() + ball_size < 0 && ball.getType() != death) {
                        lives -= 2;
                        iter.remove();
                    }
                    if (!ball_removed) {

                        // Ball clicked or killed

                        if (ball.clickCheck(touch_pos.x, touch_pos.y)
                                || BallKill.isKilled(ball.getColor()) ) {

                            boolean missed = false;
                            if (!good_color(touch_pos.y, ball)) {
                                lives -= 1;
                                missed = true;
                                touch_pos.set(0, 0, 0);
                            }

                            if (!missed)
                                switch (ball.getType()) {

                                    case ball:
                                        game.score += 10;
                                        break;

                                    case slow:
                                        game.score += 15;
                                        BallSlow.setSlowTime(
                                                BallSlow.getSlowTime().get(ball.getColor()) + TimeUtils.nanoTime(),
                                                ball.getColor()
                                        );
                                        break;

                                    case kill:
                                        game.score += 15;
                                        BallKill.setKilled(ball.getColor());
                                        break;

                                    case health:
                                        game.score += 20;
                                        lives += 1;
                                        break;

                                    case death:
                                        lives -= 6;
                                        break;

                                    case multicolor:
                                        game.score += 20;
                                }
                                spawn_time = startSpawnTime - game.score * 1000000;

                            if (game.sound_ON)
                                game.loader.click.play();

                            if (!BallKill.isKilled(ball.getColor()))
                                ball_removed = true;

                            if (ball.getType() != multicolor)
                                iter.remove();
                            else
                                ball.mult_simpl(fieldColor((int) touch_pos.y));
                        }
                    }
                }

              if (BallKill.isKilled()) {
                  iter = balls.iterator();
                  while (iter.hasNext()) {
                      Ball ball = iter.next();
                      if (good_color(BallKill.getKilledColor(), ball)) {
                          switch (ball.getType()) {

                              case ball:
                                  game.score += 10;
                                  break;

                              case slow:
                                  game.score += 15;
                                  BallSlow.setSlowTime(
                                          BallSlow.getSlowTime().get(ball.getColor()) + TimeUtils.nanoTime(),
                                          ball.getColor()
                                  );
                                  break;

                              case kill:
                                  game.score += 15;
                                  break;

                              case multicolor:
                                  game.score += 20;
                          }
                          if (ball.getType() != multicolor)
                              iter.remove();
                          else
                              ball.mult_simpl(BallKill.getKilledColor());
                        }
                  }
                  BallKill.resetKilled();
              }


                touch_pos.set(0, 0, 0);

                // Failing

                if (lives <= 0) {
                    game_state = FAIL;
                    musicBall.pause();
                } else if (lives > 6) {
                    lives = 6;
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
