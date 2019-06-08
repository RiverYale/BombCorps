package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.view.GameScreen;

public class InputController implements GestureDetector.GestureListener {
    private GameScreen gameScreen;
    private WorldController controller;
    private CameraController cameraController;
    private OrthographicCamera camera;
    private float tempZoom, tempInitDis;
    private boolean hasAim;

    public InputController(GameScreen gameScreen, WorldController controller) {
        this.gameScreen = gameScreen;
        this.controller = controller;
        this.cameraController = controller.getCameraController();
        this.camera = controller.getCamera();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 v = new Vector3(x, y, 0);
        camera.unproject(v);
//        Gdx.app.log("zc", "x="+v.x+"  y="+v.y);
        //确定是否按到了炸弹
        if(controller.getOperations()==1 || controller.getOperations()==2){

//            if(controller.getCurPlayer().getBomb() == null)
//                Gdx.app.log("bomb","null");
//            else
//                Gdx.app.log("bomb","not null");

            Rectangle r = controller.getCurPlayer().getBomb().getRect();
            if(r.contains(v.x, v.y)){
                hasAim = true;
            }else{
                hasAim = false;
            }
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 v = new Vector3(x, y, 0);
        gameScreen.cameraGUI.unproject(v);
        if(gameScreen.btnQuit.getBoundingRectangle().contains(v.x,v.y)){
            gameScreen.getGame().getNetController().quitGame(gameScreen.myPlayer());
            gameScreen.getGame().loadLobbyScreen();
            return false;
        }else if(gameScreen.btnSettings.getBoundingRectangle().contains(v.x,v.y)){
            gameScreen.setStageTrue();
            Gdx.input.setInputProcessor(gameScreen.stage);
            gameScreen.winOptions.setVisible(true);
            return false;
        }else if(gameScreen.imgMove.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(0);
            return false;
        }else if(gameScreen.imgEjection.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(1);
            return false;
        }else if(gameScreen.imgAttrack.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(2);
            return false;
        }else if(gameScreen.imgSkillOne.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(3);
            return false;
        }else if(gameScreen.imgSkillTwo.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(4);
            return false;
        }else if(gameScreen.imgSkillThree.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(5);
            return false;
        }else if(gameScreen.imgTurnEnd.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.myPlayer().equals(gameScreen.worldController.getCurPlayer())){
            controller.onOperationClicked(6);
            return false;
        }else if(gameScreen.imgMyHeroHead.getBoundingRectangle().contains(v.x,v.y)){
            gameScreen.setStageTrue();
            Gdx.input.setInputProcessor(gameScreen.stage);
            gameScreen.winHeroInfo.setVisible(true);
            return false;
        }else if(gameScreen.imgOtherHeroHead.getBoundingRectangle().contains(v.x,v.y)&&gameScreen.isClickedHero()){
            gameScreen.setStageTrue();
            Gdx.input.setInputProcessor(gameScreen.stage);
            gameScreen.winOtherHeroInfo.setVisible(true);
            return false;
        }

        v = new Vector3(x, y, 0);
        camera.unproject(v);
        int op = controller.getOperations();
        if(op == -1) {
            Player p = controller.hasPlayer(v.x, v.y);
            if (p != null) {
                cameraController.setTarget(p);
                 controller.onHeroClicked(p);
            }
        } else if (op == 0) {
            controller.getCurPlayer().setDestX(v.x);
            controller.resetOperations();
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(hasAim){
            Vector3 v = new Vector3(x, y, 0);
            camera.unproject(v);
            controller.getCurPlayer().setTap(new Vector2(v.x, v.y));
            controller.resetOperations();
        }else{
            deltaX = -deltaX/Gdx.graphics.getWidth()*(camera.viewportWidth*camera.zoom);
            deltaY = deltaY/Gdx.graphics.getHeight()*(camera.viewportHeight*camera.zoom);
            deltaX += camera.position.x;
            deltaY += camera.position.y;
            cameraController.setPosition(deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        int op = controller.getOperations();
        if(op == 1 || op == 2){
            controller.getCurPlayer().shoot();
        }
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(tempInitDis != initialDistance){
            tempInitDis = initialDistance;
            tempZoom = camera.zoom;
        }
        float zoom = MathUtils.clamp(initialDistance/distance*tempZoom, Constants.MAX_ZOON_IN, Constants.MAX_ZOON_OUT);
        cameraController.setZoom(zoom);
        return false;
    }

    @Override
    public void pinchStop() {

    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
