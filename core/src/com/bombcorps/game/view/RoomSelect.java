package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class RoomSelect {
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();

    private Stack stack;
    private Table table;

    private int mapNum;
    private Image backGround;
    private String mode;
    private String hosterName;
    private String personNum;

    private Label labelMode;
    private Label labelHosterName;
    private Label labelPersonNum;

    private boolean click = false;

    RoomSelect(int mapNum, String mode, String hosterName, String personNum){
        stack = new Stack();
        stack.setSize(0.45f * width,0.1f * height);
        table = new Table();

        this.mode = mode;
        this.hosterName = hosterName;
        this.personNum = personNum;

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("lobbyscreen/roomshowbackground.png"))));
        Image smallMap = new Image();
        switch (mapNum){
            case 0:
                smallMap = new Image(new Texture(Gdx.files.internal("lobbyscreen/map0.png")));
                break;
        }
        smallMap.setScale(0.8f);
        Image intoRoom = new Image(new Texture(Gdx.files.internal("lobbyscreen/intoroom.png")));
        intoRoom.setScale(0.8f);

        table.background(drawable);

        BitmapFont font = new BitmapFont(Gdx.files.internal("lobbyscreen/test3.fnt"),
                Gdx.files.internal("lobbyscreen/test3.png"),false);
        Label.LabelStyle style = new Label.LabelStyle(font,font.getColor());
        labelMode = new Label(mode, style);
        labelMode.setFontScale(0.05f * width / 100);
        labelHosterName = new Label(hosterName,style);
        labelHosterName.setFontScale(0.05f * width / 100);
        labelPersonNum = new Label(personNum,style);
        labelPersonNum.setFontScale(0.05f * width / 100);

        table.setFillParent(true);
        table.pad(0.0333f*width);
        table.left();
        table.add(smallMap).padLeft(0.0223f*width);
        table.add(labelMode).padLeft(0.0223f*width);
        table.add(labelHosterName).padLeft(0.0223f*width);
        table.add(labelPersonNum).padLeft(0.0223f*width);
        table.add(intoRoom).padLeft(0.0223f*width);

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click = true;
            }
        });

        stack.addActor(table);
    }

    public void setPosition(float x,float y){
        this.stack.setPosition(x,y);
    }

    public void addToStage(Stage stage){
        stage.addActor(this.stack);
    }

    public boolean isClick(){
        return click;
    }

    public void update(float deltaTime){
        click = false;
    }
}
