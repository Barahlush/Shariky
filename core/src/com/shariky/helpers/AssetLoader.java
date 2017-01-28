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

/**
 * Created by User on 17.12.2016.
 */

public class AssetLoader {
    public Skin skin;
    private TextureAtlas bg_atlas;
    private TextureAtlas icons_atlas;
    public TextureRegion yl_bl;
    public TextureRegion rd_gr;
    public TextureRegion grn;
    public TextureRegion bl_kill;
    public TextureRegion playup;
    public TextureRegion repeat;
    public Array<TextureRegion> hp;
    public TextureRegion bg;
    public TextureRegion pause;
    public TextureRegion sound_on;
    public TextureRegion sound_off;
    public static SpriteBatch basicBatch;
    public static BitmapFont basicFont;
    public static Sound click;
    public static Music musicBall, main_music;


    public void load() {
        bg_atlas = new TextureAtlas("bgs_balls");
        icons_atlas = new TextureAtlas("icons_pack");

        bl_kill = icons_atlas.findRegion("killer");
        yl_bl = icons_atlas.findRegion("yl");
        rd_gr = icons_atlas.findRegion("rd");
        grn = icons_atlas.findRegion("grn");
        repeat = icons_atlas.findRegion("repeat");
        sound_off = icons_atlas.findRegion("soundoff");
        sound_on = icons_atlas.findRegion("soundon");
        playup = icons_atlas.findRegion("playup");
        pause = icons_atlas.findRegion("pause");
        hp = new Array<TextureRegion>(true, 6);
        for (int k = 1; k != 7 ; ++k) {
            hp.add(bg_atlas.findRegion("hpbar", k));
        }
        bg = bg_atlas.findRegion("bg");

        skin = new Skin();
        skin.addRegions(icons_atlas);

        basicBatch = new SpriteBatch();
        basicFont = new BitmapFont(
                Gdx.files.internal("font.fnt"),
                Gdx.files.internal("font.png"),
                false
        );
        basicFont.getData().setScale(0.6f);

        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        main_music = Gdx.audio.newMusic(Gdx.files.internal("sharikmus.mp3"));
        musicBall = Gdx.audio.newMusic(Gdx.files.internal("main_menu_jingle.mp3"));
        musicBall.setLooping(true);
        musicBall.setVolume(0.5f);
    }

    public void dispose(){
        bg_atlas.dispose();
        icons_atlas.dispose();
        basicBatch.dispose();
        basicFont.dispose();
        click.dispose();
        musicBall.dispose();
    }
}