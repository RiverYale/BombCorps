package com.bombcorps.game.model.heros;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.model.Constants;

public abstract class AbstractHero {
    /*
        角色基本属性
     */
    private float health;         //血量
    private float endurance;      //精力
    private float ragePower;      //怒气值

    private float attack;         //攻击力
    private float criticalProbability;  //暴击几率
    private float armor;          //护甲值

    /*
    角色其他属性
     */
    private boolean isMove;
    private boolean headright;  //方向  true表示朝右 反之朝左
    private Vector2 position;   //位置
    private Vector2 dimension;  //大小
    private Vector2 origin;     //焦点
    private Vector2 scale;      //缩放
    private Vector2 velocity;   //速度
    private Vector2 acceleration;//加速度

    private STATE state;

    private TextureRegion staticRegion;
    private TextureRegion[] moveRegions;
    private TextureRegion[] attackRegions;

    public enum STATE{          //人物状态
        FALLING, GROUNDED,MOVING
    }

    public AbstractHero(){
        health = 0;
        endurance = 0;
        ragePower = 0;
        attack = 0;
        criticalProbability = 0;
        armor = 0;

        position = new Vector2();
        dimension = new Vector2();
        origin = new Vector2();
        scale = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2(0,Constants.ACCELERATION);

        headright = false;
        isMove = false;
    }

    public void update(float deltaTime){
        updatePosition(deltaTime);

    }

    protected void updatePosition(float deltaTime){
        switch(state){
            case FALLING:
                updateY(deltaTime);
                if(/* 撞到了地面 */) {
                    state = STATE.MOVING;
                    velocity.y = 0;
                }
                break;
            case MOVING:
                if(/*开始FALLing*/){
                    state = STATE.FALLING;
                    break;
                }
                if(endurance > 0 && /* 没到目的地*/ && /* 前方无阻挡物*/){
                    endurance -= Constants.ENDURANCE_COST;
                    updateX(deltaTime);
                }else{
                    state = STATE.GROUNDED;
                }
                break;
            case GROUNDED:
                break;

        }

    }

    private void updateX(float deltaTime){
        position.x += deltaTime * velocity.x;
    }

    private void updateY(float deltaTime){
        velocity.y += deltaTime * acceleration.y;
        position.y += deltaTime * velocity.y;
    }

    public abstract void render(SpriteBatch batch);


    /*
    set 与 put函数
     */
    public  void setVelocity(float VelocityX){
        velocity.x = VelocityX;
    }

    public void setState(int input){
        switch (input){
            case 1:
                velocity.x = /*向右移动？*/? Constants.VELOCITY_X : -Constants.VELOCITY_X;
                state = STATE.MOVING;
                break;
            case 2:
                state = STATE.GROUNDED;
                break;
            case 3:
                state = STATE.FALLING;
                break;
        }
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setEndurance(float endurance) {
        this.endurance = endurance;
    }

    public void setRagePower(float ragePower) {
        this.ragePower = ragePower;
    }

    public void setCriticalProbability(float criticalProbability) {
        this.criticalProbability = criticalProbability;
    }

    public void setAttack(float attack) {
        this.attack = attack;
    }

    public void setArmor(float armor){
        this.armor = armor;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setDirection(boolean direction) {
        this.headright = direction;
    }

    public float getHealth() {
        return health;
    }

    public float getEndurance() {
        return endurance;
    }

    public float getRagePower(){
        return ragePower;
    }

    public float getCriticalProbability() {
        return criticalProbability;
    }

    public float getAttack() {
        return attack;
    }

    public float getArmor() {
        return armor;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isHeadright() {
        return headright;
    }

}
