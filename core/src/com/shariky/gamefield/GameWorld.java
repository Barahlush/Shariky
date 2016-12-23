package com.shariky.gamefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shariky.objects.Ball;
import com.shariky.screens.Shariky;

import java.util.Iterator;

import static com.shariky.helpers.AssetLoader.bgtexture;
import static com.shariky.helpers.AssetLoader.greenBall;
import static com.shariky.helpers.AssetLoader.redBall;
import static com.shariky.helpers.AssetLoader.yellowBall;


/**
 * Created by User on 17.12.2016.
 */

public class GameWorld {
    Vector3 touchPos;
    Rectangle touch;

    boolean ballRemoved;
    Array<Ball> balls;

    int lives;

    private Texture yBall, rBall, gBall, bg;
    private Sound cl;
    private Music mus;

    final Shariky game;

    long spawnTime, lastBallTime;
    static long startSpawnTime;
    OrthographicCamera camera;

    static final int ballSize = 60;

    public void setCamera(OrthographicCamera cam) {
        camera = cam;
    }

    public void assetInit() {
        yBall = yellowBall;
        rBall = redBall;
        gBall = greenBall;
        bg = bgtexture;
    }

    public GameWorld(final Shariky gam) {


        game = gam;

        assetInit();

        touchPos = new Vector3();
        touch = new Rectangle();

        startSpawnTime = 1500000000L;
        spawnTime = startSpawnTime;

        balls = new Array<Ball>();
        spawnBall();
        ballRemoved = false;


        game.score = 0;
        lives = 100;
    }

    // Определение цвета поля по касанию
    private Texture fieldColor(int y) {
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

    private Texture ballMix() {
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

    public Texture getBg() {
        return bg;
    }


    public void update(float delta) {

        // Действия при касании
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        } else
            ballRemoved = false;

        // Генерирование шариков по времени
        if (TimeUtils.nanoTime() - lastBallTime > spawnTime) spawnBall();

        // Изменение модели

        Iterator<Ball> iter = balls.iterator();
        while (iter.hasNext()) {
            Ball ball = iter.next();
            ball.setY((int)(ball.getY() - ball.getSpeed() * Gdx.graphics.getDeltaTime()));
            if (ball.getY() == 0)
                ball.setSpeed(200);

            // Проваливание шарика вниз
            if (ball.getY() + ballSize < 0) {
                lives -= 20;
                iter.remove();
            }
            if (!ballRemoved) {

                // Попадание в шарик
                if (ball.clickCheck(touchPos.x, touchPos.y))
                {
                    if (fieldColor((int) touchPos.y) == ball.getColor()) {
                        game.score += 10;
                        spawnTime = startSpawnTime - game.score * 1000000;
                    } else {
                        lives -= 10;
                    }
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
