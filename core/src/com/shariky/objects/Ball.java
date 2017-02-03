package com.shariky.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by User on 19.11.2016.
 */

public class Ball {
    private String color;

    private int speed;

    public Rectangle body;

    public Ball(String color, int x, int y, int speed) {
        this.body = new Rectangle();
        this.setColor(color);
        this.setX(x);
        this.setY(y);
        this.body.width = 60;
        this.body.height = 60;
        this.speed = speed;
    }

    // SETTLERS
    public void setColor(String color) {
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
    public int getSpeed() {
        return speed;
    }
    public String getColor() {
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

    public static String ballMix() {
        int color = MathUtils.random(0, 3);
        switch (color) {
            case 0:
                return "gray";
            case 1:
                return "green";
            case 2:
                return "blue";
        }
        return ballMix();
    }
}



