package com.bombcorps.game.model.heros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;

public abstract class AbstractHero {
    /*
        角色基本属性
     */
    private float health;         //血量
    private float endurance;      //精力
    private float powerRage;      //怒气值

    private float attack;         //攻击力
    private float antiArmor;
    private float criticalProbability;  //暴击几率
    private float armor;          //护甲值

    /*
    角色其他属性
     */
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
    private float stateTime;
    private int attackTimes;

    private TextureRegion staticRegion;
    private TextureRegion deadRegion;
    private Array<TextureRegion> moveRegions;
//    private TextureRegion[] moveRegions;
    private Array<TextureRegion> attackRegions;
//    private TextureRegion[] attackRegions;

    private Animation moveAnimation;
    private Animation attackAnimation;

    public enum STATE{          //人物状态
        FALLING, GROUNDED,MOVING,ATTACK,DEAD
    }

    public AbstractHero(int hero){
        init();
        initHeroRegion(hero);
    }

    private void initHeroRegion(int hero){
        staticRegion = new TextureRegion();
        deadRegion = new TextureRegion();
        moveRegions = new Array<TextureRegion>();
//        moveRegions = new TextureRegion[4];
        attackRegions = new Array<TextureRegion>();
//        attackRegions = new TextureRegion[2];

        switch(hero){
            case Constants.ANGEL:
                staticRegion = AssetsController.instance.getRegion("Angel_stand");
                deadRegion = AssetsController.instance.getRegion("Angel_dead");
                moveRegions.add(AssetsController.instance.getRegion("Angel_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Angel_move3"));
//                moveRegions[0] = AssetsController.instance.getRegion("Angel_move0");
//                moveRegions[1] = AssetsController.instance.getRegion("Angel_move1");
//                moveRegions[2] = AssetsController.instance.getRegion("Angel_move2");
//                moveRegions[3] = AssetsController.instance.getRegion("Angel_move3");

                attackRegions.add(AssetsController.instance.getRegion("Angel_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Angel_attack1"));
//                attackRegions[0] = AssetsController.instance.getRegion("Angel_attack0");
//                attackRegions[1] = AssetsController.instance.getRegion("Angel_attack1");
                break;
            case Constants.SPARDA:
                staticRegion = AssetsController.instance.getRegion("Sparda_stand");
                deadRegion = AssetsController.instance.getRegion("Sparda_dead");
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Sparda_move3"));
//                moveRegions[0] = AssetsController.instance.getRegion("Sparda_move0");
//                moveRegions[1] = AssetsController.instance.getRegion("Sparda_move1");
//                moveRegions[2] = AssetsController.instance.getRegion("Sparda_move2");
//                moveRegions[3] = AssetsController.instance.getRegion("Sparda_move3");

                attackRegions.add(AssetsController.instance.getRegion("Sparda_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Sparda_attack1"));
//                attackRegions[0] = AssetsController.instance.getRegion("Sparda_attack0");
//                attackRegions[1] = AssetsController.instance.getRegion("Sparda_attack1");
                break;
            case Constants.SNIPER:
                staticRegion = AssetsController.instance.getRegion("Sniper_stand");
                deadRegion = AssetsController.instance.getRegion("Sniper_dead");
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Sniper_move3"));
//                moveRegions[0] = AssetsController.instance.getRegion("Sniper_move0");
//                moveRegions[1] = AssetsController.instance.getRegion("Sniper_move1");
//                moveRegions[2] = AssetsController.instance.getRegion("Sniper_move2");
//                moveRegions[3] = AssetsController.instance.getRegion("Sniper_move3");

                attackRegions.add(AssetsController.instance.getRegion("Sniper_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Sniper_attack1"));
//                attackRegions[0] = AssetsController.instance.getRegion("Sniper_attack0");
//                attackRegions[1] = AssetsController.instance.getRegion("Sniper_attack1");
                break;
            case Constants.WIZARD:
                staticRegion = AssetsController.instance.getRegion("Wizard_stand");
                deadRegion = AssetsController.instance.getRegion("Wizard_dead");
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Wizard_move3"));
//                moveRegions[0] = AssetsController.instance.getRegion("Wizard_move0");
//                moveRegions[1] = AssetsController.instance.getRegion("Wizard_move1");
//                moveRegions[2] = AssetsController.instance.getRegion("Wizard_move2");
//                moveRegions[3] = AssetsController.instance.getRegion("Wizard_move3");

                attackRegions.add(AssetsController.instance.getRegion("Wizard_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Wizard_attack1"));
//                attackRegions[0] = AssetsController.instance.getRegion("Wizard_attack0");
//                attackRegions[1] = AssetsController.instance.getRegion("Wizard_attack1");
                break;
            case Constants.PROTECTOR:
                staticRegion = AssetsController.instance.getRegion("Protector_stand");
                deadRegion = AssetsController.instance.getRegion("Protector_dead");
                moveRegions.add(AssetsController.instance.getRegion("Protector_move0"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move1"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move2"));
                moveRegions.add(AssetsController.instance.getRegion("Protector_move3"));
//                moveRegions[0] = AssetsController.instance.getRegion("Protector_move0");
//                moveRegions[1] = AssetsController.instance.getRegion("Protector_move1");
//                moveRegions[2] = AssetsController.instance.getRegion("Protector_move2");
//                moveRegions[3] = AssetsController.instance.getRegion("Protector_move3");

                attackRegions.add(AssetsController.instance.getRegion("Protector_attack0"));
                attackRegions.add(AssetsController.instance.getRegion("Protector_attack1"));
//                attackRegions[0] = AssetsController.instance.getRegion("Protector_attack0");
//                attackRegions[1] = AssetsController.instance.getRegion("Protector_attack1");
                break;
        }
        initAnimation();
    }

    private void initAnimation(){
        moveAnimation = new Animation(0.1f, moveRegions);
        attackAnimation = new Animation(0.3f, attackRegions);
    }

    private void init(){
        health = 0;
        endurance = Constants.MAX_ENDURENCE;
        powerRage = Constants.MAX_RAGEPOWER;
        attack = 0;
        criticalProbability = 0;
        armor = 0;
        antiArmor = 0;

        position = new Vector2();
        dimension = new Vector2();
        origin = new Vector2();
        scale = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2(0,Constants.ACCELERATION);

        headright = false;
        stateTime = 0;
        attackTimes = 1;
    }



    public void update(float deltaTime){
        updatePosition(deltaTime);

    }

    protected void updatePosition(float deltaTime){
        switch(state){
            case ATTACK:
            case DEAD:
            case GROUNDED:
                velocity.y = 0;
                state = STATE.FALLING;
                break;
            case FALLING:
                updateY(deltaTime);
                break;
            case MOVING:
                //TODO
                float destination = 5;      //目的地的x

                if(endurance > 0 && Math.abs(position.x - destination) > 5){
                    endurance -= Constants.MOVE_ENDURANCE_COST;
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
        renderHero(batch);
    };

    protected void renderHero(SpriteBatch batch){

        switch(state){
            case FALLING:
            case GROUNDED:
                batch.draw(staticRegion.getTexture(), position.x, position.y, origin.x, origin.y,
                        dimension.x, dimension.y, scale.x, scale.y, 0, staticRegion.getRegionX(),
                        staticRegion.getRegionY(), staticRegion.getRegionWidth(),staticRegion.getRegionHeight(),
                        !headright, false);
                break;
            case DEAD:
                batch.draw(deadRegion.getTexture(), position.x, position.y, origin.x, origin.y,
                        dimension.x, dimension.y, scale.x, scale.y, 0, deadRegion.getRegionX(),
                        deadRegion.getRegionY(), deadRegion.getRegionWidth(),deadRegion.getRegionHeight(),
                        !headright, false);
                break;
            case MOVING:
                TextureRegion moveFrame = (TextureRegion) moveAnimation.getKeyFrame(stateTime);
                stateTime += Gdx.graphics.getDeltaTime();
                stateTime %= moveAnimation.getAnimationDuration();

                batch.draw(moveFrame, position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                        scale.x, scale.y, 0);
                break;
            case ATTACK:
                TextureRegion attackFrame = (TextureRegion) attackAnimation.getKeyFrame(stateTime);
                stateTime += Gdx.graphics.getDeltaTime();

                batch.draw(attackFrame, position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                        scale.x, scale.y, 0);

                if(stateTime > attackAnimation.getAnimationDuration()){
                    attackTimes--;
                    if(attackTimes > 0){
                        stateTime -= attackAnimation.getAnimationDuration();
                    }else {
                        state = STATE.GROUNDED;
                    }
                }
                break;
        }

    }


    /*
    set 与 put函数
     */
    public void setAttackTimes(int attackTimes){
        this.attackTimes = attackTimes;
    }

    public  void setVelocity(float VelocityX){
        velocity.x = VelocityX;
    }

    public void setState(int input){
        switch (input){
            case 1:
                velocity.x = headright? Constants.VELOCITY_X : -Constants.VELOCITY_X;
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

}
