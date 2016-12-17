package com.shariky.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    final Shariky game;

    int lives;
    static final int ballSize = 60;
    long spawnTime, lastBallTime;
    boolean ballRemoved;
    static long startSpawnTime;
    OrthographicCamera camera;
    Sound click;
	SpriteBatch batch;
	Texture greenBall, yellowBall, redBall, bgtexture, top;
	Music musicBall;
	Vector3 touchPos;
    Array<Ball> balls;
    Rectangle touch;

	public GameScreen (final Shariky gam) {
        this.game = gam;

		touchPos = new Vector3();
        touch = new Rectangle();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);

        batch = new SpriteBatch();

		greenBall = new Texture("green60.png");
		redBall = new Texture("red60.png");
		yellowBall = new Texture("yellow60.png");
        bgtexture = new Texture("bgpic.png");
        top = new Texture("topbar.png");

		musicBall = Gdx.audio.newMusic(Gdx.files.internal("sharikmus.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));

        ballRemoved = false;

		musicBall.setLooping(true);
		musicBall.play();

        startSpawnTime = 1500000000L;
        spawnTime = startSpawnTime;
        game.score = 0;
        lives = 100;

        balls = new Array<Ball>();
        spawnBall();

        Gdx.gl.glClearColor(0.7f, 0.5f, 0, 1);
	}

    // Генератор шариков

    private void spawnBall() {
        Ball ball = new Ball(
                              ballMix(),
                              MathUtils.random(0, 480 - ballSize),
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
            case 0: return redBall;
            case 1: return greenBall;
            case 2: return yellowBall;
        }
        return ballMix();
    }

    // Определение цвета поля по касанию

    private Texture fieldColor(int y) {
        if (y < 250) return redBall;
        else if (y < 500) return yellowBall;
        else return greenBall;
    }


    // Отрисовка
	@Override
	public void render (float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

        game.batch.draw(bgtexture, 0, 0);
        for (Ball ball : balls) {
            game.batch.draw(ball.getColor(), ball.getX(), ball.getY());
        }
        game.batch.draw(top, 0, 750);
        game.font.draw(game.batch, "Record: " + game.record, 0, 800);
        game.font.draw(game.batch, "Score: " + game.score, 190, 800);
        game.font.draw(game.batch, "Health: " + lives + "%", 190, 780);
		game.batch.end();


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
                if (((touchPos.x - ball.getX() < 60 && touchPos.x - ball.getX() >= 0) ||
                     (touchPos.x - ball.getX() >= 0 && touchPos.x - ball.getX() <= 0))
                   &&
                    ((touchPos.y - ball.getY() < 80 && touchPos.y - ball.getY() >= 0) ||
                     (touchPos.y - ball.getY() >= 0 && touchPos.y - ball.getY() <= 0)))
                {
                    if (fieldColor((int) touchPos.y) == ball.getColor()) {
                        game.score += 10;
                        spawnTime = startSpawnTime - game.score * 1000000;
                    } else {
                        lives -= 10;
                    }
                    ballRemoved = true;
                    click.play();
                    iter.remove();
                }
            }
        }

        touchPos.set(0, 0, 0);

    // Конец жизней
        if (lives <= 0) {
            musicBall.stop();
            game.setScreen(new FailScreen(game));
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
        musicBall.stop();
    }

    // Очистка от мусора после закрытия
    @Override
	public void dispose () {
		batch.dispose();
		redBall.dispose();
		greenBall.dispose();
        yellowBall.dispose();
        musicBall.dispose();
        bgtexture.dispose();
        top.dispose();
        click.dispose();
	}

    @Override
    public void show() {
        musicBall.play();
    }
}
