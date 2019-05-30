package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class RoomSelect {
    private Image smallMap;
    private Image background;
    private String modern;
    private String hosterName;
    private String peopleNum;
    private Label roomIfol;

    RoomSelect(Image smallMap,String modern,String hosterName,String peopleNum){
        this.smallMap = smallMap;
        this.modern = modern;
        this.hosterName = hosterName;
        this.peopleNum = peopleNum;
        background = new Image(new Texture(Gdx.files.internal("roombackground.png")));
    }

    public void setPosition(int x,int y){
    }

    public void addToStage(Stage stage){
        stage.addActor(background);
        stage.addActor(smallMap);
        stage.addActor(roomIfol);
    }
}
