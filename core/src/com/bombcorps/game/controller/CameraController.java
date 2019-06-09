package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.Constants;

public class CameraController {
    private Vector2 position;
    private Object target;
    private float zoom;

    public CameraController() {
        position = new Vector2();
        zoom = 1f;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, Constants.MAX_ZOON_IN, Constants.MAX_ZOON_OUT);
    }

    public void addZoom(float amount) {
        this.zoom = MathUtils.clamp(zoom+amount, Constants.MAX_ZOON_IN, Constants.MAX_ZOON_OUT);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean hasTarget(){
        return target != null;
    }

    public void update(float deltaTime) {
        if(target != null){
            if(target instanceof Player){
                position.lerp(((Player) target).getPosition(), Constants.FOLLOW_SPEED * deltaTime);
            }else if(target instanceof Bomb){
                position.lerp(((Bomb) target).getPosition(), Constants.FOLLOW_SPEED * deltaTime);
            }
        }
    }

    public void applyTo(OrthographicCamera camera) {
        float tx = MathUtils.clamp(position.x, 0, 32);
        float ty = MathUtils.clamp(position.y, 5*zoom, 30-5*zoom);
        position.x = tx;
        position.y = ty;
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
