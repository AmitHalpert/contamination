package com.mygdx.game;

import java.lang.Math;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;


class GameScreen implements Screen {

    private final contamination game;
    Player player;

    float deltaTime;
    Boolean isPaused;

    //Screen
    OrthographicCamera camera;
    Viewport viewport;

    //graphics
    ShapeRenderer shapeRenderer;


    // world parameters
    private final int WORLD_WIDTH = 1920;
    private final int WORLD_HEIGHT = 1080;

    Array<MapObject> Walls;

    public GameScreen(final contamination game){
        this.game = game;

        // creates a player
        player = new Player(700,600);

        // Creates the wall array for the map
        Walls = new Array<MapObject>();
        makeWalls();

        // initialize parameters
        deltaTime = 0;
        isPaused = false;

        // set up the camera and the viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    // Creates the maps ground
    private void makeWalls(){
        for(int i = 50; i < 650; i+= 50){
            Walls.add(new MapObject(i,200,200,200));
        }
    }




    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0, 0, 0, 0.3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        //Checks if the game is paused
        if (isPaused){
            deltaTime = 0;
        }

        //updates the camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Draw Stuff
        game.batch.begin();

        //Draw the player
        game.batch.draw(player.render(deltaTime, Walls), player.x, player.y,player.width, player.height);

        game.batch.end();

    }


    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.dispose();

    }
}
