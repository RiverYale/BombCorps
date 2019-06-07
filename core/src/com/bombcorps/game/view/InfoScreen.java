package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.DataController;

public class InfoScreen extends AbstractGameScreen implements InputProcessor{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sprite back, myInfoBoard, heroBoard, heroInfoBoard, heroBigImage, upgrade, skillInfoBoard, bar;
    private float baseHeroInfo[][] = {
            {1000f/1500f, 50f/200f, 50f/130f},
            {1500f/1500f, 70f/200f, 130f/130f},
            {800f/1500f, 200f/200f, 70f/130f},
            {1000f/1500f, 130f/200f, 70f/130f},
            {800f/1500f, 0f/200f, 70f/130f}
    };

    private String heroNames[] = {"天使", "守护者", "射手", "狂战士", "巫师"};
//    private String heroNames[] = {"Angel", "Protector", "Sniper", "Sparda", "Wizard"};
    private TextureRegion regions[] = {AssetsController.instance.getRegion("Angel_stand"),
                                       AssetsController.instance.getRegion("Protector_stand"),
                                       AssetsController.instance.getRegion("Sniper_stand"),
                                       AssetsController.instance.getRegion("Sparda_stand"),
                                       AssetsController.instance.getRegion("Wizard_stand")};

    private int index = 0;
    private BitmapFont font;
    private String description[] = {
            "被动技能：牺牲护甲与攻击力，获得每回\n" +
                    "          合固定回血的能力\n" +
                    "技能一：发射一枚恢复弹，为目标治疗\n" +
                    "        10%最大生命值\n" +
                    "技能二：消耗80精力+30怒气,发射一枚虚\n" +
                    "        弱弹，减少其攻击力、精力值\n" +
                    "技能三：消耗100精力+100怒气，友方集\n" +
                    "        体回500血，接下来3回合友方\n" +
                    "        增加10%伤害",
            "被动技能：远高于平均的血量与护甲，\n" +
                    "          但牺牲攻击力\n" +
                    "技能一：消耗10怒气+50精力，增加护甲\n" +
                    "技能二：消耗80精力+30怒气，发射一枚\n" +
                    "        护盾弹，为目标增加100点护甲\n" +
                    "技能三：消耗100精力+100怒气，两回合\n" +
                    "        内获得60%减伤效果，且分担友\n" +
                    "        方受到伤害的40%",
            "被动技能：具有高攻击力，且每次攻击成\n" +
                    "          攻击成功暴击几率减半，失败\n" +
                    "          暴击几率加倍\n" +
                    "技能一：消耗50精力+10怒气获得30%穿甲\n" +
                    "技能二：消耗80精力+30怒气，获得额外\n" +
                    "        一次攻击机会，精力和怒气减半\n" +
                    "技能三：消耗100精力+100怒气，接下来\n" +
                    "        两回合暴击几率不减少，且暴\n" +
                    "        击伤害变为300%",
            "被动技能：具备吸血30%能力\n" +
                    "技能一：消耗100血量+50精力，提高攻击力\n" +
                    "技能二：消耗80精力+30怒气，下次攻击\n" +
                    "        获得额外30%暴击几率加成\n" +
                    "技能三：消耗100精力，和100怒气，\n" +
                    "        立即获得300的血量补给，\n" +
                    "        接下来3回合每回合回100血\n",
            "被动技能：无法造成物理伤害，但对目标\n" +
                    "          积累一层中毒效果，目标每回\n" +
                    "          合根据层数扣除血量\n" +
                    "技能一：下次攻击附加3层中毒效果\n" +
                    "技能二：消耗80精力+30怒气，攻击禁锢\n" +
                    "        目标1回合\n" +
                    "技能三：消耗100精力+100怒气，消耗所\n" +
                    "    有敌方的标记层数，对受到标记的单\n" +
                    "    位造成10%最大生命值*层数的伤害"

    };

    private static final float viewWidth = 900f;
    private static final float viewHeight = 500f;

    public InfoScreen(DirectedGame game) {
        super(game);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(viewWidth, viewHeight);
        camera.position.set(viewWidth/2, viewHeight/2, 0);
        camera.update();
        init();
    }

    public void init() {
        font = AssetsController.instance.font;

        back = new Sprite(AssetsController.instance.getRegion("back"));
        back.setSize(back.getWidth(), back.getHeight());
        back.setPosition(5, 450);

        myInfoBoard = new Sprite(AssetsController.instance.getRegion("board"));
        myInfoBoard.setSize(viewWidth*0.2f, viewHeight*0.75f);
        myInfoBoard.setPosition(50f, 60f);

        heroBoard = new Sprite(AssetsController.instance.getRegion("listItem"));
        heroBoard.setSize(viewWidth*0.2f, myInfoBoard.getHeight()/5f);
        heroBoard.setX(10f+myInfoBoard.getX()+myInfoBoard.getWidth());

        heroInfoBoard = new Sprite(AssetsController.instance.getRegion("board"));
        heroInfoBoard.setSize(viewWidth*0.46f, myInfoBoard.getHeight());
        heroInfoBoard.setPosition(10f+heroBoard.getX()+heroBoard.getWidth(), myInfoBoard.getY());

        heroBigImage = new Sprite(AssetsController.instance.getRegion("board"));
        heroBigImage.setSize(viewWidth*0.10f, viewWidth*0.12f);
        heroBigImage.setPosition(heroInfoBoard.getX()+10f, heroInfoBoard.getY()+heroInfoBoard.getHeight()-heroBigImage.getHeight()-8f);

        upgrade = new Sprite(AssetsController.instance.getRegion("upgrade"));
        upgrade.setSize(heroBigImage.getWidth(), 30f);
        upgrade.setPosition(heroBigImage.getX(), heroBigImage.getY()-upgrade.getHeight()-5f);

        skillInfoBoard = new Sprite(AssetsController.instance.getRegion("board"));
        skillInfoBoard.setSize(heroInfoBoard.getWidth(), upgrade.getY()-heroInfoBoard.getY()-8f);
        skillInfoBoard.setPosition(heroInfoBoard.getX(), heroInfoBoard.getY());

        bar = new Sprite(AssetsController.instance.getRegion("white"));
        bar.setSize(120f, 18f);
        bar.setX(heroBigImage.getX()+heroBigImage.getWidth()+107);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(AssetsController.instance.getRegion("background"), 0, 0, viewWidth, viewHeight);
        renderMyInfo(batch);
        renderHeroList(batch);
        renderHeroInfo(batch);
        back.draw(batch);

        batch.end();
    }

    private String title[] = {"昵称", "金币", "胜场", "胜率"};
    public void renderMyInfo(SpriteBatch batch) {
        myInfoBoard.draw(batch);
        font.getData().setScale(1f);
        for(int i=0;i<4;i++){
            font.draw(batch, title[i]+": ", 70, 410-i*90);
        }
        DataController dc = DataController.instance;
        font.draw(batch, dc.getName(), 90, 370);
        font.draw(batch, dc.getPersonalData(DataController.MONEY)+"", 90, 370-90);
        font.draw(batch, dc.getPersonalData(DataController.WIN_NUM)+"/"+dc.getPersonalData(DataController.GAME_NUM), 90, 370-2*90);
        font.draw(batch, dc.getWinRate()+"", 90, 370-3*90);
    }

    public void renderHeroList(SpriteBatch batch) {
        font.getData().setScale(0.8f);
        font.setColor(205/255f, 201/255f, 201/255f, 1);
        for(int i=0;i<5;i++) {
            heroBoard.setY(60f+viewHeight*0.15f*i);
            heroBoard.draw(batch);

            if((4-i) == index){
                font.setColor(Color.GOLD);
            }
            batch.draw(regions[4-i], heroBoard.getX()+20, heroBoard.getY()+22);
            font.draw(batch, heroNames[4-i], heroBoard.getX() + 65, heroBoard.getY() + 55);
            char s[] = "☆☆☆☆☆".toCharArray();
            for(int j=0;j<DataController.instance.getPersonalData(5-i);j++){
                s[j] = '★';
            }
            font.draw(batch, new String(s), heroBoard.getX() + 65, heroBoard.getY() + 30);
            if((4-i) == index){
                font.setColor(205/255f, 201/255f, 201/255f, 1);
            }
        }
        font.setColor(Color.BLACK);
    }

    private Color c[] = {Color.RED, Color.GOLD, Color.ROYAL};
    public void renderHeroInfo(SpriteBatch batch) {
        //背板
        heroInfoBoard.draw(batch);
        heroBigImage.draw(batch);
        upgrade.draw(batch);
        skillInfoBoard.draw(batch);

        //大头像
        TextureRegion tr = regions[index];
        float scale = 1.8f;
        batch.draw(tr, heroBigImage.getX()+(heroBigImage.getWidth()-tr.getRegionWidth()*scale)/2, heroBigImage.getY()+(heroBigImage.getHeight()-tr.getRegionHeight()*scale)/2, tr.getRegionWidth()*scale, tr.getRegionHeight()*scale);

        //技能描述
        font.getData().setScale(0.9f);
        font.draw(batch, description[index], skillInfoBoard.getX()+15, skillInfoBoard.getY() + 200);

        //升级按钮
        font.getData().setScale(0.8f);
        int cost = DataController.instance.getUpLevelCost(index+1);
        if(cost == -1){
            font.setColor(Color.GRAY);
            font.draw(batch, "升级", upgrade.getX()+25, upgrade.getY()+20);
            font.setColor(Color.BLACK);
        }else{
            if (cost > DataController.instance.getPersonalData(DataController.MONEY)) {
                font.setColor(Color.GRAY);
            } else {
                font.setColor(255/255f, 246/255f, 143/255f, 1);
            }
            font.draw(batch, "升级("+cost+")", upgrade.getX()+5, upgrade.getY()+20);
            font.setColor(Color.BLACK);
        }

        //属性字
        char s[] = "☆☆☆☆☆".toCharArray();
        for(int i=0;i<DataController.instance.getPersonalData(1+index);i++){
            s[i] = '★';
        }
        font.getData().setScale(0.9f);
        float x = heroBigImage.getX()+heroBigImage.getWidth()+20;
        float y = heroBigImage.getY()+heroBigImage.getHeight()-5;
        float margin = 30f;
        font.draw(batch, "称  号: " + heroNames[index], x, y);
        font.draw(batch, "等  级: " + new String(s), x, y-margin*1);
        font.draw(batch, "血  量: ", x, y-margin*2);
        font.draw(batch, "攻击力: ", x, y-margin*3);
        font.draw(batch, "护  甲: ", x, y-margin*4);

        //属性条
        for(int i=0;i<3;i++) {
            bar.setSize(120, bar.getHeight());
            bar.setColor(Color.GRAY);
            bar.setY(heroBigImage.getY()+27-i*30);
            bar.draw(batch);

            bar.setColor(c[i]);
            bar.setY(heroBigImage.getY()+27-30*i);
            bar.setSize((int)(120 * baseHeroInfo[index][i]), bar.getHeight());

            bar.draw(batch);
        }

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = new Vector3(screenX, screenY, 0);
        camera.unproject(v);
        for(int i=0;i<5;i++) {
            heroBoard.setY(60f+viewHeight*0.15f*i);
            if (heroBoard.getBoundingRectangle().contains(v.x, v.y)) {
                index = 4-i;
                break;
            }
        }
        if (upgrade.getBoundingRectangle().contains(v.x, v.y)) {
            DataController.instance.upLevel(index + 1);
        } else if(back.getBoundingRectangle().contains(v.x, v.y)){
            game.loadLobbyScreen();
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        batch.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
