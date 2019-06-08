package com.bombcorps.game.model.bombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;

public class Bomb {

    Player fromPlayer;
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

    private Vector2 velocity;
    private Vector2 acceleration;
    private float rotation;
    private Rectangle rec;
    private Animation boomAnimation;
    private TextureRegion boomKeyFrame;
    private int stateTime;

    private STATE state;
    private enum STATE{
        FLY, BOOM, WAIT, READY
    }

    public Bomb(){
        init();
    }

    public void init(){
        state = STATE.WAIT;

        stateTime = 0;
        heroType = 0;
        bombType = 0;

        initBomb();
        initBoom();

        routeScale = Constants.BOMB.ROUTESCALE;
        dimension = Constants.BOMB.DIMENSION;
        bombScale = Constants.BOMB.BOMBSCALE;
        position = new Vector2(0,0);
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        velocity = new Vector2();
        acceleration = new Vector2(0,Constants.BOMB.ACCELERATION);
        rotation = 0;

        rec = new Rectangle(0,0,dimension.x,dimension.y);
    }

    public void update(float deltaTime){
        if(state == STATE.WAIT){
            Gdx.app.log("bombState","wait");
        }else if(state == STATE.READY){
            Gdx.app.log("bombState", "ready");
        }else{
            Gdx.app.log("bombState","fly");
        }

        switch (state){
            case FLY:
                updateRotation(deltaTime);
                updateVelocity(deltaTime);
                updatePosition(deltaTime);
                break;
        }
    }

    public void initBombEveryChange(){
        state = STATE.WAIT;
        bombType = 0;
    }

    private void initBomb(){
        bomb = new TextureRegion[6][3];
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
        bomb[5][0] = AssetsController.instance.getRegion("heroFly");
    }

    private void initBoom(){
        boomDimension = Constants.BOMB.BOOM_DIMENSION;
        boomOrigin = new Vector2(boomDimension.x / 2, boomDimension.y / 2);
        boomScale = Constants.BOMB.BOOM_SCALE;

        boom = new TextureRegion[6][3][4];
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
        temp = "WizardBoom";
        for(int i = 0 ; i < 4 ; i++){
            boom[4][0][i] = AssetsController.instance.getRegion(temp + i);
        }
        temp = "heroAppear";
        for(int i = 0 ; i < 4 ; i++){
            boom[5][0][i] = AssetsController.instance.getRegion(temp + i);
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
                break;
            case BOOM:
                renderBoom(batch);
                break;
            case READY:
                renderReady(batch);
                break;
        }

    }

    public void renderReady(SpriteBatch batch){
        batch.draw(bomb[heroType][bombType], position.x - dimension.x, position.y,
                origin.x,origin.y,dimension.x,dimension.y,
                bombScale.x,bombScale.y,0);

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

    public void explode(Array<Player> playerListRed, Array<Player> playerListBlue){
        Array<Player> playersBeingHit = playersBeingHit(playerListRed, playerListBlue);  //被炸到的玩家

//        if(playersBeingHit.size > 0) {
            switch (heroType) {
                case Constants.ANGEL:
                    switch (bombType) {
                        case 0:
                            handlePlayerDamage(playersBeingHit,playerListRed, playerListBlue );
                            break;
                        case 1:
                            for(Player i : playersBeingHit){
                                i.getMyHero().setHealth(MathUtils.clamp(i.getMyHero().getHealth() +
                                                Constants.Angel.SKILL_1_HEALTH_ADD
                                        , 0, i.getMyHero().getMaxHealth()));
                            }
                            break;
                        case 2:
                            /*
                            TODO 虚弱
                             */
                            for(Player i : playersBeingHit){
                                i.getMyHero().getAura().get(2).setState(Constants.AURA.ANGELAURA);  //虚弱光环
                                if(i.getTeam() == Constants.PLAYER.RED_TEAM){
                                    for(int k = 0 ; k < playerListRed.size ; k++){
                                        if(i.getIp().equals(playerListRed.get(k).getIp())){
                                            i.skillAndBuff.redBuffs.get(k).angel_skill_2_debuff = Constants.Angel.SKILL_2_ROUND_NUM;
                                            i.getMyHero().setAttack(i.getMyHero().getAttack() - Constants.Angel.SKILL_2_ATTACK_MIN);
                                            i.getMyHero().setEndurance(MathUtils.clamp(i.getMyHero().getEndurance() -
                                                            Constants.Angel.SKILL_2_ENDURANCE_MIN
                                                            ,0, Constants.MAX_ENDURENCE));
                                            break;
                                        }
                                    }
                                }else{
                                    for(int k = 0 ; k < playerListBlue.size ; k++){
                                        if(i.getIp().equals(playerListBlue.get(k).getIp())){
                                            i.skillAndBuff.blueBuffs.get(k).angel_skill_2_debuff = Constants.Angel.SKILL_2_ROUND_NUM;
                                            i.getMyHero().setAttack(i.getMyHero().getAttack() - Constants.Angel.SKILL_2_ATTACK_MIN);
                                            i.getMyHero().setEndurance(MathUtils.clamp(i.getMyHero().getEndurance() -
                                                            Constants.Angel.SKILL_2_ENDURANCE_MIN
                                                    ,0, Constants.MAX_ENDURENCE));
                                            break;
                                        }
                                    }
                                }
                            }

                            break;

                    }
                    break;

                case Constants.SPARDA:
                    switch (bombType){
                        case 0:
                        case 1:
                            fromPlayer.getMyHero().setHealth(MathUtils.clamp(fromPlayer.getMyHero().getHealth() +
                                            damageOutput(fromPlayer.getMyHero().getAttack()) * playersBeingHit.size *
                                                    Constants.Sparda.SKILL_0_HEALTH_PERCENTAGE_ADD,
                                    0 , fromPlayer.getMyHero().getMaxHealth()));
                            handlePlayerDamage(playersBeingHit,playerListRed, playerListBlue );
                            break;
                    }
                    break;
                case Constants.SNIPER:
                    switch (bombType){
                        case 0:
                        case 1:
                            boolean skill3On = false;
                            if(fromPlayer.getTeam() == Constants.PLAYER.RED_TEAM){
                                for(int i = 0 ; i < playerListRed.size ; i++){
                                    if(fromPlayer.getIp().equals(playerListRed.get(i).getIp())){
                                        if(fromPlayer.skillAndBuff.redBuffs.get(i).sniper_skill_3_buff > 0){
                                            skill3On = true;
                                        }
                                    }
                                }
                            }else{
                                for(int i = 0 ; i < playerListBlue.size ; i++){
                                    if(fromPlayer.getIp().equals(playerListBlue.get(i).getIp())){
                                        if(fromPlayer.skillAndBuff.blueBuffs.get(i).sniper_skill_3_buff > 0){
                                            skill3On = true;
                                        }
                                    }
                                }
                            }
                            if(playersBeingHit.size > 0 && !skill3On){
                                fromPlayer.getMyHero().setCriticalProbability(fromPlayer.getMyHero().getCriticalProbability() / 2);
                                handlePlayerDamage(playersBeingHit,playerListRed, playerListBlue );
                            }else if(playersBeingHit.size == 0){
                                fromPlayer.getMyHero().setCriticalProbability(MathUtils.clamp(
                                        fromPlayer.getMyHero().getCriticalProbability() * 2,
                                        0,1));
                            }else{
                                handlePlayerDamage(playersBeingHit,playerListRed,playerListBlue);
                            }
                            break;
                    }
                    break;
                case Constants.PROTECTOR:
                    switch(bombType){
                        case 0:
                            handlePlayerDamage(playersBeingHit,playerListRed, playerListBlue);
                            break;
                        case 1:
                            /*
                            TODO
                             */
                            for(Player i : playersBeingHit){
                                i.getMyHero().setArmor(i.getMyHero().getArmor() + Constants.Protector.SKILL_2_ARMOR_ADD);

                                if(i.getTeam() == Constants.PLAYER.RED_TEAM){
                                    for(int k = 0 ; k < playerListRed.size ; k++){
                                        if(i.getIp().equals(playerListRed.get(k).getIp())){
                                            i.skillAndBuff.redBuffs.get(k).protector_skill_2_buff.add(Constants.Protector.SKILL_2_ROUND);
                                            break;
                                        }
                                    }
                                }else{
                                    for(int k = 0 ; k < playerListBlue.size ; k++){
                                        if(i.getIp().equals(playerListBlue.get(k).getIp())){
                                            i.skillAndBuff.blueBuffs.get(k).protector_skill_2_buff.add(Constants.Protector.SKILL_2_ROUND);
                                            break;
                                        }
                                    }
                                }
                            }

                            break;
                    }
                    break;
                case Constants.WIZARD:
                    switch (bombType){
                        case 0:
                            for(Player i : playersBeingHit){
                                i.getMyHero().getAura().get(4).setState(Constants.AURA.WIZARDAURA0);

                                if(i.getTeam() == Constants.PLAYER.RED_TEAM){
                                    for(int k = 0 ; k < playerListRed.size ; k++){
                                        if(i.getIp().equals(playerListRed.get(k).getIp())){

                                            //fromplayer的技能1是否激活
                                            if(!fromPlayer.skillAndBuff.wizardSkill.skill_1)
                                                i.skillAndBuff.redBuffs.get(k).wizard_skill_0_debuff++;
                                            else
                                                i.skillAndBuff.redBuffs.get(k).wizard_skill_0_debuff += 3;
                                            break;
                                        }
                                    }
                                }else{
                                    for(int k = 0 ; k < playerListBlue.size ; k++){
                                        if(i.getIp().equals(playerListBlue.get(k).getIp())){
                                            i.skillAndBuff.blueBuffs.get(k).wizard_skill_2_debuff = (int)Constants.Wizard.SKILL_2_SEAL_ROUND;
                                            break;
                                        }
                                    }
                                }
                            }

                            break;
                        case 1:
                            /*
                            TODO 禁锢
                             */
                            for(Player i : playersBeingHit){
                                i.getMyHero().getAura().get(5).setState(Constants.AURA.WIZARDAURA1);    //设置光环

                                if(i.getTeam() == Constants.PLAYER.RED_TEAM){
                                    for(int k = 0 ; k < playerListRed.size ; k++){
                                        if(i.getIp().equals(playerListRed.get(k).getIp())){
                                            i.skillAndBuff.redBuffs.get(k).wizard_skill_2_debuff = (int)Constants.Wizard.SKILL_2_SEAL_ROUND;
                                            break;
                                        }
                                    }
                                }else{
                                    for(int k = 0 ; k < playerListBlue.size ; k++){
                                        if(i.getIp().equals(playerListBlue.get(k).getIp())){
                                            i.skillAndBuff.blueBuffs.get(k).wizard_skill_2_debuff = (int)Constants.Wizard.SKILL_2_SEAL_ROUND;
                                            break;
                                        }
                                    }
                                }
                            }

                            break;
                    }
                    break;
                case 5:
                    fromPlayer.getMyHero().setPosition(position);
            }

    }

    private Array<Player> playersBeingHit(Array<Player> playerListRed, Array<Player> playerListBlue){   //被炸到的player
        Array<Player> list = new Array<Player>();
        Array<Player> playerList = new Array<Player>();
        playerList.addAll(playerListRed);
        playerList.addAll(playerListBlue);

        for(Player i : playerList){
            if(getRect().overlaps(i.getMyHero().getRec())){
                list.add(i);
            }
        }

        return list;
    }



    private void handlePlayerDamage(Array<Player> playerList,
                                    Array<Player> playerListRed,
                                    Array<Player> playerListBlue){      //扣所有被打到玩家的血
        float attack = fromPlayer.getMyHero().getAttack();

        float damage = damageOutput(attack);

        for(Player i : playerList){

            if(i.getMyHero().getDecreaseRate() == Constants.Protector.SKILL_3_TEAMMATE_DAMAGE_PERCENTAGE) {   //减伤率为40%
                Player protector = null;
                if(i.getTeam() == Constants.PLAYER.RED_TEAM){
                    for(int k = 0 ; k < playerListRed.size ; k++){
                        if(i.getIp().equals(playerListRed.get(k).getIp())){
                            protector = playerListRed.get(i.skillAndBuff.redBuffs.get(k).FromIndex);
                            break;
                        }
                    }
                }else{
                    for(int k = 0 ; k < playerListBlue.size ; k++){
                        if(i.getIp().equals(playerListBlue.get(k).getIp())){
                            protector = playerListBlue.get(i.skillAndBuff.blueBuffs.get(k).FromIndex);
                            break;
                        }
                    }
                }
                setdamagedHealth(protector, damage);
            }

            setdamagedHealth(i,damage);
        }
    }

    private void setdamagedHealth(Player player, float damage){     //根据伤害设置生命值
        damage *= (1 - player.getMyHero().getDecreaseRate());
        damage *= (1 - player.getMyHero().getArmor() / (player.getMyHero().getArmor() + 100));
        player.getMyHero().setHealth(MathUtils.clamp(player.getMyHero().getHealth() - damage, 0, player.getMyHero().getMaxHealth()));
    }

    private float damageOutput(float inputDamage){  //输出伤害
        float damage = inputDamage;
        damage += damage * fromPlayer.getMyHero().getAntiArmor();
        if(Math.random() < fromPlayer.getMyHero().getCriticalProbability()){
            damage *= fromPlayer.getMyHero().getCriticalRate();
        }

        return damage;
    }

    /*
    get   set函数
    */

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public void setFromPlayer(Player fromPlayer) {
        this.fromPlayer = fromPlayer;
    }

    public int getBombType() {
        return bombType;
    }

    public void setBombType(int bombType) {
        this.bombType = bombType;
        int boomType = 0;
        switch(heroType){
            case Constants.PROTECTOR:
            case Constants.ANGEL:
            case Constants.SNIPER:
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

    public int getHeroType() {
        return heroType;
    }

    public void setHeroType(int heroType) {
        this.heroType = heroType;
    }

    public Rectangle getRect() {
        Gdx.app.log("rectangle", rec.height + "");
        rec.x = position.x;
        rec.y = position.y;
        if(rec == null){
            Gdx.app.log("bomb","null");
        }else {
            Gdx.app.log("bomb", "not null");
        }
        return rec;
    }

    public int getState(){
        switch (state){
            case FLY:
                return Constants.BOMB.STATE_FLY;
            case BOOM:
                return Constants.BOMB.STATE_BOOM;
            case READY:
                return Constants.BOMB.STATE_READY;
            default:
                return Constants.BOMB.STATE_WAIT;
        }

    }

    public void setState(int state) {
        switch (state){
            case Constants.BOMB.STATE_FLY:
                this.state = STATE.FLY;
                break;
            case Constants.BOMB.STATE_BOOM:
                stateTime = 0;
                this.state = STATE.BOOM;
                break;
            case Constants.BOMB.STATE_READY:
                this.state = STATE.READY;
                break;
        }
    }
}
