package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


class GameScreen implements Screen {

    final contamination game;
    BluePlayer blue;
    YellowPlayer yellow;

    // Main menu features
    float deltaTime;
    boolean IsGUI;
    static boolean isPaused;
    static boolean IsScreenMainMenu;


    // SFX and music
    Music GameAmbience;

    //Screen
    OrthographicCamera camera;
    Viewport viewport;

    //graphics
    Texture background;
    Texture guiMenu;

    // world parameters
    static final int WORLD_WIDTH = 1920;
    static final int WORLD_HEIGHT = 1080;

    //World objects
    Array<MapObject> ground;
    Array<MapObject> WorldBorder;
    Array<MapObject> RadioActivePool;

    public GameScreen(final contamination game){
        this.game =  game;

        IsGUI = false;
        isPaused = false;
        IsScreenMainMenu = false;

        guiMenu = new Texture("menugui.png");

        // sounds
        GameAmbience = Gdx.audio.newMusic(Gdx.files.internal("GameAmbience.mp3"));
        GameAmbience.setLooping(true);
        GameAmbience.setVolume(0.3f);
        GameAmbience.play();

        // Map graphics
        background = new Texture("genesis.png");

        // Create Map Objects
        ground = new Array<MapObject>();
        WorldBorder = new Array<MapObject>();
        RadioActivePool = new Array<MapObject>();
        createGrounds();
        createMapBorders();
        createRadioActivePools();


        // creates a players
        blue = new BluePlayer(1750,300);
        yellow = new YellowPlayer(500,500);

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

        if(IsScreenMainMenu){
            game.setScreen(new MainMenuScreen(game));
        }

        //updates the camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // BEGIN TO DRAW:
        game.batch.begin();
        //Draw map
        game.batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        //Draw the players
        game.batch.draw(blue.render(deltaTime, ground, WorldBorder, RadioActivePool),  blue.x,  blue.y, blue.width,  blue.height);
        game.batch.draw(yellow.render(deltaTime,ground,WorldBorder,RadioActivePool),   yellow.x,yellow.y ,yellow.width,yellow.height);

        DrawPlayersBullets();
        GUI();


        game.batch.end();
    }




    public void GUI(){

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !IsGUI) {
            IsGUI = true;
        }
        else if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && IsGUI){
            IsGUI = false;
            isPaused = false;
        }


        if(IsGUI){
            isPaused = true;
            game.batch.draw(guiMenu,MainMenuScreen.xCenter/2+300,MainMenuScreen.yCenter/2+300,400,400);
            //exit button


            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 290 + 100 + 300 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 290 + 300){
                if(Gdx.input.isTouched()){
                    dispose();
                    Gdx.app.exit();
                }
            }

            // resume button
            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 500 + 100 + 300  && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 530 + 300){
                if(Gdx.input.isTouched()){
                    isPaused = false;
                    IsGUI = false;
                }
            }
            // main menu button
            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 400 + 100 + 300 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 400+40 + 300){
                if(Gdx.input.justTouched() && !IsScreenMainMenu ){
                    GameAmbience.stop();
                    dispose();
                    IsScreenMainMenu = true;
                    }
                }
            }
        }

    public void DrawPlayersBullets(){
        Array<Bullet> Bluebullets = BluePlayer.getBullets();
        for (int BlueIndex = 0; BlueIndex < Bluebullets.size; BlueIndex++){
            Bullet BluePlayerBullets = Bluebullets.get(BlueIndex);
            game.batch.draw(BluePlayerBullets.update(deltaTime,ground,WorldBorder),BluePlayerBullets.bulletX,BluePlayerBullets.bulletY,120,120);
        }

        Array<Bullet> Yellowbullets = YellowPlayer.getYellowPlayerBullets();
        for (int YellowIndex = 0; YellowIndex < Yellowbullets.size; YellowIndex++){
            Bullet YellowPlayerBullets = Yellowbullets.get(YellowIndex);
            game.batch.draw(YellowPlayerBullets.update(deltaTime,ground,WorldBorder),YellowPlayerBullets.bulletX,YellowPlayerBullets.bulletY,120,120);
        }

    }

    public void createRadioActivePools(){
        RadioActivePool.add(new MapObject(1260,5,70,200));
        RadioActivePool.add(new MapObject(650,-100,100,170));
    }

    private void createGrounds(){
        ////
        // environment Grounds
        ////
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

    private void createMapBorders(){

        ////
        // environment bounds
        ////

        //left rock
        WorldBorder.add(new MapObject(-435,-37,580,329));
        // middle rock
        WorldBorder.add(new MapObject(1065,-37,75,310));
        // middle pit left
        WorldBorder.add(new MapObject(359,-115,150,260));
        // middle pit right
        WorldBorder.add(new MapObject(918,-115,100,275));
        // right rock
        WorldBorder.add(new MapObject(1495,-39,74,310));

        ////
        // WORLD BOUNDS
        ////
        // create left world border
        WorldBorder.add(new MapObject(-650,200,580,3000));

        // create right world border
        WorldBorder.add(new MapObject(1989,200,500,1200));

        // create upper world border
        WorldBorder.add(new MapObject(-550,1200,3000,200));

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
        yellow.dispose();
        GameAmbience.dispose();
        guiMenu.dispose();
        blue.dispose();
        background.dispose();
    }
}
