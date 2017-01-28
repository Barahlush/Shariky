package com.shariky.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by User on 27.12.2016.
 */

public class Label {
    private  long time;
    private BitmapFont font;
    private String stroke;
    private String mem;
    private static String empt = "";
    private float speed;
    private float X;
    private float Y;
    private boolean isHide;

    public Label (BitmapFont fnt, float x, float y, String lbl) {
        time = TimeUtils.nanoTime();
        X = x;
        Y = y;
        font = fnt;
        stroke = lbl;
    }

    public String getStroke() {
        return stroke;
    }

    public void hide() {
        mem = stroke;
        stroke = empt;
        isHide = true;
    }

    public void show() {
        stroke = mem;
        isHide = false;
    }
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public boolean isHide() {
        return this.isHide;
    }

    public void draw(SpriteBatch batch, boolean blink, float period) {
        if (!blink)
            font.draw(batch, stroke, X, Y);
        else {
            if (TimeUtils.nanoTime() - time > period * 1000000000L) {
                isHide = !isHide;
                font.draw(batch, stroke, X, Y);
            }
        }
    }
}
