package com.bombcorps.game.model;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    public static final String HEROES_OBJECTS = "images/heroes.txt";
    public static final String CONFIG = "BombCorpsConfig";
    public static final float MAX_ZOON_IN = 0.5f;
    public static final float MAX_ZOON_OUT = 2f;

    public static final float ACCELERATION = -5f;   //加速度
    public static final float VELOCITY_X = 5f;        //水平速度

    public static final int HERO_MAX_LEVEL = 5;
    /*
    Bomb类
     */
    public static final class BOMB{
        public static final int STATE_FLY = 0;
        public static final int STATE_BOOM = 1;
        public static final float BOOM_DURETION = 0.2f;
        public static final Vector2 BOOM_DIMENSION = new Vector2(5f,5f);
        public static final Vector2 BOOM_SCALE = new Vector2(1f,1f);

        public static final Vector2 DIMENSION = new Vector2(5f,5f);

        public static final float ROTATE_SPEED = 120f;
        public static final float ACCELERATION = -8f;
        public static final Vector2 BOMBSCALE = new Vector2(1f,1f);
        public static final float ROUTESCALE = 1f;
    }


    /*
    人物状态
     */
    public static final Vector2 HERO_DIMENSION = new Vector2(10f,10f);

    public static final int SPARDA = 0;
    public static final int PROTECTOR = 1;
    public static final int ANGEL = 2;
    public static final int SNIPER = 3;
    public static final int WIZARD = 4;

    public static final int STATE_MOVING = 1;
    public static final int STATE_GROUNDED = 2;
    public static final int STATE_FALLING = 3;
    public static final int STATE_ATTACK = 4;
    public static final int STATE_DEAD = 5;

    public static final float MAX_ENDURENCE = 200;
    public static final float MAX_RAGEPOWER = 100;
    public static final float MOVE_ENDURANCE_COST = 0.2f;
    public static final float TRANSPORT_ENDURENCE_COST = 150f;
    public static final float CRITICALPROBABILITY_DAMAGE_RATE = 2f;

    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    /*
    Sniper
     */
    public static final class Sniper{
        public static final float HEALTH = 800f;
        public static final float ATTACK = 200f;
        public static final float ARMOR = 70f;
        public static final float START_CRITICALPROBABILITY = 0f;
        /*
        技能消耗
         */
        public static final float SKILL_1_ENDURENCE_COST = 50f;
        public static final float SKILL_2_ENDURENCE_COST = 80f;
        public static final float SKILL_3_ENDURENCE_COST = 100f;
        public static final float SKILL_1_POWERRAGE_COST = 10f;
        public static final float SKILL_2_POWERRAGE_COST = 30f;
        public static final float SKILL_3_POWERRAGE_COST = 100f;
        /*
        技能效果加成
         */
        public static final float SKILL_0_CRITICALPROBABILITY_PER_ROUND_ADD = 0.2f;
        public static final float SKILL_1_ANTI_ARMOR_RATE = 0.3f;
        public static final float SKILL_2_SECOND_HIT_DAMAGE_PERCENTAGE = 0.5f;
        public static final float SKILL_3_CRITICALBROBABILITY_DAMAGE_RATE = 3f;
    }

    /*
    Angel
     */
    public static final class Angel{
        public static final float HEALTH = 1000f;
        public static final float ATTACK = 50f;
        public static final float ARMOR = 50f;
        public static final float START_CRITICALPROBABILITY = 0.2f;
        public static final float ANTI_ARMOR_RATE = 0f;
        /*
        技能消耗
         */
        public static final float SKILL_1_ENDURENCE_COST = 50f;
        public static final float SKILL_2_ENDURENCE_COST = 80f;
        public static final float SKILL_3_ENDURENCE_COST = 100f;
        public static final float SKILL_1_POWERRAGE_COST = 10f;
        public static final float SKILL_2_POWERRAGE_COST = 30f;
        public static final float SKILL_3_POWERRAGE_COST = 100f;
        /*
        技能效果加成
         */
        public static final float SKILL_0_HEALTH_PER_ROUND_ADD = 150f;
        public static final float SKILL_1_HEALTH_PERCENTAGE_ADD = 0.1f;
        public static final int SKILL_2_ROUND_NUM = 2;
        public static final float SKILL_2_ATTACK_PERCENTAGE_MIN = 0.2f;
        public static final float SKILL_2_ENDURANCE_MIN = 80f;
        public static final float SKILL_3_TEAMMATE_HEALTH_ADD = 500f;
        public static final float SKILL_3_TEAMMATE_ATTACK_PERCENTAGE_ADD = 0.1f;
    }

    /*
    Wizard
     */
    public static final class Wizard{
        public static final float HEALTH = 800f;
        public static final float ATTACK = 0f;
        public static final float ARMOR = 70f;
        public static final float START_CRITICALPROBABILITY = 0f;
        public static final float ANTI_ARMOR_RATE = 0.3f;
        /*
        技能消耗
         */
        public static final float SKILL_1_ENDURENCE_COST = 50f;
        public static final float SKILL_2_ENDURENCE_COST = 80f;
        public static final float SKILL_3_ENDURENCE_COST = 100f;
        public static final float SKILL_1_POWERRAGE_COST = 10f;
        public static final float SKILL_2_POWERRAGE_COST = 30f;
        public static final float SKILL_3_POWERRAGE_COST = 100f;
        /*
        技能效果加成
         */
        public static final float SKILL_0_DAMAGE_PER_ROUND_PER_LAYER = 50f;
        public static final float SKILL_1_LAYERS_ADD = 3f;
        public static final float SKILL_2_SEAL_ROUND = 1f;
        public static final float SKILL_3_PERCENTAGE_PER_LAYER = 0.1f;
    }

    /*
    Sparda
     */
    public static final class Sparda{
        public static final float HEALTH = 1000f;
        public static final float ATTACK = 130f;
        public static final float ARMOR = 70f;
        public static final float START_CRITICALPROBABILITY = 0.2f;
        public static final float ANTI_ARMOR_RATE = 0f;
        /*
        技能消耗
         */
        public static final float SKILL_1_ENDURENCE_COST = 50f;
        public static final float SKILL_2_ENDURENCE_COST = 80f;
        public static final float SKILL_3_ENDURENCE_COST = 100f;
        public static final float SKILL_1_HEALTH_COST = 100f;
        public static final float SKILL_2_HEALTH_COST = 100f;
        public static final float SKILL_3_HEALTH_COST = 100f;
        /*
        技能效果加成
         */
        public static final float SKILL_0_ATTACK_PERCENTAGE_ADD = 0.1f;
        public static final float SKILL_0_HEALTH_PERCENTAGE_ADD = 0.3f;
        public static final float SKILL_1_ATTACK_ADD = 50f;
        public static final float SKILL_2_CRITICALPROBABILITY_ADD = 0.3f;
        public static final float SKILL_3_POWERRAGE_EACH_ROUND_COST = 20f;

    }

    /*
    Protector
     */
    public static final class Protector{
        public static final float HEALTH = 1500f;
        public static final float ATTACK = 70f;
        public static final float ARMOR = 130f;
        public static final float START_CRITICALPROBABILITY = 0.2f;
        public static final float ANTI_ARMOR_RATE = 0.3f;
        /*
        技能消耗
         */
        public static final float SKILL_1_ENDURENCE_COST = 50f;
        public static final float SKILL_2_ENDURENCE_COST = 80f;
        public static final float SKILL_3_ENDURENCE_COST = 100f;
        public static final float SKILL_1_POWERRAGE_COST = 10f;
        public static final float SKILL_2_POWERRAGE_COST = 30f;
        public static final float SKILL_3_POWERRAGE_COST = 100f;
        /*
        技能效果加成
         */
        public static final float SKILL_1_ARMOR_ADD = 100f;
        public static final float SKILL_2_SHIELD_PERCENTAGE = 0.2f;
        public static final int SKILL_3_ROUND_NUM = 2;
        public static final float SKILL_3_TEAMMATE_DAMAGE_PERCENTAGE = 0.4f;
        public static final float SKILL_3_SELF_DAMAGE_PERCENTAGE = 0.6f;
    }

}
