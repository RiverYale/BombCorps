package com.bombcorps.game.model.Bombs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;

public abstract class Bomb {
    private TextureRegion[] bomb;
    private float routeScale;

    private Vector2 position;
    private Vector2 dimension;
    private Vector2 bombScale;
    private Vector2 origin;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float rotation;

    private STATE state;
    public enum STATE{
        FLY, BOOM
    }

    public Bomb(int type, float angle, float speed, Vector2 inputPosition){
        init(inputPosition, angle, speed);

    }

    public void init(Vector2 inputPosition, float angle, float speed){
        bomb = new TextureRegion[2];

        routeScale = Constants.BOMB.ROUTESCALE;
        position = inputPosition;
        dimension = Constants.BOMB.DIMENSION;
        bombScale = Constants.BOMB.BOMBSCALE;
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        velocity = new Vector2( (float)(speed * Math.cos(angle)) , (float)(speed * Math.sin(angle)));
        acceleration = new Vector2(0,Constants.BOMB.ACCELERATION);
        rotation = 0;

        state = STATE.FLY;
    }

    public void update(float deltaTime){
        switch (state){
            case FLY:
                updateVelocity(deltaTime);
                updatePosition(deltaTime);
                break;
            case BOOM:


        }
    }

    public void updateVelocity(float deltaTime){
        velocity.y += acceleration.y * deltaTime;
    }

    public void updatePosition(float deltaTime){
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    protected void updateState(){

    }

    public void render(SpriteBatch batch){

    }

    public void renderBomb(SpriteBatch batch){

    }

    public void renderBoom(SpriteBatch batch){

    }


    /*
    get   set函数
    */
    public TextureRegion[] getBomb() {
        return bomb;
    }

    public void setBomb(TextureRegion[] bomb) {
        this.bomb = bomb;
    }

    public float getRouteScale() {
        return routeScale;
    }

    public void setRouteScale(float routeScale) {
        this.routeScale = routeScale;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDimension() {
        return dimension;
    }

    public void setDimension(Vector2 dimension) {
        this.dimension = dimension;
    }

    public Vector2 getBombScale() {
        return bombScale;
    }

    public void setBombScale(Vector2 bombScale) {
        this.bombScale = bombScale;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}
