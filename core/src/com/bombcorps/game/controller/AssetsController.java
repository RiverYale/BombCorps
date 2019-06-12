package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

public class AssetsController  implements Disposable, AssetErrorListener {
    public static final String TAG = AssetsController.class.getName();
    public static final AssetsController instance = new AssetsController();
    private AssetManager assetManager;
    private HashMap<String, TextureAtlas.AtlasRegion> textureMap;
    private HashMap<String, Sound> soundMap;
    private HashMap<String, Music> musicMap;
    public BitmapFont font;

    //备用
    public Music bgm1;
    public Music bgm2;
    public Sound btnClicked;
    public Sound levelup;
    public Sound lose;
    public Sound win;

    public Sound angelshoot;
    public Sound spardashoot;
    public Sound snipershoot;
    public Sound protectorshoot;

    public Sound protectorskill1;
    public Sound protectorskill3;
    public Sound spardaskill1;
    public Sound spardaskill3;
    public Sound angelskill1;
    public Sound angelskill3;
    public Sound wizardskill1;
    public Sound wizardskill3;
    public Sound sniperskill1;
    public Sound sniperskill3;

    public Sound angelboom0;
    public Sound angelboom1;

    public Sound spardaboom0;

    public Sound sniperboom0;
    public Sound sniperboom1;

    public Sound protectorboom0;
    public Sound protectorboom1;

    public Sound wizardboom0;
    public Sound wizardboom1;

    public Sound tp;

    private AssetsController() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        textureMap = new HashMap<String, TextureAtlas.AtlasRegion>();
        musicMap = new HashMap<String, Music>();
        soundMap = new HashMap<String, Sound>();

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
        assetManager.load("music/bgm.mp3",Music.class);
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("music/bgm.mp3"));
        bgm2 = Gdx.audio.newMusic(Gdx.files.internal("music/bgm2.mp3"));
        btnClicked = Gdx.audio.newSound(Gdx.files.internal("sound/btnclicked.mp3"));
        levelup =Gdx.audio.newSound(Gdx.files.internal("sound/levelup.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.internal("sound/lose.mp3"));
        win = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
        angelshoot =Gdx.audio.newSound(Gdx.files.internal("sound/angelshoot.mp3"));
        snipershoot =Gdx.audio.newSound(Gdx.files.internal("sound/snipershoot.mp3"));
        spardashoot =Gdx.audio.newSound(Gdx.files.internal("sound/spardashoot.mp3"));
        protectorshoot =Gdx.audio.newSound(Gdx.files.internal("sound/protectorshoot.mp3"));
        spardaskill1 = Gdx.audio.newSound(Gdx.files.internal("sound/spardaskill1.mp3"));
        spardaskill3 = Gdx.audio.newSound(Gdx.files.internal("sound/spardaskill3.mp3"));
        angelskill1 =Gdx.audio.newSound(Gdx.files.internal("sound/angelskill1.mp3"));
        angelskill3 =Gdx.audio.newSound(Gdx.files.internal("sound/angelskill3.mp3"));
        protectorskill1 =Gdx.audio.newSound(Gdx.files.internal("sound/protectorskill1.mp3"));
        protectorskill3 =Gdx.audio.newSound(Gdx.files.internal("sound/protectorskill3.mp3"));
        wizardskill1 = Gdx.audio.newSound(Gdx.files.internal("sound/wizardskill1.mp3"));
        wizardskill3 = Gdx.audio.newSound(Gdx.files.internal("sound/wizardskill3.mp3"));
        sniperskill1 =Gdx.audio.newSound(Gdx.files.internal("sound/sniperskill1.mp3"));
        sniperskill3 = Gdx.audio.newSound(Gdx.files.internal("sound/sniperskill3.mp3"));

        angelboom0 =Gdx.audio.newSound(Gdx.files.internal("sound/angelboom0.mp3"));
        angelboom1 =Gdx.audio.newSound(Gdx.files.internal("sound/angelboom1.mp3"));
        spardaboom0 = Gdx.audio.newSound(Gdx.files.internal("sound/spardaboom0.mp3"));
        sniperboom0 =Gdx.audio.newSound(Gdx.files.internal("sound/sniperboom0.mp3"));
        sniperboom1 = Gdx.audio.newSound(Gdx.files.internal("sound/sniperboom1.mp3"));
        protectorboom0 =Gdx.audio.newSound(Gdx.files.internal("sound/protectorboom0.mp3"));
        protectorboom1 = Gdx.audio.newSound(Gdx.files.internal("sound/protectorboom1.mp3"));
        wizardboom0 =Gdx.audio.newSound(Gdx.files.internal("sound/wizardboom0.mp3"));
        wizardboom1 = Gdx.audio.newSound(Gdx.files.internal("sound/wizardboom1.mp3"));

        tp =Gdx.audio.newSound(Gdx.files.internal("sound/tp.mp3"));
    }

    public void loadTexture(String fileName) {
        assetManager.load(fileName, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(fileName);
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        for(TextureAtlas.AtlasRegion t : atlas.getRegions()){
            textureMap.put(t.name, t);
        }
    }

    public TextureAtlas.AtlasRegion getRegion(String regionName) {
        if (textureMap.get(regionName) == null) {
            Gdx.app.log(TAG, "Couldn't get " + regionName);
        }
        return textureMap.get(regionName);
    }

    public void loadSound(String key, String fileName) {
        assetManager.load(fileName, Sound.class);
        assetManager.finishLoading();
        soundMap.put(key, assetManager.get(fileName, Sound.class));
    }

    public Sound getSound(String key) {
        return soundMap.get(key);
    }

    public void loadMusic(String key, String fileName) {
        assetManager.load(fileName, Music.class);
        assetManager.finishLoading();
        musicMap.put(key, assetManager.get(fileName, Music.class));
    }

        public Music getMusic(String key) {
        return musicMap.get(key);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        textureMap.clear();
        soundMap.clear();
        musicMap.clear();
        assetManager.dispose();
        font.dispose();
    }
}
