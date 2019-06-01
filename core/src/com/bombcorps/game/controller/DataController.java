package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.bombcorps.game.model.Constants;

public class DataController {
    public static final String TAG = DataController.class.getName();
    public static final DataController instance = new DataController();
    public static final int MONEY = 0;
    public static final int ANGEL = 1;
    public static final int PROTECTOR = 2;
    public static final int SNIPER = 3;
    public static final int SPARDAR = 4;
    public static final int WIZARD = 5;
    public static final int GAME_NUM = 6;
    public static final int WIN_NUM = 7;
    private Preferences prefs;
    private int upLevelCost[] = {100, 200, 300, 400, 500};
    private String name;
    private float volSound;
    private float volMusic;
    private String personalDataKey[] = {"money", "angel", "protector", "sniper", "sparda", "wizard", "gameNum", "winNum"};
    private int personalDataVal[];

    private DataController() {
        prefs = Gdx.app.getPreferences(Constants.CONFIG);
        personalDataVal = new int[personalDataKey.length];
    }

    public float getVolSound() {
        return volSound;
    }

    public void setVolSound(float volSound) {
        this.volSound = volSound;
    }

    public float getVolMusic() {
        return volMusic;
    }

    public void setVolMusic(float volMusic) {
        this.volMusic = volMusic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void loadPersonalData() {
        for(int i=0;i<personalDataKey.length;i++){
            personalDataVal[i] = prefs.getInteger(personalDataKey[i], 0);
        }
    }

    public int getPersonalData(int type){
        return personalDataVal[type];
    }

    public boolean upLevel(int type){
        if(type<1 || type>5 || personalDataVal[type]>=Constants.HERO_MAX_LEVEL || personalDataVal[MONEY]<upLevelCost[personalDataVal[type]]){
            return false;
        }
        personalDataVal[MONEY] -= upLevelCost[personalDataVal[type]];
        personalDataVal[type]++;
        prefs.putInteger("money", personalDataVal[MONEY]);
        prefs.putInteger(personalDataKey[type], personalDataVal[type]);
        prefs.flush();
        return true;
    }

    public void addMoney(int amount) {
        personalDataVal[MONEY] += amount;
        prefs.putInteger("money", personalDataVal[MONEY]);
        prefs.flush();
    }

    public void addGameNum(boolean isWin) {
        personalDataVal[GAME_NUM]++;
        prefs.putInteger("gameNum", personalDataVal[GAME_NUM]);
        if (isWin) {
            personalDataVal[WIN_NUM]++;
            prefs.putInteger("gameNum", personalDataVal[WIN_NUM]);
        }
        prefs.flush();
    }

    public void loadSettings(){
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        name = prefs.getString("name", "Steve");
        if(name.length() < 1){
            name = "Steve";
        }
    }

    public void saveSettings(){
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putString("name", name);
        prefs.flush();
    }
}
