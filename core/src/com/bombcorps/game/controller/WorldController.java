package com.bombcorps.game.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.Pillar;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.Bonus;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Rock;
import com.bombcorps.game.model.World;
import com.bombcorps.game.view.DirectedGame;

public class WorldController {
    private DirectedGame game;
    private OrthographicCamera camera;
    private CameraController cameraController;

    private NetController net;
    private World world;
    private int perRound = 0;

    private Player curPlayer;
    private int operations;

    public WorldController(DirectedGame game, OrthographicCamera camera, NetController net) {
        this.game = game;
        this.camera = camera;
        this.net = net;
        game.getRoom().getPlayerManager().gameBegin();
        this.world = game.getWorld();
        cameraController = new CameraController();
        init();
    }

    public void init() {
        operations = -1;
        net.bindWorldController(this);
        curPlayer = world.getFirstPlayer();
        cameraController.setTarget(curPlayer);
//        AudioController.instance.play(AssetsController.instance.getMusic("")); //TODO
    }

    public void update(float deltaTime){
        testCollisions();
        cameraController.update(deltaTime);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public CameraController getCameraController() {
        return cameraController;
    }

    public NetController getNetController() {
        return net;
    }

    public World getWorld(){
        return  world;
    }

    public String getWorldIp(){
        return world.getIp();
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public Array<Player> getPlayers() {
        return world.getPlayers();
    }

    public Player hasPlayer(float x, float y) {
        for(Player p : world.getPlayers()){
            Vector2 position =new Vector2();
            Vector2 dimension = new Vector2();
            p.getRect().getPosition(position);
            p.getRect().getSize(dimension);
//            Gdx.app.log("qin","position.x = "+position.x+" position.y = "+position.y+
//                    " dimension.x = "+dimension.x+" dimension.y = "+dimension.y);
            if(p.getRect().contains(x, y)){
                return p;
            }
        }
        return null;
    }

    public int getOperations() {
        return operations;
    }

    public void resetOperations() {
        net.operate(operations, curPlayer.getDestX(), curPlayer.getTap().x, curPlayer.getTap().y);
        this.operations = -1;
    }

    public void onHeroClicked(Player p) {
//        Gdx.app.log("qin","hero clicked is used in WorldController");
        game.onHeroClicked(p);
    }

    public void onOperationClicked(int index) {
        switch (index) {
            case 0:
                operations = 0;
                break;
            case 1:
                operations = 1;
                //TODO curPlayer放个球
                getCurPlayer().getBomb().setState(Constants.BOMB.STATE_READY);
                break;
            case 2:
                operations = 2;
                //TODO curPlayer放个炸弹
                getCurPlayer().getBomb().setState(Constants.BOMB.STATE_READY);
                break;
            case 3:
            case 4:
            case 5:
                curPlayer.useSkill(index);
                resetOperations();
                break;
            case 6:
                if(world.getIp().equals(curPlayer.getIp())){
                    boolean hasBonus = MathUtils.random(9) > 6; // 30%的几率
                    Bonus b = null;
                    if(hasBonus){
                        b = world.spawnBonus();
                    }
                    net.roundStart(b);
                    startNextRound(b);
                }else{
                    net.roundOver();
                }
                break;
        }
    }

    public Bonus spawnBonus() {
        return world.spawnBonus();
    }

    public void startNextRound(Bonus b) {
        //TODO
//        Gdx.app.log("ain","Change");
        world.getPlayerManager().initEveryChange();
//        Gdx.app.log("qin","next player is ");
        if(b != null){
            world.addBonus(b);
        }

        curPlayer = world.getNextPlayer();

        perRound++;
        while (curPlayer.getMyHero().getState() == Constants.STATE_DEAD){
            curPlayer = world.getNextPlayer();
            perRound++;
        }
        if (perRound > world.getPlayers().size) {
            world.getPlayerManager().initEveryRound();
            perRound %= world.getPlayers().size;
        }
        cameraController.setTarget(curPlayer);
    }

    public void playerOperate(Message msg) {
        int op = msg.getOp();
        float targetX = msg.getTargetX();
        float tapX = msg.getTapX();
        float tapY = msg.getTapY();
        switch (op) {
            case 0:
                curPlayer.setDestX(targetX);
                break;
            case 1:
                //TODO curPlayer扔球
                if(curPlayer.useSkill(op)) {
                    curPlayer.setTap(new Vector2(tapX, tapY));
                    curPlayer.shoot(cameraController);
                }
                break;
            case 2:
                //TODO curPlayer扔炸弹
                if(curPlayer.useSkill(op)) {
//                    curPlayer
                    curPlayer.setTap(new Vector2(tapX, tapY));
                    curPlayer.shoot(cameraController);

                }
                break;
            case 3:
            case 4:
            case 5:
                curPlayer.useSkill(op);
                break;
        }
    }

    public void playerQuit(Message msg) {
        Player p = msg.getTargetPlayer();
        game.playerQuit(p.getID());
        world.removePlayer(p);
    }

    public void errorStop() {
        game.errorStop();
    }

    public int isGameOver() {  //0 未结束   1 红赢   2 蓝赢   3 平
        boolean red = false, blue = false;
        for (Player p : world.getPlayers()) {
            if(p.getTeam() == Constants.PLAYER.RED_TEAM){
                red = true;
            }else{
                blue = true;
            }
        }
        if (red && blue) {
            return 0;
        } else if (red) {
            return 1;
        } else if (blue) {
            return 2;
        } else {
            return 3;
        }
    }

    public void testCollisions() {
        boolean b_falling = true;
        Rectangle r1 = curPlayer.getRect();
        Rectangle r2;
        for(Rock r : world.rocks) {
            r2 = r.getRect();
            r1.setPosition(r1.getX(), r1.getY()-0.05f);
            if (r1.overlaps(r2)) {
                b_falling = false;
            }
            r1.setPosition(r1.getX(), r1.getY()+0.05f);
            if (r1.overlaps(r2)) {
                onCollisionsPlayerWithRock(r);
            }
        }
        for (Pillar p : world.pillars) {
            r2 = p.getRect();
            if (r1.overlaps(r2)) {
                onCollisionsPlayerWithPillar(p);
            }
        }
        if (b_falling) {
            curPlayer.setHeroState(Constants.STATE_FALLING);
        }
        for (Bonus b : world.bonusManager.getBonusList()) {
            r2 = b.getRect();
            if (r1.overlaps(r2)) {
                onCollisionsPlayerWithBonus(b);
            }
        }
        if(curPlayer.getBomb().getState() == Constants.BOMB.STATE_FLY) {
            r1 = curPlayer.getBomb().getRect();
            for(Rock r : world.rocks) {
                r2 = r.getRect();
                if (r1.overlaps(r2)) {
                    onCollisionsBombWithRock(curPlayer.getBomb());
                }
            }
        }

        Bonus fallingOne = world.getFallingBonus();
        if(fallingOne != null){
            r1 = fallingOne.getRect();
            for (Rock r : world.rocks) {
                r2 = r.getRect();
                if (r1.overlaps(r2)) {
                    onCollisionsBonusWithRock(fallingOne, r);
                }
            }
        }
    }

    private void onCollisionsPlayerWithRock(Rock r) {
        float heightDifference = Math.abs(curPlayer.getPosition().y - (r.getPosition().y + r.getRect().getHeight()));
        if (heightDifference > 0.25f) { //TODO
            boolean hitLeftEdge = curPlayer.getPosition().x > (r.getPosition().x + r.getRect().getWidth() / 2.0f);
            if (hitLeftEdge) {
                curPlayer.setX(r.getPosition().x + r.getRect().getWidth());
            } else {
                curPlayer.setX(r.getPosition().x - r.getRect().getWidth());
            }
        }
        switch (curPlayer.getHeroState()) {
            case Constants.STATE_GROUNDED:
                break;
            case Constants.STATE_MOVING:
                if (heightDifference > 0.25f) {
                    curPlayer.setHeroState(Constants.STATE_GROUNDED);
                } else {
                    curPlayer.setY(r.getPosition().y + r.getRect().getHeight());
                }
                break;
            case Constants.STATE_FALLING:
                curPlayer.setY(r.getPosition().y + r.getRect().getHeight());
                curPlayer.setHeroState(Constants.STATE_GROUNDED);
                break;
        }
    }

    private void onCollisionsPlayerWithPillar(Pillar r) {
        float heightDifference = Math.abs(curPlayer.getPosition().y - (r.getPosition().y + r.getRect().getHeight()));
        if (heightDifference > 0.25f) { //TODO
            boolean hitLeftEdge = curPlayer.getPosition().x > (r.getPosition().x + r.getRect().getWidth() / 2.0f);
            if (hitLeftEdge) {
                curPlayer.setX(r.getPosition().x + r.getRect().getWidth());
            } else {
                curPlayer.setX(r.getPosition().x - r.getRect().getWidth());
            }
        }
        switch (curPlayer.getHeroState()) {
            case Constants.STATE_GROUNDED:
                break;
            case Constants.STATE_MOVING:
                if (heightDifference > 0.25f) {
                    curPlayer.setHeroState(Constants.STATE_GROUNDED);
                } else {
                    curPlayer.setY(r.getPosition().y + r.getRect().getHeight());
                }
                break;
            case Constants.STATE_FALLING:
                curPlayer.setY(r.getPosition().y + r.getRect().getHeight());
                curPlayer.setHeroState(Constants.STATE_GROUNDED);
                break;
        }
    }

    private void onCollisionsPlayerWithBonus(Bonus b) {
        b.attachTo(curPlayer);
    }

    private void onCollisionsBombWithRock(Bomb b) {
        world.getPlayerManager().explode(curPlayer);
    }

    private void onCollisionsBonusWithRock(Bonus b, Rock rock) {
        b.setState(Constants.BONUS.GROUNDED);
    }

}
