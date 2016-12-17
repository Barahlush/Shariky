package com.shariky.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by User on 17.12.2016.
 */

public class AssetLoader {

    public static SpriteBatch basicBatch;
    public static BitmapFont basicFont;
    public static Texture greenBall, yellowBall, redBall, bgtexture;
    public static Sound click;
    public static Music musicBall;


    public static void load() {

        basicBatch = new SpriteBatch();
        basicFont = new BitmapFont();
        greenBall = new Texture("gr60.png");
        greenBall.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        redBall = new Texture("red_grey60.png");
        redBall.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        yellowBall = new Texture("yel_bl60.png");
        yellowBall.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        bgtexture = new Texture("bgpicnew.png");
        bgtexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);

        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        musicBall = Gdx.audio.newMusic(Gdx.files.internal("sharikmus.mp3"));
    }

    public static void dispose(){
        greenBall.dispose();
        yellowBall.dispose();
        redBall.dispose();
        bgtexture.dispose();
        click.dispose();
        musicBall.dispose();
    }
}