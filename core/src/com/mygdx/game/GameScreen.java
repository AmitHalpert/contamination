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
    ObjectAnimation LeftPlayerHealthHUD;
    ObjectAnimation RightPlayerHealthHUD;
    ObjectAnimation RadioActivePoolAnimation;
    Texture background;
    Texture guiMenu;

    // world parameters
    static final int WORLD_WIDTH = Gdx.graphics.getWidth();
    static final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    // The players Array
    static Array<Player> Players;

    //World objects
    Array<MapObject> ground;
    Array<MapObject> WorldBorder;
    Array<MapObject> RadioActivePool;

    public GameScreen(final contamination game){
        this.game =  game;

        // initialize parameters
        IsGUI = false;
        isPaused = false;
        IsScreenMainMenu = false;


        ////
        // SFX
        ////
        GameAmbience = Gdx.audio.newMusic(Gdx.files.internal("GameAmbience.mp3"));
        GameAmbience.setLooping(true);
        GameAmbience.setVolume(0.09f);
        GameAmbience.play();

        ////
        // graphics
        ////
        guiMenu = new Texture("menugui.png");
        background = new Texture("genesis.png");
        // right health bar
        RightPlayerHealthHUD = new ObjectAnimation();
        RightPlayerHealthHUD.loadAnimation("right-player-health_",4);
        // left health bar
        LeftPlayerHealthHUD = new ObjectAnimation();
        LeftPlayerHealthHUD.loadAnimation("left-player-health_",4);
        // RadioActive Pool
        RadioActivePoolAnimation = new ObjectAnimation();
        RadioActivePoolAnimation.loadAnimation("RadioActivePoolAnimation_",5);

        ////
        // Create Map Objects
        ////
        ground = new Array<MapObject>();
        WorldBorder = new Array<MapObject>();
        RadioActivePool = new Array<MapObject>();
        createGrounds();
        createMapBorders();
        createRadioActivePools();


        // creates the players
        Players = new Array<Player>();
        Players.add(new Player(1600,400,false));
        Players.add(new Player(400,500,true));



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

        ////
        // BEGIN TO DRAW:
        ////


        game.batch.begin();

        // Draws map
        game.batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        // Draws RadioActivePool Animation
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.002f),569,46,284,190);
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.002f),1209,200,213,190);

        // Draw the players
        for(Player players : Players){
            game.batch.draw(players.render(deltaTime,ground,WorldBorder,RadioActivePool), players.x,players.y,players.width,players.height);
        }


        DrawPlayersBullets();
        MenuGUI();
        DrawPlayersHealthBarHUD();

        game.batch.end();
    }


    public void DrawPlayersHealthBarHUD(){
        // blue player
        game.batch.draw(RightPlayerHealthHUD.getIndexFrame(0),1520,920,430,170);
        game.batch.draw(Players.get(0).render(deltaTime,ground,WorldBorder,RadioActivePool), 1760,950,Players.get(0).width,Players.get(0).height);

        // right player
        game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(0),-30,920,430,170);
        game.batch.draw(Players.get(1).render(deltaTime,ground,WorldBorder,RadioActivePool), -10,950,Players.get(1).width,Players.get(1).height);
    }

    public void MenuGUI(){

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


        // draws the blue's player bullets
        Array<Bullet> Bluebullets = Players.get(0).getBullets();
        for(Bullet BluebulletsIndex : Bluebullets){
            game.batch.draw(BluebulletsIndex.update(deltaTime,ground,WorldBorder),BluebulletsIndex.bulletX,BluebulletsIndex.bulletY,BluebulletsIndex.width,BluebulletsIndex.height);
        }


        // draws the orange's player bullets
        Array<Bullet> Orangebullets = Players.get(1).getBullets();
        for(Bullet OrangebulletsIndex : Orangebullets){
            game.batch.draw(OrangebulletsIndex.update(deltaTime,ground,WorldBorder),OrangebulletsIndex.bulletX,OrangebulletsIndex.bulletY,OrangebulletsIndex.width,OrangebulletsIndex.height);
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
        ground.add(new MapObject(50,10,82,330));
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
        WorldBorder.add(new MapObject(-435,-37,580,345));
        // middle rock
        WorldBorder.add(new MapObject(1065,-37,75,329));
        // inner middle left RadioActivePool
        WorldBorder.add(new MapObject(359,-115,150,275));
        // inner middle right RadioActivePool
        WorldBorder.add(new MapObject(918,-115,100,285));
        // right rock
        WorldBorder.add(new MapObject(1495,-39,74,315));

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

        for(Player players : Players){
            players.dispose();
        }
        GameAmbience.dispose();
        guiMenu.dispose();
        background.dispose();
    }
}
