package com.bombcorps.game.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SkillAndBuff {

    private AngelSkill angelSkill;
    private SpardaSkill spardaSkill;
    private ProtectorSkill protectorSkill;
    private SniperSkill sniperSkill;
    private WizardSkill wizardSkill;



    private Array<Player> playerListBlue;
    private Array<Player> playerListRed;

    Array<Buff> redBuffs;
    Array<Buff> blueBuffs;


    public SkillAndBuff(Array<Player> playerlistblue, Array<Player> playerListred){
        this.playerListBlue = playerlistblue;
        this.playerListRed = playerListred;

        for(int i = 0 ; i < playerListRed.size ; i++){
            redBuffs.add(new Buff(playerListred.get(i)));
        }
        for(int i = 0 ; i < playerlistblue.size ; i++){
            blueBuffs.add(new Buff(playerlistblue.get(i)));
        }

    }

//    public void updateBuff(){
//
//    }

    public void initSkill(){               //每次操作对象改变，都要调用
        angelSkill = new AngelSkill();
        spardaSkill = new SpardaSkill();
        protectorSkill = new ProtectorSkill();
        sniperSkill = new SniperSkill();
        wizardSkill = new WizardSkill();
    }

    public class Buff{
        Player curPlayer;

        public Buff(Player player){
            curPlayer = player;
        }

        public void handleBuffEveryRound(){
            handleInstantBuff();

        }

        private void handleInstantBuff(){
            for(int i = 0 ; i < sparda_skill_1_buff ; i++){
                curPlayer.getMyHero().setAttack(curPlayer.getMyHero().getAttack() - Constants.Sparda.ATTACK * 0.1f);
            }

            if(sparda_skill_2_buff > 0)
                curPlayer.getMyHero().setCriticalProbability(Constants.Sparda.START_CRITICALPROBABILITY);

            if(sniper_skill_1_buff > 0)
                curPlayer.getMyHero().setAntiArmor(0);


        }

        Array<Integer> angel_skill_3_buff;      //增加AttackBUFF
        int angel_skill_2_debuff;               //虚弱剩余时间
        boolean angel_self_buff;                //天使自身回血buff

        int sparda_skill_1_buff;                //增加伤害的数量   instant
        int sparda_skill_2_buff;                //增加暴击的数量   instant
        boolean sparda_skill_3_buff;            //技能3是否释放

        Array<Integer> protector_skill_1_buff;  //技能1BUFF数量和剩余ROUND数量
        //skill_3
        int decrease_received_damage;              //减伤Buff剩余局数
        int FromIndex;                          //buff来源
        boolean receive_teammate_damage;    //是否接受队友伤害

//        int sniper_skill_0_buff;        //是否减少暴击几率

        int sniper_skill_1_buff;            //技能1增加穿甲的数量
        int sniper_skill_3_buff;            //技能3剩余局数

        int wizard_skill_0_debuff;          //debuff层数
        int wizard_skill_2_debuff;          //技能2禁锢剩余局数

    }

    public class AngelSkill{
        boolean skill_1;
        boolean skill_2;
        boolean skill_3;


        public void useSkill_1(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_2(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_3(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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
        boolean skill_1;
        boolean skill_2;
        boolean skill_3;

        public void useSkill_1(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getHealth() >= Constants.Sparda.SKILL_1_HEALTH_COST){

                skill_1 = true;

                setBomb(player, Constants.SPARDA, 1);

                player.getMyHero().setAttack(player.getMyHero().getAttack() + 0.1f * Constants.Sparda.ATTACK);
                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_1_buff++;
                }else{
                    blueBuffs.get(index).sparda_skill_1_buff++;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setHealth(player.getMyHero().getHealth() - Constants.Sparda.SKILL_1_HEALTH_COST);
            }
        }

        public void useSkill_2(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_2_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sparda.SKILL_2_RAGEPOWER_COST){

                skill_2 = true;

                setBomb(player, Constants.SPARDA, 1);

                player.getMyHero().setCriticalProbability(MathUtils.clamp(player.getMyHero().getCriticalProbability()
                        + 0.3f, 0, 1));

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_2_buff++;
                }else{
                    blueBuffs.get(index).sparda_skill_2_buff++;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_2_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sparda.SKILL_2_RAGEPOWER_COST);
            }
        }

        public void useSkill_3(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sparda.SKILL_3_ENDURENCE_COST){
                skill_3 = true;

                player.getMyHero().getAura().setState(Constants.AURA.SPARDAAURA);      //aura

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sparda_skill_3_buff = true;
                }else{
                    blueBuffs.get(index).sparda_skill_3_buff = true;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sparda.SKILL_3_ENDURENCE_COST);
            }
        }
    }

    public class ProtectorSkill{
        boolean skill_1;
        boolean skill_2;
        boolean skill_3;

        public void useSkill_1(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Protector.SKILL_1_ENDURENCE_COST &&
                    player.getMyHero().getRagePower() >= Constants.Protector.SKILL_1_POWERRAGE_COST){

                skill_1 = true;

                player.getMyHero().getAura().setState(Constants.AURA.PROTECTORAURA);  //aura

                player.getMyHero().setArmor(player.getMyHero().getArmor() + 50);

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).protector_skill_1_buff.add(2);
                }else{
                    blueBuffs.get(index).protector_skill_1_buff.add(2);
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Protector.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Protector.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_3(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Protector.SKILL_3_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Protector.SKILL_3_POWERRAGE_COST){
                skill_3 = true;

                if(team == Constants.PLAYER.RED_TEAM){
                    for(int i = 0 ; i < redBuffs.size ; i++) {
                        if(i == index) {
                            redBuffs.get(i).receive_teammate_damage = true;
                            redBuffs.get(i).decrease_received_damage = 2;
                        }else{
                            redBuffs.get(i).decrease_received_damage = 2;
                            redBuffs.get(i).FromIndex = index;
                        }

                    }
                }else{
                    for(int i = 0 ; i < blueBuffs.size ; i++){
                        if(i == index){
                            blueBuffs.get(i).receive_teammate_damage = true;
                            blueBuffs.get(i).decrease_received_damage = 2;
                        }else{
                            blueBuffs.get(i).decrease_received_damage = 2;
                            blueBuffs.get(i).FromIndex = index;
                        }
                    }
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Protector.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Protector.SKILL_3_POWERRAGE_COST);
            }
        }

    }

    public class SniperSkill{
        boolean skill_1;
        boolean skill_2;
        boolean skill_3;

        public void useSkill_1(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_2(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_3(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Sniper.SKILL_3_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Sniper.SKILL_3_POWERRAGE_COST){
                skill_3 = true;

                if(team == Constants.PLAYER.RED_TEAM){
                    redBuffs.get(index).sniper_skill_3_buff = 2;
                }else{
                    blueBuffs.get(index).sniper_skill_3_buff = 2;
                }

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Sniper.SKILL_3_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Sniper.SKILL_3_POWERRAGE_COST);
            }
        }


    }

    public class WizardSkill{
        boolean skill_1;
        boolean skill_2;
        boolean skill_3;

        public void useSkill_1(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
                player = playerListBlue.get(index);
            }

            if(player.getMyHero().getEndurance() >= Constants.Wizard.SKILL_1_ENDURENCE_COST
                    && player.getMyHero().getRagePower() >= Constants.Wizard.SKILL_1_POWERRAGE_COST){
                skill_1 = true;

                player.getMyHero().setEndurance(player.getMyHero().getEndurance() - Constants.Wizard.SKILL_1_ENDURENCE_COST);
                player.getMyHero().setRagePower(player.getMyHero().getRagePower() - Constants.Wizard.SKILL_1_POWERRAGE_COST);
            }
        }

        public void useSkill_2(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

        public void useSkill_3(int team, int index){
            Player player;
            if(team == Constants.PLAYER.RED_TEAM){
                player = playerListRed.get(index);
            }else{
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

    private void setBomb(Player player,int heroType, int bombType){
        player.bomb.setPosition(player.getMyHero().getPosition());
        player.bomb.setHeroType(heroType);
        player.bomb.setBombType(bombType);
        player.bomb.setState(Constants.BOMB.STATE_READY);
    }

//    public void initBuffEveryRound(){
//        /*
//        天使被动回血
//         */
//        for(Player i : playerListRed)
//            if(Angel.class.isInstance(i))
//                i.getMyHero().setHealth(MathUtils.clamp(i.getMyHero().getHealth() + 200, 0, i.getMyHero().getMaxHealth()));
//        for(Player i : playerListBlue)
//            if(Angel.class.isInstance(i))
//                i.getMyHero().setHealth(MathUtils.clamp(i.getMyHero().getHealth() + 200, 0, i.getMyHero().getMaxHealth()));
//        /*
//        天使技能3buff
//         */
//        initAngelSkill3Buff(Red_angel_skill_3_buff, playerListRed);
//        initAngelSkill3Buff(Blue_angel_skill_3_buff, playerListBlue);
//        /*
//        Wizard Buff
//        */
//        for(int i = 0 ; i < wizard_buff_list.length ; i++){
//            if(i < playerListRed.size){
//                playerListRed.get(i).getMyHero().setHealth(playerListRed.get(i).getMyHero().getHealth() - 100 * wizard_buff_list[i]);
//            }else{
//                playerListBlue.get(i).getMyHero().setHealth(playerListBlue.get(i).getMyHero().getHealth() - 100 * wizard_buff_list[i]);
//            }
//
//        }
//
//    }

//    private void initAngelSkill3Buff(Array<Integer> bufflist, Array<Player> playerlist){
//        for(int i = 0 ; i < bufflist.size ; i++){
//
//
//            bufflist.set(i, bufflist.get(i) - 1);
//
//            if(bufflist.get(i).equals(2)){
//                for(Player k : playerlist){
//                    k.getMyHero().getAura().setState(Constants.AURA.ANGELAURA);
////                    j.getMyHero().setAttack(j.getMyHero().getAttack() + Constants.);
//                    if(Angel.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Angel.ATTACK * 0.1f);
//                    if(Protector.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Protector.ATTACK * 0.1f);
//                    if(Sniper.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Sniper.ATTACK * 0.1f);
//                    if(Wizard.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Wizard.ATTACK * 0.1f);
//                    if(Sparda.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() + Constants.Sparda.ATTACK * 0.1f);
//                }
//            }
//
//            for(Player k : playerlist){
//                k.getMyHero().setHealth(MathUtils.clamp(k.getMyHero().getHealth() + 200, 0, k.getMyHero().getMaxHealth()));
//            }
//
//            if(bufflist.get(i).equals(0)){
//                bufflist.removeIndex(i);
//                for(Player k : playerlist){
//                    k.getMyHero().getAura().setState(Constants.AURA.ANGELAURA);
////                    j.getMyHero().setAttack(j.getMyHero().getAttack() + Constants.);
//                    if(Angel.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Angel.ATTACK * 0.1f);
//                    if(Protector.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Protector.ATTACK * 0.1f);
//                    if(Sniper.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Sniper.ATTACK * 0.1f);
//                    if(Wizard.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Wizard.ATTACK * 0.1f);
//                    if(Sparda.class.isInstance(k))
//                        k.getMyHero().setAttack(k.getMyHero().getAttack() - Constants.Sparda.ATTACK * 0.1f);
//                }
//            }
//
//        }
//
//        if(bufflist.size == 0)
//            for(Player i : playerlist){
//                i.getMyHero().getAura().setState(Constants.AURA.WAIT);
//            }
//
//    }

}
