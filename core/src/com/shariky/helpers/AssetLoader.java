package com.shariky.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by User on 17.12.2016.
 */

public class AssetLoader {
    public Skin skin;
    private TextureAtlas bg_atlas;
    private TextureAtlas icons_atlas;
    private TextureAtlas balls_atlas;
    public HashMap<String, TextureRegion> kill_balls;
    public HashMap<String, TextureRegion> balls;
    public HashMap<String, TextureRegion> slow_balls;
    public HashMap<String, TextureRegion> multiclr_balls;
    public TextureRegion death_ball;
    public TextureRegion health_ball;
    public HashMap<String, TextureRegion> buttons;
    public Array<TextureRegion> hp;
    public TextureRegion bg;
    public static SpriteBatch basicBatch;
    public static BitmapFont basicFont;
    public static Sound click;
    public static Music musicBall, main_music;


    public void load() {
        bg_atlas = new TextureAtlas("bgs_balls");
        balls_atlas = new TextureAtlas("balls");
        icons_atlas = new TextureAtlas("icons");

        // Balls textures
        death_ball = new TextureRegion();
        balls = new HashMap<String, TextureRegion>(3);
        kill_balls = new HashMap<String, TextureRegion>(3);
        slow_balls = new HashMap<String, TextureRegion>(3);
        multiclr_balls = new HashMap<String, TextureRegion>(3);

        death_ball = balls_atlas.findRegion("death");
        health_ball = balls_atlas.findRegion("health");

        balls.put("blue", balls_atlas.findRegion("blue"));
        balls.put("gray", balls_atlas.findRegion("gray"));
        balls.put("green", balls_atlas.findRegion("green"));

        kill_balls.put("green", balls_atlas.findRegion("green_kb"));
        kill_balls.put("blue", balls_atlas.findRegion("blue_kb"));
        kill_balls.put("gray", balls_atlas.findRegion("gray_kb"));

        slow_balls.put("green", balls_atlas.findRegion("green_sb"));
        slow_balls.put("blue", balls_atlas.findRegion("blue_sb"));
        slow_balls.put("gray", balls_atlas.findRegion("gray_sb"));

        multiclr_balls.put("green_blue", balls_atlas.findRegion("green_blue"));
        multiclr_balls.put("tricolor", balls_atlas.findRegion("tricolor"));
        multiclr_balls.put("green_gray", balls_atlas.findRegion("green_gray"));
        multiclr_balls.put("blue_gray", balls_atlas.findRegion("blue_gray"));

        // Button textures
        buttons = new HashMap<String, TextureRegion>();
        buttons.put("repeat", icons_atlas.findRegion("repeat"));
        buttons.put("sound_off", icons_atlas.findRegion("soundoff"));
        buttons.put("sound_on", icons_atlas.findRegion("soundon"));
        buttons.put("pause", icons_atlas.findRegion("pause"));
        buttons.put("play", icons_atlas.findRegion("play"));



        // Background textures
        hp = new Array<TextureRegion>(true, 6);
        for (int k = 1; k != 7 ; ++k) {
            hp.add(bg_atlas.findRegion("hpbar", k));
        }

        bg = bg_atlas.findRegion("bg");

        skin = new Skin();
        skin.addRegions(balls_atlas);


        // Batch & fonts
        basicBatch = new SpriteBatch();
        basicFont = new BitmapFont(
                Gdx.files.internal("font.fnt"),
                Gdx.files.internal("font.png"),
                false
        );
        basicFont.getData().setScale(0.6f);


        // Music
        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        main_music = Gdx.audio.newMusic(Gdx.files.internal("sharikmus.mp3"));
        musicBall = Gdx.audio.newMusic(Gdx.files.internal("main_menu_jingle.mp3"));
        musicBall.setLooping(true);
        musicBall.setVolume(0.5f);
    }

    public void dispose(){
        bg_atlas.dispose();
        balls_atlas.dispose();
        basicBatch.dispose();
        basicFont.dispose();
        click.dispose();
        musicBall.dispose();
    }
}