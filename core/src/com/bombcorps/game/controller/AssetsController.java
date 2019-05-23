package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

public class AssetsController  implements Disposable, AssetErrorListener {
    public static final String TAG = AssetsController.class.getName();
    public static final AssetsController instance = new AssetsController();
    private AssetManager assetManager;
    private HashMap<String, TextureAtlas.AtlasRegion> assetsMap;

    public AssetsController() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetsMap = new HashMap<String, TextureAtlas.AtlasRegion>();
    }

    public void load(String fileName) {
        assetManager.load(fileName, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(fileName);
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        for(TextureAtlas.AtlasRegion t : atlas.getRegions()){
            assetsMap.put(t.name, t);
        }
    }

    public TextureAtlas.AtlasRegion getRegion(String regionName) {
        return assetsMap.get(regionName);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetsMap.clear();
        assetManager.dispose();
    }
}
