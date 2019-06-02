package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class RoomSelect {
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();

    private int mapNum;
    private Image backGround;
    private String mode;
    private String hosterName;
    private String personNum;

    private Image smallMap;
    private Label labelMode;
    private Label labelHosterName;
    private Label labelPersonNum;

    private boolean click = false;

    RoomSelect(int mapNum, String mode, String hosterName, String personNum){
        this.mapNum = mapNum;
        this.mode = mode;
        this.hosterName = hosterName;
        this.personNum = personNum;

        backGround = new Image(new Texture(Gdx.files.internal("lobbyscreen/roomshowbackground.png")));
        backGround.setSize(0.45f * width,0.1f * height);

        //smallMap = new Image();
        switch (mapNum){
            case 0:
                smallMap = new Image(new Texture(Gdx.files.internal("lobbyscreen/map0.png")));
                break;
        }
        smallMap.setSize(0.035f * width, 0.035f * width);

        BitmapFont font = new BitmapFont(Gdx.files.internal("lobbyscreen/test3.fnt"),
                Gdx.files.internal("lobbyscreen/test3.png"),false);
        Label.LabelStyle style = new Label.LabelStyle(font,font.getColor());
        labelMode = new Label(mode, style);
        labelMode.setFontScale(0.05f * width / 100);
        labelHosterName = new Label(hosterName,style);
        labelHosterName.setFontScale(0.05f * width / 100);
        labelPersonNum = new Label(personNum,style);
        labelPersonNum.setFontScale(0.05f * width / 100);

        click = false;
        addListener();
    }

    public void setPosition(float x,float y){
        backGround.setPosition(x, y);
        float middle = backGround.getY() + backGround.getHeight()/2;
        smallMap.setPosition(x + 0.024f * width, backGround.getY() + backGround.getHeight()/2 -smallMap.getHeight()/2 );
        labelMode.setPosition(backGround.getX() + 0.07f * width,middle - labelMode.getHeight()/2 );
        labelHosterName.setPosition(labelMode.getX() + 0.13f * width,labelMode.getY());
        labelPersonNum.setPosition(labelHosterName.getX() + 0.13f * width,labelMode.getY());
    }

    public void addToStage(Stage stage){
        stage.addActor(backGround);
        stage.addActor(smallMap);
        stage.addActor(labelMode);
        stage.addActor(labelHosterName);
        stage.addActor(labelPersonNum);
    }

    public boolean isClick(){
        return click;
    }

    public void update(float deltaTime){
        click = false;
    }

    public void addListener(){
        backGround.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }
        });

        smallMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }

        });

        labelMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }

        });

        labelHosterName.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }

        });

        labelPersonNum.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }
        });
    }
}