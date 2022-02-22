package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


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
    static final int WORLD_WIDTH = 1920;
    static final int WORLD_HEIGHT = 1080;

    //World objects
    Array<MapObject> ground;
    Array<MapObject> WorldBorder;

    public GameScreen(final contamination game){
        this.game =  game;

        // Map graphics
        background = new Texture("genesis.png");

        // Creates the wall array for the map and calls makeWalls function
        ground = new Array<MapObject>();
        WorldBorder = new Array<MapObject>();
        createGrounds();
        CreateMapBorders();

        // creates a player
        player = new Player(1750,300);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // initialize parameters
        deltaTime = 0;

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
        //Draw the player
        game.batch.draw(player.render(deltaTime, ground,WorldBorder),  player.x,  player.y, player.width,  player.height);


        DrawBullets();

        game.batch.end();

    }

    // Creates the maps ground
    private void createGrounds(){

        // environment Grounds

        //left rock
        ground.add(new MapObject(50,10,85,330));

        // middle rock
        ground.add(new MapObject(1067,-12,70,320));

        // right rock
        ground.add(new MapObject(1495,-12,70,320));

        // right Ground
        ground.add(new MapObject(920,-39,1200,224));
        // left Ground
        ground.add(new MapObject(50,-39,455,224));
    }

    private void CreateMapBorders(){


        // environment bounds

        //left rock
        WorldBorder.add(new MapObject(-435,-39,580,369));

        // middle rock
        WorldBorder.add(new MapObject(1065,-35,74,329));

        // right rock
        WorldBorder.add(new MapObject(1495,-35,74,329));


        // WORLD BOUNDS

        // create left world border
        WorldBorder.add(new MapObject(-650,200,580,3000));

        // create right world border
        WorldBorder.add(new MapObject(1989,200,500,1200));

        // create upper world border
        WorldBorder.add(new MapObject(-550,1200,3000,200));

    }

    public void MainMenu(){
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
            dispose();
            Gdx.app.exit();
        }
    }

    public void DrawBullets(){
        Array<Bullet> bullets = Player.getBullets();
        for (int w = 0; w < bullets.size; w++){
            Bullet b = (Bullet) bullets.get(w);
            game.batch.draw(b.update(deltaTime,ground,WorldBorder),b.x,b.y,120,120);
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
