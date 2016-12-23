package com.shariky.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by User on 19.11.2016.
 */

public class Ball {
    private Texture color;

    private int speed;

    public Rectangle body;
    public Ball(Texture color, int x, int y, int speed) {
        this.body = new Rectangle();
        this.setColor(color);
        this.setX(x);
        this.setY(y);
        this.body.width = 60;
        this.body.height = 60;
        this.speed = speed;
    }

    // SETTLERS
    public void setColor(Texture color) {
    this.color = color;
    }
    public void setX(int X) {
        this.body.x = X;
    }
    public void setY(int Y) {
        this.body.y = Y;
    }
    public void setSpeed(int speed) { this.speed = speed; }

    // GETTERS
    public int getSpeed() {
        return this.speed;
    }
    public Texture getColor() {
        return this.color;
    }
    public float getX() {
        return this.body.x;
    }
    public float getY() {
        return this.body.y;
    }
    public float getHeight() { return this.body.height; }
    public float getWidth() { return this.body.width; }

    public boolean clickCheck(float touch_x, float touch_y) {
        if (((touch_x - body.getX() < 60 && touch_x - body.getX() >= 0) ||
                (touch_x - body.getX() >= 0 && touch_x - body.getX() <= 0))
        &&
        ((touch_y - body.getY() < 80 && touch_y - body.getY() >= 0) ||
                (touch_y - body.getY() >= 0 && touch_y - body.getY() <= 0))) {
            return true;
        } else {
            return false;
        }
    }
}


