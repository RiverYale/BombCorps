package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class Aura {
    private Vector2 position;
    private AURA state;
    private float scaleChange = 0;

    private enum AURA{
        SPARDAAURA, PROTECTORAURA, ANGELAURA, SNIPERAURA,WIZARDAURA0,WIZARDAURA1,WAIT;

        public TextureRegion aura;
        public Vector2 scale;
        public Vector2 origin;
        public Vector2 dimension;

        AURA(){
            aura = new TextureRegion();
            scale = Constants.AURA.SCALE;
            origin = Constants.AURA.ORIGIN;
            dimension = Constants.AURA.DIMENSION;
        }
    }

    public Aura(){
        position = new Vector2();
        state = AURA.WAIT;
        initAura();
    }

    private void initAura(){
        AURA.SPARDAAURA.aura = AssetsController.instance.getRegion("SpardaAura0");
        AURA.PROTECTORAURA.aura = AssetsController.instance.getRegion("ProtectorAura0");
        AURA.ANGELAURA.aura = AssetsController.instance.getRegion("AngelAura0");
        AURA.SNIPERAURA.aura = AssetsController.instance.getRegion("SniperAura0");
        AURA.WIZARDAURA0.aura = AssetsController.instance.getRegion("WizardAura0");
        AURA.WIZARDAURA1.aura = AssetsController.instance.getRegion("WizardAura1");
    }

    public void update(float deltaTime, Vector2 position){
        this.position = position;
        AURA.SPARDAAURA.scale.x += scaleChange;
        AURA.SPARDAAURA.scale.y += scaleChange;
        AURA.WIZARDAURA0.scale.x += scaleChange;
        AURA.WIZARDAURA0.scale.y += scaleChange;
        AURA.WIZARDAURA1.scale.x += scaleChange;
        AURA.WIZARDAURA1.scale.y += scaleChange;
        AURA.SNIPERAURA.scale.x += scaleChange;
        AURA.SNIPERAURA.scale.y += scaleChange;
        AURA.ANGELAURA.scale.x += scaleChange;
        AURA.ANGELAURA.scale.y += scaleChange;
        AURA.PROTECTORAURA.scale.x += scaleChange;
        AURA.PROTECTORAURA.scale.y += scaleChange;
        if(AURA.SPARDAAURA.scale.x >= 2.2){
            scaleChange = -0.001f;
        }
        if(AURA.SPARDAAURA.scale.x <= 1.8){
            scaleChange = 0.003f;
        }

    }

    public void render(SpriteBatch batch){
        if(state != AURA.WAIT) {
            batch.draw(state.aura, position.x + 0.22f, position.y + 0.22f, state.origin.x, state.origin.y,
                    state.dimension.x, state.dimension.y, state.scale.x, state.scale.y, 0);
        }
    }

    public void setState(int input){
        switch (input){
            case Constants.AURA.WAIT:
                state = AURA.WAIT;
                break;
            case Constants.AURA.SPARDAAURA:
                state = AURA.SPARDAAURA;
                break;
            case Constants.AURA.PROTECTORAURA:
                state = AURA.PROTECTORAURA;
                break;
            case Constants.AURA.ANGELAURA:
                state = AURA.ANGELAURA;
                break;
            case Constants.AURA.SNIPERAURA:
                state = AURA.SNIPERAURA;
                break;
            case Constants.AURA.WIZARDAURA0:
                state = AURA.WIZARDAURA0;
                break;
            case Constants.AURA.WIZARDAURA1:
                state = AURA.WIZARDAURA1;
                break;
        }

    }


}
