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

    // Main menu features
    float deltaTime;

    //Screen
    OrthographicCamera camera;
    Viewport viewport;

    //graphics
    private final Texture background;

    // world parameters
    private final int WORLD_WIDTH = 1920;
    private final int WORLD_HEIGHT = 1080;

    //World objects
    Array<MapObject> Platforms;

    public GameScreen(final contamination game){
        this.game = game;

        // creates a player
        player = new Player(700,600);

        // initialize parameters
        deltaTime = 0;

        // set up the camera and the viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Map graphics
        background = new Texture("map.png");

        // Creates the wall array for the map and calls makeWalls function
        Platforms = new Array<MapObject>();
        createGround();
        createMapBounds();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        //The Game's Main menu
        MainMenu();

        //updates the camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // BEGIN TO DRAW:
        game.batch.begin();

        //Draw map
        game.batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        //Draw the player and gives all the input player class needs
        game.batch.draw(player.render(deltaTime, Platforms),  player.x,  player.y, player.width,  player.height);


        game.batch.end();

    }

    // Creates the maps ground
    private void createGround(){
        for(int i = 50; i < 650; i+= 50){
            Platforms.add(new MapObject(i,-110,1300,203));
        }
    }

    private void createMapBounds(){
        // create left world border
        for(int i = 50; i < 650; i+= 50){
            Platforms.add(new MapObject(-700,i,640,600));
        }
        // create right world border
        for(int i = 50; i < 650; i+= 50){
            Platforms.add(new MapObject(1989,i,500,600));
        }

        for(int i = 50; i < 650; i+= 50){
            Platforms.add(new MapObject(i,1200,1300,203));
        }
    }

    public void MainMenu(){
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
            dispose();
            Gdx.app.exit();
        }
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
        background.dispose();

    }
}
