package com.shariky.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.shariky.objects.BonusBalls.BallSlow;
import com.shariky.screens.Shariky;

import static com.shariky.objects.Ball.Colors.blue;
import static com.shariky.objects.Ball.Colors.blue_gray;
import static com.shariky.objects.Ball.Colors.gray;
import static com.shariky.objects.Ball.Colors.green;
import static com.shariky.objects.Ball.Colors.green_blue;
import static com.shariky.objects.Ball.Colors.green_gray;
import static com.shariky.objects.Ball.Types.ball;
import static com.shariky.objects.Ball.Types.death;
import static com.shariky.objects.Ball.Types.health;
import static com.shariky.objects.Ball.Types.kill;
import static com.shariky.objects.Ball.Types.multicolor;
import static com.shariky.objects.Ball.Types.slow;

/**
 * Created by User on 19.11.2016.
 */

public class Ball {
    public enum Types {
        ball,
        multicolor,
        kill,
        slow,
        health,
        death
    }
    public enum Colors {
        blue,
        gray,
        green,
        death_ball,
        health_ball,
        green_blue,
        green_gray,
        blue_gray,
        tricolor
    }
    protected Colors color;
    protected Types type;

    protected int speed;

    public Rectangle body;

    public Ball(Colors color, int x, int y, int speed) {
        this.body = new Rectangle();
        this.type = ball;
        this.setColor(color);
        this.setX(x);
        this.setY(y);
        this.body.width = 60;
        this.body.height = 60;
        this.speed = speed;
    }
    public Ball() {
        this.body = new Rectangle();
        this.type = ball;
        this.setColor(green);
        this.setX(0);
        this.setY(0);
        this.body.width = 60;
        this.body.height = 60;
        this.speed = speed;
    }

    public Ball(Ball other) {
        this.body = other.body;
        this.type = other.type;
        this.color = other.color;
        this.setX((int)other.body.x);
        this.setY((int)other.body.y);
        this.body.width = 60;
        this.body.height = 60;
        this.speed = other.speed;
        this.type = ball;
    }


    // SETTLERS
    public void setColor(Colors color) {
    this.color = color;
    }
    public void setX(int X) {
        body.x = X;
    }
    public void setY(int Y) {
        body.y = Y;
    }
    public void setSpeed(int speed) { this.speed = speed; }

    // GETTERS
    public Types getType() {return type;}
    public int getSpeed() {
        return speed;
    }
    public Colors getColor() {
        return color;
    }
    public float getX() {
        return body.x;
    }
    public float getY() {
        return body.y;
    }
    public float getHeight() { return body.height; }
    public float getWidth() { return body.width; }

    public boolean clickCheck(float touch_x, float touch_y) {
        if ((touch_x - body.getX() < body.width && touch_x - body.getX() >= 0) &&
        (touch_y - body.getY() < body.height * (4/3) && touch_y - body.getY() >= 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static Ball.Colors ballColorMix() {
        int color = MathUtils.random(0, 3);
        switch (color) {
            case 0:
                return gray;
            case 1:
                return green;
            case 2:
                return blue;
        }
        return ballColorMix();
    }

    public static Types ballTypeMix() {
        int color = MathUtils.random(0, 13);
        switch (color) {
            case 2:
            case 3:
                return  multicolor;
            case 4:
            case 5:
            case 10:
            case 11:
            case 12:
                return ball;
            case 6:
            case 0:
                return kill;
            case 7:
            case 1:
                return slow;
            case 8:
                return health;
            case 9:
                return death;

        }
        return ballTypeMix();
    }

    public void hit(Shariky game) {
        game.score += 10;
    }

    public boolean isSlowed() {
        return BallSlow.getSlowTime().containsKey(color)
            && (BallSlow.getSlowTime().get(color) > 0);
    }

    public void mult_simpl(Ball.Colors field) {
        switch (this.color) {
            case green_blue:
                switch (field) {
                    case green:
                        this.color = blue;
                        this.type = ball;
                        break;
                    case blue:
                        this.color = green;
                        this.type = ball;
                        break;
                }
                break;
            case green_gray:
                switch (field) {
                    case green:
                        this.color = gray;
                        this.type = ball;
                        break;
                    case gray:
                        this.color = green;
                        this.type = ball;
                        break;
                }
                break;
            case blue_gray:
                switch (field) {
                    case blue:
                        this.color = gray;
                        this.type = ball;
                        break;
                    case gray:
                        this.color = blue;
                        this.type = ball;
                        break;
                }
                break;
            case tricolor:
                switch (field) {
                    case green:
                        this.color = blue_gray;
                        break;
                    case blue:
                        this.color = green_gray;
                        break;
                    case gray:
                        this.color = green_blue;
                        break;
                }
                break;
        }
    }
}



