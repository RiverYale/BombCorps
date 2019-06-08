package com.bombcorps.game.model.heros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.auras.Aura;

public class BaseHero{

    /*
    技能
     */
    protected boolean skill_jump;
    protected boolean skill_1;
    protected boolean skill_2;
    protected boolean skill_3;

    private float level;
    /*
        角色基本属性
     */
    private Rectangle rec;


    private float maxHealth;

    private float health;         //血量
    private float endurance;      //精力
    private float powerRage;      //怒气值

    private float attack;         //攻击力
    private float antiArmor;
    private float criticalProbability;  //暴击几率
    private float criticalRate; //暴击倍率
    private float armor;          //护甲值
    private float decreaseRate;     //减伤率

    /*
    角色其他属性
     */
    private float destination;

    private boolean headright;  //方向  true表示朝右 反之朝左
    private Vector2 position;   //位置
    private Vector2 dimension;  //大小
    private Vector2 origin;     //焦点
    private Vector2 scale;      //缩放
    private Vector2 velocity;   //速度
    private Vector2 acceleration;//加速度

    private STATE state;

    /*
    渲染英雄
     */
    private Array<Aura> aura;

    private float stateTime;
    private int attackTimes;

    private TextureRegion staticRegion;
    private TextureRegion deadRegion;
    private Array<TextureRegion> moveRegions;
    private Array<TextureRegion> attackRegions;

    private TextureRegion moveKeyFrame;
    private  TextureRegion attackKeyFrame;

    private Animation moveAnimation;
    private Animation attackAnimation;

    private enum STATE{          //人物状态
        FALLING, GROUNDED,MOVING,ATTACK,DEAD
    }

    public BaseHero(int hero){
        init();
        initHeroRegion(hero);
    }

    private void initHeroRegion(int hero){
        staticRegion = new TextureRegion();
        deadRegion = new TextureRegion();
        moveRegions = new Array<TextureRegion>();
        attackRegions = new Array<TextureRegion>();

        switch(hero){
            case Constants.ANGEL:
                staticRegion = AssetsController.instance.getRegion("Angel_stand");
                deadRegion = AssetsController.instance.getRegion("Angel_dead");
                moveRegions.add(AssetsController.instance.getRegion("Angel_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move3"));

                attackRegions.add(AssetsController.instance.getRegion("Angel_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Angel_attack1"));
                break;
            case Constants.SPARDA:
                staticRegion = AssetsController.instance.getRegion("Sparda_stand");
                deadRegion = AssetsController.instance.getRegion("Sparda_dead");
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move3"));

                attackRegions.add(AssetsController.instance.getRegion("Sparda_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Sparda_attack1"));
                break;
            case Constants.SNIPER:
                staticRegion = AssetsController.instance.getRegion("Sniper_stand");
                deadRegion = AssetsController.instance.getRegion("Sniper_dead");
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move3"));

                attackRegions.add(AssetsController.instance.getRegion("Sniper_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Sniper_attack1"));
                break;
            case Constants.WIZARD:
                staticRegion = AssetsController.instance.getRegion("Wizard_stand");
                deadRegion = AssetsController.instance.getRegion("Wizard_dead");
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move3"));

                attackRegions.add(AssetsController.instance.getRegion("Wizard_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Wizard_attack1"));
                break;
            case Constants.PROTECTOR:
                staticRegion = AssetsController.instance.getRegion("Protector_stand");
                deadRegion = AssetsController.instance.getRegion("Protector_dead");
                moveRegions.add(AssetsController.instance.getRegion("Protector_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move3"));

                attackRegions.add(AssetsController.instance.getRegion("Protector_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Protector_attack1"));
                break;
        }
        initAnimation();
    }

    private void initAnimation(){
        moveAnimation = new Animation(0.1f, moveRegions);
        attackAnimation = new Animation(0.3f, attackRegions);
    }

    private void init(){
        skill_jump = false;
        skill_1 = false;
        skill_2 = false;
        skill_3 = false;
        this.state = STATE.GROUNDED;

        aura = new Array<Aura>();
        for(int i = 0 ; i < 6 ; i++)
            aura.add(new Aura());

        criticalRate = 2;
        destination = 0;
        health = 0;
        endurance = Constants.MAX_ENDURENCE;
        powerRage = 0;
        attack = 0;
        criticalProbability = 0;
        armor = 0;
        antiArmor = 0;

        position = new Vector2();
        dimension = Constants.HERO_DIMENSION;
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        scale = new Vector2(1,1);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0,Constants.ACCELERATION);
        rec = new Rectangle(0,0,dimension.x,dimension.y);

        headright = false;
        stateTime = 0;
        attackTimes = 1;
        decreaseRate = 0f;

    }



    public void update(float deltaTime){
        updatePosition(deltaTime);
        for(Aura i : aura)
            i.update(deltaTime, position);

        if(health == 0){
            state = STATE.DEAD;
        }
    }

    protected void updatePosition(float deltaTime){
//        Gdx.app.log("zc", "updateStart");
//        Gdx.app.log("position", "x = "+ position.x + "y = "+ position.y);
//        Gdx.app.log("destination", " " + destination);
        switch(state){
//            case ATTACK:
//            case DEAD:
            case GROUNDED:
                velocity.y = 0;
//                Gdx.app.log("state", "grounded");
//                state = STATE.FALLING;
                break;
            case FALLING:
                updateY(deltaTime);
//                updateX(deltaTime);
//                Gdx.app.log("state", "falling");
                break;
            case MOVING:
                if(endurance > 0 && Math.abs(position.x - destination) > 0.5f){
                    endurance -= Constants.MOVE_ENDURANCE_COST;
//                    Gdx.app.log("state", "moving");
                    updateX(deltaTime);

                    if(endurance < 0)
                        setEndurance(0);
                }else{
                    state = STATE.GROUNDED;
                }
                break;
        }

    }

    protected void updateX(float deltaTime){
        position.x += deltaTime * velocity.x;
    }

    protected void updateY(float deltaTime){
        position.y += deltaTime * velocity.y;
        velocity.y += deltaTime * acceleration.y;
    }

    public void render(SpriteBatch batch){
        for(Aura i : aura)
            i.render(batch);
        renderHero(batch);
    }

    protected void renderHero(SpriteBatch batch){
        switch(state){

            case GROUNDED:
                batch.draw(staticRegion.getTexture(), position.x, position.y, origin.x, origin.y,
                        dimension.x, dimension.y, scale.x, scale.y, 0, staticRegion.getRegionX(),
                        staticRegion.getRegionY(), staticRegion.getRegionWidth(),staticRegion.getRegionHeight(),
                        !headright, false);
                //Gdx.app.log("flipX", " "+ staticRegion.isFlipX());
                break;
            case DEAD:
                batch.draw(deadRegion.getTexture(), position.x, position.y, origin.x, origin.y,
                        dimension.x, dimension.y, scale.x, scale.y, 0, deadRegion.getRegionX(),
                        deadRegion.getRegionY(), deadRegion.getRegionWidth(),deadRegion.getRegionHeight(),
                        !headright, false);
                break;
            case FALLING:
            case MOVING:
                moveKeyFrame = (TextureRegion) moveAnimation.getKeyFrame(stateTime);
                stateTime += Gdx.graphics.getDeltaTime();
                stateTime %= moveAnimation.getAnimationDuration();

                batch.draw(moveKeyFrame.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                        scale.x, scale.y,0,moveKeyFrame.getRegionX(),moveKeyFrame.getRegionY(),moveKeyFrame.getRegionWidth(),
                        moveKeyFrame.getRegionHeight(),!headright,false);
                break;
            case ATTACK:
                attackKeyFrame = (TextureRegion) attackAnimation.getKeyFrame(stateTime);
                stateTime += Gdx.graphics.getDeltaTime();

                batch.draw(attackKeyFrame.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                        scale.x, scale.y, 0, attackKeyFrame.getRegionX(), attackKeyFrame.getRegionY(), attackKeyFrame.getRegionWidth(),
                        attackKeyFrame.getRegionHeight(),!headright, false);

                if(stateTime > attackAnimation.getAnimationDuration()){
                    attackTimes--;
                    state = STATE.GROUNDED;
                    if(attackTimes > 0){
                        endurance = Constants.MAX_ENDURENCE / 2;
                    }
                }
                break;
        }

    }

    public void setskill_jump(){
        if(!skill_1 && !skill_2 && !skill_3 && endurance >= 100) {
            endurance -= 100;
            skill_jump = true;
        }
    }

    public boolean getSkill_jump(){
        return skill_jump;
    }

    public boolean getSkill_1() {
        return skill_1;
    }


    public boolean getSkill_2() {
        return skill_2;
    }


    public boolean getSkill_3() {
        return skill_3;
    }

    public void setSkill_jump(boolean skill_jump) {
        this.skill_jump = skill_jump;
    }

    public void setSkill_1(boolean skill_1) {
        this.skill_1 = skill_1;
    }

    public void setSkill_2(boolean skill_2) {
        this.skill_2 = skill_2;
    }

    public void setSkill_3(boolean skill_3) {
        this.skill_3 = skill_3;
    }

    /*
    set 与 put函数
     */

    public float getCriticalRate() {
        return criticalRate;
    }

    public void setCriticalRate(float criticalRate) {
        this.criticalRate = criticalRate;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setLevel(float level){
        this.level = level;
    }

    public float getLevel(){
        return level;
    }

    public Array<Aura> getAura(){
        return aura;
    }

    public float getDestination() {
        return destination;
    }

    public void setDestination(float destination){
        if(destination > position.x){
            headright = true;
            velocity.x = Constants.VELOCITY_X;
        }else{
            headright = false;
            velocity.x = -Constants.VELOCITY_X;
        }

        this.destination = destination;
    }

    public Rectangle getRec() {
        rec.x = position.x;
        rec.y = position.y;

        return rec;
    }

    public void setAttackTimes(int attackTimes){
        this.attackTimes = attackTimes;
    }

    public  void setVelocity(float VelocityX){
        velocity.x = VelocityX;
    }

    public boolean isDead(){
        return state == STATE.DEAD;
    }

    public int getState(){
        switch (state){
            case ATTACK:
                return Constants.STATE_ATTACK;
            case MOVING:
                return Constants.STATE_MOVING;
            case FALLING:
                return Constants.STATE_FALLING;
            case DEAD:
                return Constants.STATE_DEAD;
            default:
                return Constants.STATE_GROUNDED;
        }
    }

    public void setState(int input){
        switch (input){
            case Constants.STATE_MOVING:
//                velocity.x = headright? Constants.VELOCITY_X : -Constants.VELOCITY_X;
                state = STATE.MOVING;
                break;
            case Constants.STATE_GROUNDED:
                state = STATE.GROUNDED;
                break;
            case Constants.STATE_FALLING:
                state = STATE.FALLING;
                break;
            case Constants.STATE_DEAD:
                state = STATE.DEAD;
                break;
            case Constants.STATE_ATTACK:
                state = STATE.ATTACK;
                break;
//            case Constants.STATE_WAIT:
//                state = STATE.WAIT;
//                break;
        }
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setEndurance(float endurance) {
        this.endurance = endurance;
    }

    public void setRagePower(float ragePower) {
        this.powerRage = ragePower;
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

    public int getAttackTimes() {
        return attackTimes;
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
        return powerRage;
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

    public float getAntiArmor() {
        return antiArmor;
    }

    public void setAntiArmor(float antiArmor) {
        this.antiArmor = antiArmor;
    }

    public float getDecreaseRate() {
        return decreaseRate;
    }

    public void setDecreaseRate(float decreaseRate) {
        this.decreaseRate = decreaseRate;
    }
}
