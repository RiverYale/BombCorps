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
    private int health;         //血量
    private int endurance;      //精力
    private int ragePower;      //怒气值

    private int attack;         //攻击力
    private float criticalProbability;  //暴击几率
    private int armor;          //护甲值

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
        ATTACKING, FALLING, GROUNDED,MOVING
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

    }

    protected void updatePosition(float deltaTime){
        switch(state){
            case FALLING:
                velocity.y += acceleration.y * deltaTime;
                position.y -= velocity.y * deltaTime;
                if(/* 撞到了地面 */) {
                    state = STATE.GROUNDED;
                }
                break;
            case MOVING:
                if(endurance > 0)
                    position.x += velocity.x * deltaTime;
                break;
            case GROUNDED:
            case ATTACKING:

        }

        updateX(deltaTime);
        updateY(deltaTime);
    }

    private void updateX(float deltaTime){
        position.x += deltaTime * velocity.x;
    }

    private void updateY(float deltaTime){
        position.y += deltaTime * velocity.y;
    }

    public abstract void render(SpriteBatch batch);


    /*
    set 与 put函数
     */
    public void setVelocity(float velocityX, float )

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public void setRagePower(int ragePower) {
        this.ragePower = ragePower;
    }

    public void setCriticalProbability(float criticalProbability) {
        this.criticalProbability = criticalProbability;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setArmor(int armor){
        this.armor = armor;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setDirection(boolean direction) {
        this.headright = direction;
    }

    public int getHealth() {
        return health;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getRagePower(){
        return ragePower;
    }

    public float getCriticalProbability() {
        return criticalProbability;
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isHeadright() {
        return headright;
    }

}
