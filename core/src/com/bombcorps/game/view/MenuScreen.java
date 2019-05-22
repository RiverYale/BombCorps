package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        if (Gdx.input.isTouched()) {
//            game.setScreen(new GameHubScreen(game)); //检测到触碰立即切换到GameHubScreen
//        }


    }
    @Override public void resize(int width,int height){}
    @Override public void show(){}
    @Override public void hide(){}
    @Override public void pause(){}
    private Stage stage;

}