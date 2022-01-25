package com.mygdx.game;

import java.lang.Math;

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


class GameScreen implements Screen {

    final DojaDuckGame game;
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


    GameScreen(final DojaDuckGame game){
        this.game = game;

        player = new Player(640,300);

        deltaTime = 0;
        isPaused = false;

        // set up the camera and the viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //graphics
        shapeRenderer = new ShapeRenderer();



    }


    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0, 0, 0, 0.3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        InputHandling(deltaTime);
        if (isPaused){
            deltaTime = 0;
        }

        //updates the camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        // Draw Stuff
        game.batch.begin();
        game.batch.draw(player.render(deltaTime), player.x, player.y,player.width, player.height);


        game.batch.end();

        // gets player input and updates the player's position
        // the player needs to be facing right when calculating its position in order for the overlaps function to work
        if (player.isFacingLeft) {
            player.flip();
        }

    }

    // function to process user input
    public void InputHandling(float deltaTime){
        player.moveXBy(0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) player.moveXBy(200 * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)) player.moveXBy(-200 * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.ENTER)) isPaused = true;
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) isPaused = false;

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
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

    }
}
