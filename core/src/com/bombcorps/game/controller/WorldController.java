package com.bombcorps.game.controller;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.bombcorps.game.model.Bombs.Bomb;
import com.bombcorps.game.model.Bonus;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Rock;
import com.bombcorps.game.model.World;

import java.util.ArrayList;

public class WorldController {
    private DirctGame game;
    private OrthographicCamera camera;
    private InputController input;
    private CameraController cameraController;

    private NetController net;
    private World world;

    private Player curPlayer;
    private int operations;

    public WorldController(DirctGame game, OrthographicCamera camera, NetController net) {
        this.game = game;
        this.camera = camera;
        this.net = net;
        this.world = game.getWorld();
        cameraController = new CameraController();
        input = new InputController(this);
    }

    public void init() {
        operations = -1;
        net.bindWorldController(this);
        curPlayer = world.getFirstPlayer();
        cameraController.setTarget(curPlayer);
        AudioController.instance.play(AssetsController.instance.getMusic("")); //TODO
    }

    public InputProcessor getInputProcessor() {
        return new GestureDetector(input);
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

    public String getWorldIp(){
        return world.getIp();
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return world.getPlayers();
    }

    public Player hasPlayer(float x, float y) {
        for(Player p : world.getPlayers()){
            if(p.getRec().contains(x, y)){
                return p;
            }
        }
        return null;
    }

    public int getOperations() {
        return operations;
    }

    public void resetOperations() {
        net.operate(operations, curPlayer, curPlayer.getDestX(), curPlayer.getTapX(), curPlayer.getTapY());
        this.operations = -1;
    }

    public void onOperationClicked(int index) {
        switch (index) {
            case 0:
                operations = 0;
                break;
            case 1:
                operations = 1;
                //TODO curPlayer放个球
                break;
            case 2:
                operations = 2;
                //TODO curPlayer放个炸弹
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
        if(b != null){
            world.addBonus(b);
        }
        curPlayer = world.getNextPlayer();
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
                curPlayer.setTapX(tapX);
                curPlayer.setTapY(tapY);
                curPlayer.shoot();
                break;
            case 2:
                //TODO curPlayer扔炸弹
                curPlayer.setTapX(tapX);
                curPlayer.setTapY(tapY);
                curPlayer.shoot();
                break;
            case 3:
            case 4:
            case 5:
                curPlayer.useSkill(op);
                break;
        }
    }

    public void playerQuit(Message msg) {
        Player p = msg.getPlayera();
        game.playerQuit(p.getID());
        world.removePlayer(p);
    }

    public void errorStop() {
        game.errorStop();
    }

    public boolean isGameOver() {
        boolean red = false, blue = false;
        for (Player p : world.getPlayers()) {
            if(p.getTeam() == 0){
                red = true;
            }else{
                blue = true;
            }
        }
        return !(red && blue);
    }

    public void testCollisions() {
        //TODO Bonus碰Rock? Bomb碰Player?
        Rectangle r1 = curPlayer.getRect();
        Rectangle r2;
        for(Rock r : world.rocks) {
            r2 = r.getRect();
            if (r1.overlaps(r2)) {
                onCollisionsPlayerWithRock(r);
            }
        }
        for (Bonus b : world.bonus) {
            r2 = b.getRect();
            if (r1.overlaps(r2)) {
                onCollisionsPlayerWithBonus(b);
            }
        }
        if(curPlayer.getBomb().getState() == FLY) { //TODO
            r1 = curPlayer.getBomb().getRect();
            for(Rock r : world.rocks) {
                r2 = r.getRect();
                if (r1.overlaps(r2)) {
                    onCollisionsBombWithRock(curPlayer.getBomb());
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
            return;
        }
        switch (curPlayer.getHeroState()) {
            case WAIT:
            case GROUNDED:
                break;
            case MOVING:
                if (heightDifference > 0.25f) { //TODO
                    curPlayer.se
                    curPlayer.setHeroState(Constants.STATE_GROUNDED);
                } else {
                    curPlayer.setY(r.getPosition().y + r.getRect().getHeight());
                }
                break;
            case FALLING:
                r.getPosition().y + r.getRect().getHeight();
                curPlayer.setHeroState(Constants.STATE_GROUNDED);
                break;
        }
    }

    private void onCollisionsPlayerWithBonus(Bonus b) {
        b.attachTo(curPlayer);
    }

    private void onCollisionsBombWithRock(Bomb b) {
        b.explode();
    }

}
