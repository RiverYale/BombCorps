package com.bombcorps.game.controller;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.World;

public class WorldController {
    private DirctGame game;
    private OrthographicCamera camera;
    private InputController input;
    private CameraController cameraController;

    private NetController net;
    private World world;

    public WorldController(DirctGame game, OrthographicCamera camera, NetController net) {
        this.game = game;
        this.camera = camera;
        this.net = net;
        cameraController = new CameraController();
        input = new InputController(this);
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
}
