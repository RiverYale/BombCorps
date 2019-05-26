package com.bombcorps.game.controller;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.bombcorps.game.model.World;

public class WorldController {
    private OrthographicCamera camera;
    private InputController inputController;
    private CameraController cameraController;
    private World world;

    public WorldController(Game game, OrthographicCamera camera) {
        this.camera = camera;
        cameraController = new CameraController();
        inputController = new InputController(this);
    }

    public InputProcessor getInputProcessor() {
        return new GestureDetector(inputController);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public CameraController getCameraController() {
        return cameraController;
    }
}
