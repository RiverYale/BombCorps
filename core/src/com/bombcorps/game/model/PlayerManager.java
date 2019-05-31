package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.heros.Angel;
import com.bombcorps.game.model.heros.BaseHero;
import com.bombcorps.game.model.heros.Protector;
import com.bombcorps.game.model.heros.Sniper;
import com.bombcorps.game.model.heros.Sparda;
import com.bombcorps.game.model.heros.Wizard;
import com.sun.jndi.cosnaming.CNCtx;

public class PlayerManager {
    public class Buff {
        public Array<Integer> blue_angel_skill_3_leftRound = new Array<Integer>();
        public Array<Integer> red_angel_skill_3_leftRound = new Array<Integer>();
        public int[] blueTeam_angel_skill_2_buff = new int[playerListBlue.size];
        public int[] redTeam_angel_skill_2_buff = new int[playerListRed.size];

    }
    private Buff buff;
    private SkillAndBuff skillAndBuff;

    private Array<Player> playerListRed;
    private Array<Player> playerListBlue;



    private Bomb bomb;   //TODO

    public PlayerManager(){
        playerListRed = new Array<Player>();
        playerListBlue = new Array<Player>();
    }

    public void addPlayer(String IP, int team, String ID){
        Player player = new Player(ID);
        player.setTeam(team);
        player.setIp(IP);

        if(team == Constants.PLAYER.RED_TEAM)
            playerListRed.add(player);
        else
            playerListBlue.add(player);
    }

    public void deletePlayerAtIndex(int index, int side){
        if(side == Constants.PLAYER.BLUE_TEAM) {
            if (index < playerListRed.size)
                playerListRed.removeIndex(index);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
        }
        else {
            if (index < playerListBlue.size)
                playerListBlue.removeIndex(index);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
        }

    }

    public void gameBegin(){
        bomb = new Bomb();
//        buff = new Buff();
        skillAndBuff = new SkillAndBuff(playerListBlue, playerListRed);

        for(Player i : playerListRed)
            i.creatHero(skillAndBuff, bomb);
        for(Player i : playerListBlue)
            i.creatHero(skillAndBuff, bomb);
    }

    public void update(float deltaTime){
        for(Player i : playerListRed)
            i.getMyHero().update(deltaTime);

        for(Player i : playerListBlue)
            i.getMyHero().update(deltaTime);
    }

    public void render(SpriteBatch batch){
        for(Player i : playerListRed)
            i.getMyHero().render(batch);

        for(Player i : playerListBlue)
            i.getMyHero().render(batch);
    }

    public void initGameEveryRound(){
        BaseHero tempHero;
        Array<Player> list = null;
        for(int j = 0 ; j < 2 ; j++) {
            if (j == 0)
                list = playerListBlue;
            else
                list = playerListRed;

            for (Player i : list) {
                tempHero = i.getMyHero();

                tempHero.setState(Constants.STATE_WAIT);
                tempHero.setAttackTimes(1);
                tempHero.setEndurance(Constants.MAX_ENDURENCE);

                tempHero.setRagePower(MathUtils.clamp(i.getMyHero().getRagePower() + 20, 0, 200));
//            i.getMyHero().set
            /*
            skill todo
             */
                initAngel(tempHero, list, j);

            }

        }
        initBuff();

    }

    public void initAngel(BaseHero tempHero, Array<Player> list, int j){

        if(Angel.class.isInstance(tempHero)){
            tempHero.setHealth(MathUtils.clamp(tempHero.getHealth() + Constants.Angel.SKILL_0_HEALTH_PER_ROUND_ADD,
                    0,tempHero.getMaxHealth()));

            if(j == 0){
                if(tempHero.getSkill_3()){
                    tempHero.setSkill_3(false);
                    buff.blue_angel_skill_3_leftRound.add(3);
                }
            }else{
                if(tempHero.getSkill_3()){
                    tempHero.setSkill_3(false);
                    buff.red_angel_skill_3_leftRound.add(3);
                }
            }

        }
    }

    public void initBuff(){
        initSkill_2_buff();
        initAngelSkill_3_Buff();
    }

    public void initSkill_2_buff(){
        for(int i = 0 ; i < buff.blueTeam_angel_skill_2_buff.length ; i++){
            if(buff.blueTeam_angel_skill_2_buff[i] > 0){
                buff.blueTeam_angel_skill_2_buff[i]--;
            }else if(buff.blueTeam_angel_skill_2_buff[i] == 0){
                buff.blueTeam_angel_skill_2_buff[i]--;
                playerListBlue.get(i).getMyHero().setAttack(playerListBlue.get(i).getMyHero().getAttack() + 50);
                playerListBlue.get(i).getMyHero().setEndurance(200);
            }
        }
    }

    private void initsideAngelSkil_3_Buff(Array<Integer> skilllist, Array<Player> playerList){
        for(int i = 0 ; i < skilllist.size ; i++){
            if(skilllist.get(i) == 3){
                for(Player k : playerList){
                    if(!k.getMyHero().isDead())
                        k.getMyHero().setHealth(MathUtils.clamp(k.getMyHero().getHealth() + 500,
                                0, k.getMyHero().getMaxHealth()));

                    addAttack(k);
                }
            }

            skilllist.set(i,skilllist.get(i) - 1);

            if(skilllist.get(i) == 0){
                skilllist.removeIndex(i);
                for(Player k : playerList){
                    minAttack(k);
                }
            }
        }

    }

    public void initAngelSkill_3_Buff(){
        initsideAngelSkil_3_Buff(buff.blue_angel_skill_3_leftRound, playerListBlue);
        initsideAngelSkil_3_Buff(buff.red_angel_skill_3_leftRound, playerListRed);
    }



    public void setAngelSkill_2_DeBuffFor(int team, int index){
        if(team == Constants.PLAYER.BLUE_TEAM){
            buff.blueTeam_angel_skill_2_buff[index] = 2;
            playerListBlue.get(index).getMyHero().setEndurance(150);
            playerListBlue.get(index).getMyHero().setAttack(playerListBlue.get(index).getMyHero().getAttack() - 50);
        }else{
            buff.redTeam_angel_skill_2_buff[index] = 2;
            playerListRed.get(index).getMyHero().setEndurance(150);
            playerListRed.get(index).getMyHero().setAttack(playerListRed.get(index).getMyHero().getAttack() - 50);
        }
    }

    public void setAngelSkill_3_Buff(int team, int index){
        if(team == Constants.PLAYER.BLUE_TEAM){
            playerListBlue.get(index).getMyHero()
        }
    }

    private void minAttack(Player k){
        if(Angel.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Angel.ATTACK * 0.1f);
        if(Protector.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Protector.ATTACK * 0.1f);
        if(Sniper.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Sniper.ATTACK * 0.1f);
        if(Wizard.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Wizard.ATTACK * 0.1f);
        if(Sparda.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Sparda.ATTACK * 0.1f);
    }

    private void addAttack(Player k){
        if(Angel.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Angel.ATTACK * 0.1f);
        if(Protector.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Protector.ATTACK * 0.1f);
        if(Sniper.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Sniper.ATTACK * 0.1f);
        if(Wizard.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Wizard.ATTACK * 0.1f);
        if(Sparda.class.isInstance(k))
            k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Sparda.ATTACK * 0.1f);
    }

    public Array<Player> getAllPlayerList(){
        Array<Player> list = new Array<Player>();
        list.addAll(playerListRed);
        list.addAll(playerListBlue);

        return list;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setPlayerReadyAtIndex(int index, boolean ready, int team){
        if(team == Constants.PLAYER.RED_TEAM) {
            if (index < playerListRed.size)
                playerListRed.get(index).setReady(ready);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerReadyAtIndex\"");
        }
        else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setReady(ready);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerReadyAtIndex\"");
        }
    }

    public boolean getPlayerReadyAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM) {
            if (index < playerListRed.size)
                return playerListRed.get(index).getReady();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerReadyAtIndex\"");
            return false;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getReady();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerReadyAtIndex\"");
            return false;
        }

    }


    public void setPlayerHeroTypeAtIndex(int index, int heroType, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setHeroType(heroType);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerHeroTypeAtIndex\"");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setHeroType(heroType);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerHeroTypeAtIndex\"");
        }
    }

    public int getPlayerHeroTypeAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getHeroType();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerHeroTypeAtIndex\"");
            return -1;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getHeroType();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerHeroTypeAtIndex\"");
            return -1;
        }
    }

    public void changePlayerSideAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size){
                Player temp = playerListRed.removeIndex(index);
                temp.setTeam(Constants.PLAYER.BLUE_TEAM);
                playerListBlue.add(temp);
            }else{
                Gdx.app.error("Out of Bounds : ", "playerList in function \"setPlayerSideAtIndex\"");
            }
        }else{
            if(index < playerListBlue.size){
                Player temp = playerListBlue.removeIndex(index);
                temp.setTeam(Constants.PLAYER.RED_TEAM);
                playerListRed.add(temp);
            }else{
                Gdx.app.error("Out of Bounds : ", "playerList in function \"setPlayerSideAtIndex\"");
            }

        }

    }

    public Array<Player> getPlayerListRed() {
        return playerListRed;
    }

    public Array<Player> getPlayerListBlue() {
        return playerListBlue;
    }

    public void setPlayerStateAtIndex(int index, int state, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setState(state);
            else
                Gdx.app.error("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setState(state);
            else
                Gdx.app.error("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
        }

    }

    public int getPlayerStateAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getState();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
            return -1;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getState();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
            return -1;
        }
    }

    public String getPlayerIpAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getIp();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
            return null;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getIp();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
            return null;
        }
    }

    public void setPlayerPortAtIndex(int index, String IP, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setIp(IP);
            else
                Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerIpAtIndex\" ");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setIp(IP);
            else
                Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerIpAtIndex\" ");
        }
    }
}
