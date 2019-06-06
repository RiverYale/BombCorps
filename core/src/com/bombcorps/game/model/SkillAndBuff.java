package com.bombcorps.game.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;

public class SkillAndBuff {

    public AngelSkill angelSkill;
    public SpardaSkill spardaSkill;
    public ProtectorSkill protectorSkill;
    public SniperSkill sniperSkill;
    public WizardSkill wizardSkill;

    public boolean canJump;

    public Array<Player> playerListBlue;
    public Array<Player> playerListRed;

    public Array<Buff> redBuffs;
    public Array<Buff> blueBuffs;


    public SkillAndBuff(Array<Player> playerlistblue, Array<Player> playerListred){
        this.playerListBlue = playerlistblue;
        this.playerListRed = playerListred;
        redBuffs = new Array<Buff>();
        blueBuffs = new Array<Buff>();
        for(int i = 0 ; i < playerListRed.size ; i++){
            redBuffs.add(new Buff(playerListred.get(i)));
        }
        for(int i = 0 ; i < playerlistblue.size ; i++){
            blueBuffs.add(new Buff(playerlistblue.get(i)));
        }
        initSkillEveryChange();
    }

    public void updateBuffEveryRound(){         //每回合调用
        for(Buff i : redBuffs)
            i.updateRoundBuff();
        for(Buff i : blueBuffs)
            i.updateRoundBuff();

    }


    public void initSkillEveryChange(){               //每次操作对象改变，都要调用
        angelSkill = new AngelSkill();
        spardaSkill = new SpardaSkill();
        protectorSkill = new ProtectorSkill();
        sniperSkill = new SniperSkill();
        wizardSkill = new WizardSkill();

        canJump = true;

        for(Buff i : redBuffs)
            i.updateInstantBuff();
        for(Buff i : blueBuffs)
            i.updateInstantBuff();
    }

    /*
    弹射技能
     */
    public void jump(int team, String IP){
        Player player;
        int index = -1;
        if(team == Constants.PLAYER.RED_TEAM){
            index = getindex(playerListRed, IP);
            player = playerListRed.get(index);
        }else{
            index = getindex(playerListBlue, IP);
            player = playerListBlue.get(index);
        }

        if(canJump && player.getMyHero().getEndurance() >= Constants.TRANSPORT_ENDURENCE_COST){
            canJump = false;
            player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.TRANSPORT_ENDURENCE_COST);
            setBomb(player, 5,0);
        }

    }

    public class Buff{
        public Player curPlayer;

        public Buff(Player player){
            curPlayer = player;
        }

        public void updateRoundBuff(){
            //update天使技能3
            for(int i = 0 ; i < angel_skill_3_buff.size ; i++){
                angel_skill_3_buff.set(i,angel_skill_3_buff.get(i) - 1);
                if(angel_skill_3_buff.get(i) == 0){
                    mindamage();
                    angel_skill_3_buff.removeIndex(i);
                    i--;
                }
            }

            //update 天使技能2debuff
            if(angel_skill_2_debuff > 0){
                angel_skill_2_debuff--;
            }

            if(angel_skill_2_debuff == 0){
                curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() + Constants.Angel.SKILL_2_ATTACK_MIN);    //恢复攻击力
                curPlayer.getMyHero().getAura().get(2).setState(Constants.AURA.WAIT);    //取消光环
            }


            //update 天使被动buff
            if(angel_self_buff){
                curPlayer.getMyHero().setHealth(MathUtils.clamp(curPlayer.getMyHero().getHealth() +
                                Constants.Angel.SKILL_0_HEALTH_PER_ROUND_ADD,
                        0, curPlayer.getMyHero().getMaxHealth()));
            }

            //update sparda技能3
            if(sparda_skill_3_buff > 0){
//                curPlayer.getMyHero().getAura().setState(Constants.AURA.WAIT);
                curPlayer.getMyHero().setHealth(MathUtils.clamp(curPlayer.getMyHero().getHealth() +
                                Constants.Sparda.SKILL_3_HEALTH_EACH_ROUND, 0,curPlayer.getMyHero().getMaxHealth()));
                sparda_skill_3_buff--;
            }

            if(sparda_skill_3_buff == 0)
                curPlayer.getMyHero().getAura().get(0).setState(Constants.AURA.WAIT);  //取消光环

            //update protector技能1
            for(int i = 0 ; i < protector_skill_1_buff.size ; i++){
                protector_skill_1_buff.set(i, protector_skill_1_buff.get(i) - 1);
                if(protector_skill_1_buff.get(i) == 0){
                    curPlayer.getMyHero().setArmor(curPlayer.getMyHero().getArmor() - Constants.Protector.SKILL_1_ARMOR_ADD);
                    protector_skill_1_buff.removeIndex(i);
                    i--;
                }
            }

            //update protector技能2
            for(int i = 0 ; i < protector_skill_2_buff.size ; i++){
                protector_skill_2_buff.set(i, protector_skill_2_buff.get(i) - 1);
                if(protector_skill_2_buff.get(i) == 0){
                    curPlayer.getMyHero().setArmor(curPlayer.getMyHero().getArmor() - Constants.Protector.SKILL_2_ARMOR_ADD);
                    protector_skill_2_buff.removeIndex(i);
                    i--;
                }
            }

            //update protector技能3
            if(protector_skill_3_left_round > 1){
                protector_skill_3_left_round--;
            }else if(protector_skill_3_left_round == 1){
//                if(curPlayer.getMyHero().getDecreaseRate() == Constants.Protector.SKILL_3_SELF_DAMAGE_PERCENTAGE){
                curPlayer.getMyHero().getAura().get(1).setState(Constants.AURA.WAIT);   //取消光环

                protector_skill_3_left_round--;
                curPlayer.getMyHero().setDecreaseRate(0);
            }

            //update sniper 技能3
            if(sniper_skill_3_buff > 1){
                sniper_skill_3_buff--;
            }else if(sniper_skill_3_buff == 1){
                sniper_skill_3_buff--;
                curPlayer.getMyHero().getAura().get(3).setState(Constants.AURA.WAIT);   //取消光环
                curPlayer.getMyHero().setCriticalRate(2f);
            }

            //更新wizard skill_0技能
            if(wizard_skill_0_debuff == 0) {
                curPlayer.getMyHero().getAura().get(4).setState(Constants.AURA.WAIT);
            }
            else {
                curPlayer.getMyHero().setHealth(MathUtils.clamp(curPlayer.getMyHero().getHealth() -
                                wizard_skill_0_debuff * Constants.Wizard.SKILL_0_DAMAGE_PER_ROUND_PER_LAYER, 0,
                        curPlayer.getMyHero().getMaxHealth()));
            }

            //更新wizard skill_2禁锢
            if(wizard_skill_2_debuff > 1){
                wizard_skill_2_debuff--;
            }else if(wizard_skill_2_debuff == 1){
                wizard_skill_2_debuff--;
                curPlayer.getMyHero().getAura().get(5).setState(Constants.AURA.WAIT);   //取消光环
            }

        }

        private void updateInstantBuff(){
            //刷新sparda技能一
            for(int i = 0 ; i < sparda_skill_1_buff ; i++){
                curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() - Constants.Sparda.SKILL_1_ATTACK_ADD);
            }

            //更新sparda技能2
            if(sparda_skill_2_buff > 0)
                curPlayer.getMyHero().setCriticalProbability(Constants.Sparda.START_CRITICALPROBABILITY);

            //更新sniper技能1
            if(sniper_skill_1_buff > 0)
                curPlayer.getMyHero().setAntiArmor(0);




        }

        private void mindamage(){       //天使技能3效果消除
            switch (curPlayer.getHeroType()){
                case Constants.ANGEL:
                    curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() -
                            Constants.Angel.ATTACK * Constants.Angel.SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD);
                    break;
                case Constants.PROTECTOR:
                    curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() -
                            Constants.Protector.ATTACK * Constants.Angel.SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD);
                    break;
                case Constants.SPARDA:
                    curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() -
                            Constants.Sparda.ATTACK * Constants.Angel.SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD);
                    break;
                case Constants.SNIPER:
                    curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() -
                            Constants.Sniper.ATTACK * Constants.Angel.SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD);
                    break;
                case Constants.WIZARD:
                    curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() -
                            Constants.Wizard.ATTACK * Constants.Angel.SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD);
                    break;
            }
        }

        public Array<Integer> angel_skill_3_buff;      //增加AttackBUFF
        public int angel_skill_2_debuff;               //虚弱剩余时间
        public boolean angel_self_buff;                //天使自身回血buff

        public int sparda_skill_1_buff;                //增加伤害的数量   instant
        public int sparda_skill_2_buff;                //增加暴击的数量   instant
        public int sparda_skill_3_buff;            //技能3剩余局数

        public Array<Integer> protector_skill_1_buff;  //技能1BUFF数量和剩余ROUND数量
        public Array<Integer> protector_skill_2_buff;    //技能2buff数量与剩余盘数
        //skill_3
        public int protector_skill_3_left_round;              //减伤Buff剩余局数
        public int FromIndex;                          //buff来源

//        int sniper_skill_0_buff;        //是否减少暴击几率

        public int sniper_skill_1_buff;            //技能1增加穿甲的数量
        public int sniper_skill_3_buff;            //技能3剩余局数

        public int wizard_skill_0_debuff;          //debuff层数
        public int wizard_skill_2_debuff;          //技能2禁锢剩余局数

    }

    public class AngelSkill{
        public boolean skill_1;
        public boolean skill_2;
        public boolean skill_3;


        public void useSkill_1(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(!skill_2 && player.getMyHero().getEndurance() >= Constants.Angel.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Angel.SKILL_1_POWERRAGE_COST){
                skill_1 = true;

                setBomb(player, Constants.ANGEL, 1);

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Angel.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Angel.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(!skill_1 && player.getMyHero().getEndurance() >= Constants.Angel.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Angel.SKILL_2_POWERRAGE_COST){
                skill_2 = true;

                setBomb(player, Constants.ANGEL, 2);

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Angel.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Angel.SKILL_2_POWERRAGE_COST);
            }
        }

        public void useSkill_3(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Angel.SKILL_3_ENDURENCE_COST &&
                    player.getMyHero().getRagePower() >= Constants.Angel.SKILL_3_POWERRAGE_COST){

                skill_3 = true;

                if(team == Constants.PLAYER.RED_TEAM) {
                    for (Buff i : redBuffs) {
                        i.angel_skill_3_buff.add(3);
                        for(Player k : playerListRed){
                            k.getMyHero().setHealth(MathUtils.clamp(k.getMyHero().getHealth() +  500f, 0,k.getMyHero().getMaxHealth()));

                            addDamage(k);
                        }
                    }
                } else{
                    for(Buff i : blueBuffs){
                        i.angel_skill_3_buff.add(3);
                        for(Player k : playerListBlue){
                            k.getMyHero().setHealth(MathUtils.clamp(k.getMyHero().getHealth() +  500f, 0,k.getMyHero().getMaxHealth()));

                            addDamage(k);
                        }
                    }
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Angel.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Angel.SKILL_3_POWERRAGE_COST);
            }
        }


        private void addDamage(Player k){
            switch (k.getHeroType()){
                case Constants.ANGEL:
                    k.getMyHero().setAttack(k.getMyHero().getAttack() + 0.1f * Constants.Angel.ATTACK);
                    break;
                case Constants.PROTECTOR:
                    k.getMyHero().setAttack(k.getMyHero().getAttack() + 0.1f * Constants.Protector.ATTACK);
                    break;
                case Constants.SPARDA:
                    k.getMyHero().setAttack(k.getMyHero().getAttack() + 0.1f * Constants.Sparda.ATTACK);
                    break;
                case Constants.SNIPER:
                    k.getMyHero().setAttack(k.getMyHero().getAttack() + 0.1f * Constants.Sniper.ATTACK);
                    break;
                case Constants.WIZARD:
                    k.getMyHero().setAttack(k.getMyHero().getAttack() + 0.1f * Constants.Wizard.ATTACK);
                    break;
            }
        }

    }

    public class SpardaSkill{
        public boolean skill_1;
        public boolean skill_2;
        public boolean skill_3;

        public void useSkill_1(int team, String IP){
            Player player;
            int index;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getHealth() >= Constants.Sparda.SKILL_1_HEALTH_COST){

                skill_1 = true;

                setBomb(player, Constants.SPARDA, 1);

                player.getMyHero().setAttack(player.getMyHero().getAttack() + Constants.Sparda.SKILL_1_ATTACK_ADD);
                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_1_buff++;
                }else{
                    blueBuffs.get(index).sparda_skill_1_buff++;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setHealth(player.getMyHero().getHealth() - Constants.Sparda.SKILL_1_HEALTH_COST);
            }
        }

        public void useSkill_2(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sparda.SKILL_2_RAGEPOWER_COST){

                skill_2 = true;

                setBomb(player, Constants.SPARDA, 1);

                player.getMyHero().setCriticalProbability(MathUtils.clamp(player.getMyHero().getCriticalProbability()
                        + Constants.Sparda.SKILL_2_CRITICALPROBABILITY_ADD, 0, 1));

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_2_buff++;
                }else{
                    blueBuffs.get(index).sparda_skill_2_buff++;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sparda.SKILL_2_RAGEPOWER_COST);
            }
        }

        public void useSkill_3(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_3_ENDURENCE_COST &&
                    player.getMyHero().getRagePower() >= Constants.MAX_RAGEPOWER){
                skill_3 = true;

                player.getMyHero().getAura().get(0).setState(Constants.AURA.SPARDAAURA);      //aura

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_3_buff = Constants.Sparda.SKILL_3_ROUND;
                }else{
                    blueBuffs.get(index).sparda_skill_3_buff = Constants.Sparda.SKILL_3_ROUND;
                }
                player.getMyHero().setHealth(MathUtils.clamp(player.getMyHero().getHealth() +
                                Constants.Sparda.SKILL_3_HEALTH_INSTANT, 0,player.getMyHero().getMaxHealth()));

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sparda.SKILL_3_RAGEPOWER_COST);
            }
        }
    }

    public class ProtectorSkill{
        public boolean skill_1;
        public boolean skill_2;
        public boolean skill_3;

        public void useSkill_1(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Protector.SKILL_1_ENDURENCE_COST &&
                    player.getMyHero().getRagePower() >= Constants.Protector.SKILL_1_POWERRAGE_COST){

                skill_1 = true;


                player.getMyHero().setArmor(player.getMyHero().getArmor() + Constants.Protector.SKILL_1_ARMOR_ADD);

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).protector_skill_1_buff.add(2);
                }else{
                    blueBuffs.get(index).protector_skill_1_buff.add(2);
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Protector.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Protector.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Protector.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Protector.SKILL_2_POWERRAGE_COST){
                skill_2 = true;

                setBomb(player, Constants.PROTECTOR, 1);

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Protector.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Protector.SKILL_2_POWERRAGE_COST);
            }
        }

        public void useSkill_3(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Protector.SKILL_3_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Protector.SKILL_3_POWERRAGE_COST){
                skill_3 = true;

//                player.getMyHero().getAura().get(1).setState(Constants.AURA.PROTECTORAURA);  //aura

                if(team == Constants.PLAYER.RED_TEAM){
                    for(int i = 0 ; i < redBuffs.size ; i++) {
                        redBuffs.get(i).protector_skill_3_left_round = Constants.Protector.SKILL_3_ROUND_NUM;

                        if(i == index) {
                            playerListRed.get(i).getMyHero().setDecreaseRate(Constants.Protector.SKILL_3_SELF_DAMAGE_PERCENTAGE);
                        }else{
                            playerListRed.get(i).getMyHero().setDecreaseRate(Constants.Protector.SKILL_3_TEAMMATE_DAMAGE_PERCENTAGE);
                            redBuffs.get(i).FromIndex = index;
                        }

                        playerListRed.get(i).getMyHero().getAura().get(1).setState(Constants.AURA.PROTECTORAURA);
                    }
                }else{
                    for(int i = 0 ; i < blueBuffs.size ; i++){
                        blueBuffs.get(i).protector_skill_3_left_round = Constants.Protector.SKILL_3_ROUND_NUM;
                        if(i == index){
                            playerListBlue.get(i).getMyHero().setDecreaseRate(Constants.Protector.SKILL_3_SELF_DAMAGE_PERCENTAGE);
                        }else{
                            playerListBlue.get(i).getMyHero().setDecreaseRate(Constants.Protector.SKILL_3_TEAMMATE_DAMAGE_PERCENTAGE);
                            blueBuffs.get(i).FromIndex = index;
                        }

                        playerListBlue.get(i).getMyHero().getAura().get(1).setState(Constants.AURA.PROTECTORAURA);
                    }
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Protector.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Protector.SKILL_3_POWERRAGE_COST);
            }
        }

    }

    public class SniperSkill{
        public boolean skill_1;
        public boolean skill_2;
        public boolean skill_3;

        public void useSkill_1(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sniper.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sniper.SKILL_1_POWERRAGE_COST){
                skill_1 = true;

                setBomb(player, Constants.SNIPER, 1);

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sniper_skill_1_buff++;
                }else{
                    blueBuffs.get(index).sniper_skill_1_buff++;
                }

                player.getMyHero().setAntiArmor(MathUtils.clamp(player.getMyHero().getAntiArmor() +
                        Constants.Sniper.SKILL_1_ANTI_ARMOR_RATE,0,1));

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sniper.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sniper.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sniper.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sniper.SKILL_2_POWERRAGE_COST){
                skill_2 = true;
                player.getMyHero().setAttackTimes(2); // 攻击次数

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sniper.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sniper.SKILL_2_POWERRAGE_COST);
            }
        }

        public void useSkill_3(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sniper.SKILL_3_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sniper.SKILL_3_POWERRAGE_COST){
                skill_3 = true;

                player.getMyHero().setCriticalRate(3f);
                player.getMyHero().getAura().get(3).setState(Constants.AURA.SNIPERAURA);

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sniper_skill_3_buff = Constants.Sniper.SKILL_3_LEFT_ROUND;
                }else{
                    blueBuffs.get(index).sniper_skill_3_buff = Constants.Sniper.SKILL_3_LEFT_ROUND;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sniper.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sniper.SKILL_3_POWERRAGE_COST);
            }
        }


    }

    public class WizardSkill{
        public boolean skill_1;
        public boolean skill_2;
        public boolean skill_3;

        public void useSkill_1(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Wizard.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Wizard.SKILL_1_POWERRAGE_COST){
                skill_1 = true;

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Wizard.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Wizard.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Wizard.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Wizard.SKILL_2_POWERRAGE_COST){
                skill_2 = true;

                setBomb(player, Constants.WIZARD, 1);

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Wizard.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Wizard.SKILL_2_POWERRAGE_COST);
            }
        }

        public void useSkill_3(int team, String IP){
            Player player;
            int index = -1;
            if(team == Constants.PLAYER.RED_TEAM){
                index = getindex(playerListRed, IP);
                player = playerListRed.get(index);
            }else{
                index = getindex(playerListBlue, IP);
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Wizard.SKILL_3_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Wizard.SKILL_3_POWERRAGE_COST){
                skill_3 = true;

                if(team == Constants.PLAYER.RED_TEAM){
                    for(int i = 0 ; i < blueBuffs.size ; i++){          //属于红队则引爆蓝队
                        playerListBlue.get(i).getMyHero().setHealth(MathUtils.clamp(playerListBlue.get(i).getMyHero().getHealth()
                            - Constants.Wizard.SKILL_3_PER_LAYER * blueBuffs.get(i).wizard_skill_0_debuff, 0,
                                playerListBlue.get(i).getMyHero().getMaxHealth()));
                    }
                }else{
                    for(int i = 0 ; i < redBuffs.size ; i++){          //属于红队则引爆蓝队
                        playerListRed.get(i).getMyHero().setHealth(MathUtils.clamp(playerListRed.get(i).getMyHero().getHealth()
                                        - Constants.Wizard.SKILL_3_PER_LAYER * redBuffs.get(i).wizard_skill_0_debuff, 0,
                                playerListRed.get(i).getMyHero().getMaxHealth()));
                    }
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Wizard.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Wizard.SKILL_3_POWERRAGE_COST);
            }
        }


    }

    private int getindex(Array<Player> playerList, String IP){
        int index = -1;
        for(int i = 0 ; i < playerList.size ; i++){
            if(playerList.get(i).getIp().equals(IP)){
                index = i;
            }
        }
        return index;
    }

    private void setBomb(Player player,int heroType, int bombType){
        player.bomb.setPosition(player.getMyHero().getPosition());
        player.bomb.setHeroType(heroType);
        player.bomb.setBombType(bombType);
        player.bomb.setState(Constants.BOMB.STATE_READY);
    }


}
