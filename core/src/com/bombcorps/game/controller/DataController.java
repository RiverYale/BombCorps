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
    private Preferences prefs;
    private int upLevelCost[] = {100, 200, 300, 400, 500};
    private String name;
    private float volSound;
    private float volMusic;
    private String personalDataKey[] = {"money", "angel", "protector", "sniper", "sparda", "wizard"};
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
        if(type<1 || type>5 || personalDataVal[type]>=Constants.HERO_MAX_LEVEL || personalDataVal[0]<upLevelCost[personalDataVal[type]]){
            return false;
        }
        personalDataVal[0] -= upLevelCost[personalDataVal[type]];
        personalDataVal[type]++;
        prefs.putInteger("money", personalDataVal[0]);
        prefs.putInteger(personalDataKey[type], personalDataVal[type]);
        prefs.flush();
        return true;
    }

    public void addMoney(int amount) {
        personalDataVal[0] += amount;
        prefs.putInteger("money", personalDataVal[0]);
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
