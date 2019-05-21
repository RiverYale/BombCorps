package com.bombcorps.game.model.heros;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

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
    private Vector2 position;   //位置
    private boolean headright;  //方向  true表示朝右 反之朝左

    private TextureRegion staticRegion;
    private TextureRegion[] moveRegions;
    private TextureRegion[] attackRegions;

    public AbstractHero(){
        health = 0;
        endurance = 0;
        ragePower = 0;
        attack = 0;
        criticalProbability = 0;
        armor = 0;

        position = new Vector2();
        headright = false;

        staticRegion = null;
        moveRegions = null;
        attackRegions = null;
    }

    public void update(float deltaTime){

    }

    public abstract void render(SpriteBatch batch);


    /*
    set 与 put函数
     */
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
