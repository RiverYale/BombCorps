package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.Image;
import java.awt.Label;
import java.util.concurrent.LinkedBlockingDeque;

public class LobbyScreen extends AbstractGameScreen{
    private static final String TAG = LobbyScreen.class.getName();

    private Image lsBackground; //大厅背景
    private Image perIfoBackground; //个人信息背景
    private Image roomListBackground;

    private Stage perIfoStage;  //个人信息舞台
    private Stage roomListStage; //房间列表舞台

    private Label labelShowName;    //昵称
    private Label labelShowRate;    //胜率
    private Label labelShowWinAmount;   //胜场
    private Label labelShowProperty;    //金币数量

    public LobbyScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }
}
