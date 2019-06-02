package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;

public class SiteShow {
    private int heroSelect;
    private Image hero[];
    private String playerName;
    private int level;

    private Label labelPlayerName;
    private Label labelLevel;

    private Table table;
    private Stack stack;

    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();

    SiteShow(int heroSelect,String playerId,int level){
        heroSelect = 0;
        table = new Table();
        //table.setSize(100,100);
        stack = new Stack();
        stack.setSize(0.111f * width,0.16f * height);

        hero = new Image[5];
        hero[0] = new Image(AssetsController.instance.getRegion("Angel_stand"));
        hero[1] = new Image(AssetsController.instance.getRegion("Sparda_stand"));
        hero[2] = new Image(AssetsController.instance.getRegion("Protector_stand"));
        hero[3] = new Image(AssetsController.instance.getRegion("Sniper_stand"));
        hero[4] = new Image(AssetsController.instance.getRegion("Wizard_stand"));

        this.heroSelect = heroSelect;
        this.playerName = playerId;
        this.level =level;

        BitmapFont font = AssetsController.instance.font;
        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);
        String levelShow = "";
        for(int i=0; i<level;i++){
            levelShow = levelShow + "★";
        }
        labelPlayerName = new Label(playerName,style);
        labelPlayerName.setOrigin(labelPlayerName.getWidth() / 2,labelPlayerName.getHeight() / 2);
        labelPlayerName.setFontScale(0.000555f * width,0.001f * height);
        //labelPlayerName.debug();

        labelLevel = new Label(levelShow,style);
        labelLevel.setOrigin(labelLevel.getWidth() / 2,labelLevel.getHeight() / 2);
        labelLevel.setFontScale(0.000555f * width,0.001f * height);

        table.add(labelPlayerName).height(0.06f * height);
        table.row();
        table.add(labelLevel);
        //table.debug();

        stack.addActor(table);
    }

    public void setPosition(float x,float y){
        //Gdx.app.log("heroselect",heroSelect+"");
        hero[heroSelect].setSize((hero[heroSelect].getWidth()/900)*width,(hero[heroSelect].getHeight()/500)*height);
        hero[heroSelect].setPosition(x - hero[heroSelect].getWidth() / 2,y + 0.04f * height);
        //hero[heroSelect].debug();
        stack.setPosition(x - stack.getWidth() / 2,y - 0.12f * height);
    }

    public void addToStage(Stage stage){
        stage.addActor(hero[heroSelect]);
        stage.addActor(stack);
    }
}
