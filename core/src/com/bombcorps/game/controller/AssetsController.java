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

    private AssetsController() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        textureMap = new HashMap<String, TextureAtlas.AtlasRegion>();
        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
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
//        if (textureMap.get(regionName) == null) {
//            Gdx.app.log("zc", regionName);
//        }
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
        assetManager.dispose();
        font.dispose();
    }
}
