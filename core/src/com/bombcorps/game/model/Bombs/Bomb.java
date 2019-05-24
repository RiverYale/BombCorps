package com.bombcorps.game.model.Bombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;

public class Bomb {
//    public static Bomb instance = new Bomb();

    private int heroType;
    private int bombType;
    private TextureRegion[][] bomb;
    private TextureRegion[][][] boom;
    private float routeScale;

    private Vector2 position;
    private Vector2 dimension;
    private Vector2 bombScale;
    private Vector2 origin;

    private Vector2 boomOrigin;
    private Vector2 boomDimension;
    private Vector2 boomScale;

    private float angle;
    private float speed;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float rotation;
    private Rectangle rec;
    private Animation boomAnimation;
    private TextureRegion boomKeyFrame;
    private int stateTime;

    private STATE state;
    public enum STATE{
        FLY, BOOM, WAIT
    }

    public Bomb(){
        init();
    }

    public void init(){
        stateTime = 0;
        heroType = 0;
        bombType = 0;

        initBomb();
        initBoom();

        routeScale = Constants.BOMB.ROUTESCALE;
        dimension = Constants.BOMB.DIMENSION;
        bombScale = Constants.BOMB.BOMBSCALE;
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        velocity = new Vector2( (float)(speed * Math.cos(angle)) , (float)(speed * Math.sin(angle)));
        acceleration = new Vector2(0,Constants.BOMB.ACCELERATION);
        rotation = 0;

        rec = new Rectangle(0,0,dimension.x,dimension.y);
    }

    public void update(float deltaTime){
        switch (state){
            case FLY:
                updateRotation(deltaTime);
                updateVelocity(deltaTime);
                updatePosition(deltaTime);
                break;
        }
    }

    private void initBomb(){
        bomb = new TextureRegion[5][3];
        String temp = "";
        for(int i = 0 ; i < 5 ; i++){
            switch (i){
                case 0:
                    temp = "SpardaBomb";
                    break;
                case 1:
                    temp = "ProtectorBomb";
                    break;
                case 2:
                    temp = "AngelBomb";
                    break;
                case 3:
                    temp = "SniperBomb";
                    break;
                case 4:
                    temp = "WizardBomb";
                    break;
            }
            for(int j = 0 ; j < 2 ; j++){
                bomb[i][j] = AssetsController.instance.getRegion(temp + j);
            }
        }
        bomb[2][2] = AssetsController.instance.getRegion("AngelBomb2");
    }

    private void initBoom(){
        boomDimension = Constants.BOMB.BOOM_DIMENSION;
        boomOrigin = new Vector2(boomDimension.x / 2, boomDimension.y / 2);
        boomScale = Constants.BOMB.BOOM_SCALE;

        boom = new TextureRegion[5][3][4];
        String temp = "SpardaBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[0][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "ProtectorBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[1][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "ProtectorShield";
        for(int i = 0 ; i < 4 ; i++){
            boom[1][1][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "AngelBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[2][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "AngelHeal";
        for(int i = 0 ; i < 4 ; i++){
            boom[2][1][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "AngelWeak";
        for(int i = 0 ; i < 4 ; i++){
            boom[2][2][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "SniperBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[3][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "SniperAntiArmor";
        for(int i = 0 ; i < 4 ; i++){
            boom[3][1][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "WizardBomb";
        for(int i = 0 ; i < 4 ; i++){
            boom[4][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "WizardBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[4][1][i] = AssetsController.instance.getRegion(temp + i);
        }
    }

    protected void updateRotation(float deltaTime){
        rotation += Constants.BOMB.ROTATE_SPEED * deltaTime;
        rotation %= 360;
    }

    protected void updateVelocity(float deltaTime){
        velocity.y += acceleration.y * deltaTime;
    }

    protected void updatePosition(float deltaTime){
        position.x += velocity.x * deltaTime * routeScale;
        position.y += velocity.y * deltaTime * routeScale;
    }

    public void render(SpriteBatch batch){

        switch(state){
            case FLY:
                renderBomb(batch);
            case BOOM:
                renderBoom(batch);
        }
    }

    public void renderBomb(SpriteBatch batch){
        batch.draw(bomb[heroType][bombType], position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y,
                bombScale.x, bombScale.y,rotation);
    }


    public void renderBoom(SpriteBatch batch){
        boomKeyFrame = (TextureRegion) boomAnimation.getKeyFrame(stateTime);
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw(boomKeyFrame, position.x, position.y, boomOrigin.x, boomOrigin.y, boomDimension.x, boomDimension.y,
                boomScale.x,boomScale.y,0);

        if(stateTime > boomAnimation.getAnimationDuration()){
            state = STATE.WAIT;
        }
    }


    /*
    get   set函数
    */

    public int getBombType() {
        return bombType;
    }

    public void setBombType(int bombType) {
        this.bombType = bombType;
        int boomType = 0;
        switch(heroType){
            case 1:
            case 2:
            case 3:
                boomType = bombType;
                break;
        }

        Array<TextureRegion> temp = new Array<TextureRegion>(boom[heroType][boomType]);
        boomAnimation = new Animation(Constants.BOMB.BOOM_DURETION, temp);
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getHeroType() {
        return heroType;
    }

    public void setHeroType(int heroType) {
        this.heroType = heroType;
    }

    public Rectangle getRec() {
        rec.x = position.x;
        rec.y = position.y;
        return rec;
    }

    public void setRec(Rectangle rec) {
        this.rec = rec;
    }

    public void setState(int state) {
        switch (state){
            case 0:
                this.state = STATE.FLY;
                break;
            case 1:
                stateTime = 0;
                this.state = STATE.BOOM;
                break;
        }
    }
}
