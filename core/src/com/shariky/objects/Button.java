package com.shariky.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by User on 23.12.2016.
 */

public class Button {
    private TextureRegion btn_pic, btn_pic_prsd;
    private float height;
    private float width;
    private float speed;
    private float X;
    private float Y;
    private boolean isPressed;


    public Button (TextureRegion texture, float x, float y, float hg, float wd){
        btn_pic = texture;
        isPressed = false;
        X = x;
        speed = 0;
        Y = y;
        height = hg;
        width = wd;
    }

    public void setX(float x) {
        X = x;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float spd) {
        speed = spd;
    }

    public Button (TextureRegion texture, float x, float y){
        btn_pic = texture;
        isPressed = false;
        X = x;
        speed = 0;
        Y = y;
        height = texture.getRegionHeight();
        width = texture.getRegionWidth();
    }

    public void setButtonTexture(TextureRegion texture) {
        btn_pic = texture;
    }

    public boolean clickCheck(float touch_x, float touch_y) {
        if ((touch_x - X < width && touch_x - X >= 0) &&
                (touch_y - Y < height * (4/3) && touch_y - Y >= 0)) {
            return true;
        } else {
            return false;
        }
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(btn_pic, X, Y, width, height);
    }

}
