package com.bombcorps.game.controller;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.Bonus;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.World;

public class WorldController {
    private DirctGame game;
    private OrthographicCamera camera;
    private InputController input;
    private CameraController cameraController;

    private NetController net;

    public World getWorld() {
        return world;
    }

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

    public Array<Player> getPlayers() {
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
                if(curPlayer.isNeedTarget(index)){
                    operations = index;
                }else{
                    curPlayer.useSkill(index, null);
                    resetOperations();
                }
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

    public void startNextRound(Bonus b) {
        if(b != null){
            world.addBonus(b);
        }
        curPlayer = world.getNextPlayer();
        cameraController.setTarget(curPlayer);
    }

    public void playerOperate(Message msg) {
        int op = msg.getOp();
        Player target = msg.getTarget();
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
                if(curPlayer.isNeedTarget(op)){
                    curPlayer.useSkill(op, target);
                }else{
                    curPlayer.useSkill(op, null);
                }
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
}
