package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class GameScreen implements Screen {

    //Screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private Texture backgroud;
    private TextureAtlas textureAtlas;
    private TextureRegion player_1_TextureRegion;

    // world parameters
    private final int WORLD_WIDTH = 54;
    private final int WORLD_HEIGHT = 35;

    // game objects
    private Player playerAmogus;


    GameScreen(){

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //set up the texture atlas
        textureAtlas = new TextureAtlas();
        backgroud = new Texture("map.png");
        textureAtlas = new TextureAtlas("images.atlas");

        //set up texture objects
        player_1_TextureRegion = textureAtlas.findRegion("tile091");

        // set up game objects
        playerAmogus = new Player(2,2,2,WORLD_WIDTH/2,WORLD_HEIGHT/4,player_1_TextureRegion );

        batch = new SpriteBatch();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        batch.draw(backgroud,0,0,WORLD_WIDTH ,WORLD_HEIGHT);


        //player
        playerAmogus.draw(batch);

        //FX



        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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

    }
}
