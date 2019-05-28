package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.BombCorps;
import com.bombcorps.game.model.Constants;

public class InputController implements GestureDetector.GestureListener {

    public void handleGameInput() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("zc", "tap x="+x+" y="+y+" count="+count+" button="+button);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        BombCorps.x += deltaX;
        BombCorps.y -= deltaY;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }
    float temp;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        float zoomSize = distance/initialDistance;
        temp = MathUtils.clamp(zoomSize*BombCorps.zoom, Constants.MAX_ZOON_IN, Constants.MAX_ZOON_OUT);
        BombCorps.width = (int)(300*temp);
        BombCorps.height = (int)(300*temp);
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        BombCorps.zoom = temp;
    }
}
