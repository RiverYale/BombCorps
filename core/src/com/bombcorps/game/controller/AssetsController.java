package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.bombcorps.game.model.Constants;

public class AssetsController  implements Disposable, AssetErrorListener {
    public static final String TAG = AssetsController.class.getName();
    public static final AssetsController instance = new AssetsController();
    private AssetManager assetManager;

    public AssetHeroes heroes;

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.HEROES_OBJECTS, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(Constants.HEROES_OBJECTS);
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        heroes = new AssetHeroes(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    class AssetHeroes {
        public final TextureAtlas.AtlasRegion angel[] = new TextureAtlas.AtlasRegion[4];
        public final TextureAtlas.AtlasRegion protector[] = new TextureAtlas.AtlasRegion[4];
        public final TextureAtlas.AtlasRegion sniper[] = new TextureAtlas.AtlasRegion[4];
        public final TextureAtlas.AtlasRegion sparda[] = new TextureAtlas.AtlasRegion[4];
        public final TextureAtlas.AtlasRegion wizard[] = new TextureAtlas.AtlasRegion[4];

        AssetHeroes(TextureAtlas atlas) {
            for(int i=0; i<3; i++){
                angel[i] = atlas.findRegion("Angel"+i);
            }
            angel[4] = atlas.findRegion("Angel_dead");
            for(int i=0; i<3; i++){
                protector[i] = atlas.findRegion("Protector"+i);
            }
            protector[4] = atlas.findRegion("Protector_dead");
            for(int i=0; i<3; i++){
                sniper[i] = atlas.findRegion("Sniper"+i);
            }
            sniper[4] = atlas.findRegion("Sniper_dead");
            for(int i=0; i<3; i++){
                sparda[i] = atlas.findRegion("Sparda"+i);
            }
            sparda[4] = atlas.findRegion("Sparda_dead");
            for(int i=0; i<3; i++){
                wizard[i] = atlas.findRegion("Wizard"+i);
            }
            wizard[4] = atlas.findRegion("Wizard_dead");
        }
    }
}
