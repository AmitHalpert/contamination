package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;
import java.util.LinkedList;


class GameScreen implements Screen {

    final contamination game;



    // Main menu features
    float deltaTime;
    boolean IsGUI;
    static boolean isPaused;
    float timeDrop;

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
    LinkedList<AmmoDrop> AmmoDrops;
    Array<MapObject> Grounds;
    Array<MapObject> WorldBorders;
    Array<MapObject> RadioActivePools;



    public GameScreen(final contamination game){
        this.game =  game;

        // initialize parameters
        IsGUI = false;
        isPaused = false;

        //**
        // creates the players
        //**
        Players = new Array<Player>();
        Players.add(new Player(1500,400, Player.PlayersController.Blue));
        Players.add(new Player(400,500,Player.PlayersController.Orange));





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
        Grounds = new Array<MapObject>();
        WorldBorders = new Array<MapObject>();
        RadioActivePools = new Array<MapObject>();
        AmmoDrops = new LinkedList<>();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        deltaTime = 0;
        timeDrop = 0;


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();


        //updates the camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Creates and places the map Objects
        createGrounds();
        createMapBorders();
        createRadioActivePools();

        ////
        // BEGIN TO DRAW:
        ////
        game.batch.begin();

        // Draws map
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        // Draws RadioActivePool Animation
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.002f), 569, 46, 284, 190);
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.002f), 1209, 200, 213, 190);

        // Draw the players
        for (Player players : Players) {
            game.batch.draw(players.render(deltaTime, Grounds, WorldBorders, RadioActivePools), players.PlayerX, players.PlayerY, players.width, players.height);
        }



        // collisionHandling
        // freeze if on ground
        for(MapObject GroundIndex : Grounds){
            for(AmmoDrop DropIndex : AmmoDrops){
                if(DropIndex.hitBox.overlaps(GroundIndex.hitBox)){
                    DropIndex.freeze = true;
                }
            }
        }


        for(MapObject RadioActivePoolIndex : RadioActivePools) {
            for (Iterator<AmmoDrop> Iter = AmmoDrops.iterator(); Iter.hasNext(); ) {
                AmmoDrop TempAmmoDrops = Iter.next();
                if (TempAmmoDrops.hitBox.overlaps(RadioActivePoolIndex.hitBox) || TempAmmoDrops.DeleteDrop) {
                    Iter.remove();
                }
            }
        }



        // Drop Spawner.
        timeDrop += deltaTime;
        if (timeDrop >= 1f) {
            AmmoDrop drop = new AmmoDrop(MathUtils.random(0, 1900), 1920);
            AmmoDrops.add(drop);
            timeDrop = 0;
        }




        for (AmmoDrop drops : AmmoDrops) {
            game.batch.draw(drops.update(deltaTime), drops.dropX, drops.dropY, drops.width, drops.height);
        }




        /////
        // Draw hierarchy
        /////

        DrawPlayersBullets();
        MenuGUI();
        DrawPlayersHealthBarHUD();

        game.batch.end();

        ////
        // END TO DRAW:
        ////
    }


    public void DrawAmmoDrops(){
        // Draws AmmoDrops

    }

    public void DrawPlayersHealthBarHUD(){
        // blue player health bar
        switch (Players.get(0).PlayerHealth){
            case 3:
                game.batch.draw(RightPlayerHealthHUD.getIndexFrame(0),1520,920,430,170);
                break;

            case 2:
                game.batch.draw(RightPlayerHealthHUD.getIndexFrame(1),1520,920,430,170);
                break;

            case 1:
                game.batch.draw(RightPlayerHealthHUD.getIndexFrame(2),1520,920,430,170);
                break;
            case 0:
                game.batch.draw(RightPlayerHealthHUD.getIndexFrame(3),1520,920,430,170);
                break;
            default:
                game.batch.draw(RightPlayerHealthHUD.getIndexFrame(3),1520,920,430,170);

        }
        game.batch.draw(Players.get(0).render(deltaTime, Grounds, WorldBorders, RadioActivePools), 1760,950,Players.get(0).width,Players.get(0).height);




        // orange player health bar
        switch (Players.get(1).PlayerHealth){
            case 3:
                game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(0),-30,920,430,170);
                break;

            case 2:
                game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(1),-30,920,430,170);
                break;

            case 1:
                game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(2),-30,920,430,170);
                break;
            case 0:
                game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(3),-30,920,430,170);
                break;
            default:
                game.batch.draw(LeftPlayerHealthHUD.getIndexFrame(3),-30,920,430,170);
        }
        game.batch.draw(Players.get(1).render(deltaTime, Grounds, WorldBorders, RadioActivePools), -10,950,Players.get(1).width,Players.get(1).height);
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
                if(Gdx.input.justTouched()){
                    GameAmbience.stop();
                    dispose();
                    game.setScreen(new MainMenuScreen(game));
                    }
                }
            }
        }

    public void DrawPlayersBullets(){

        // draws the blue's player bullets
        Array<Bullet> Bluebullets = Players.get(0).getBullets();
        for(Bullet BluebulletsIndex : Bluebullets){

            game.batch.draw(BluebulletsIndex.update(deltaTime, Grounds, WorldBorders), BluebulletsIndex.bulletX, BluebulletsIndex.bulletY, BluebulletsIndex.width, BluebulletsIndex.height);
        }


        // draws the orange's player bullets
        Array<Bullet> Orangebullets = Players.get(1).getBullets();
        for(Bullet OrangebulletsIndex : Orangebullets){
            game.batch.draw(OrangebulletsIndex.update(deltaTime, Grounds, WorldBorders),OrangebulletsIndex.bulletX,OrangebulletsIndex.bulletY,OrangebulletsIndex.width,OrangebulletsIndex.height);
        }

    }

    public void createRadioActivePools(){
        RadioActivePools.add(new MapObject(1260,5,90,200));
        RadioActivePools.add(new MapObject(610,-100,190,170));
    }

    private void createGrounds(){
        ////
        // environment Grounds
        ////
        //left rock
        Grounds.add(new MapObject(50,10,89,330));
        // middle rock
        Grounds.add(new MapObject(1067,-12,70,320));
        // right rock
        Grounds.add(new MapObject(1495,-12,70,320));
        // right Ground
        Grounds.add(new MapObject(920,-39,1200,224));
        // left Ground
        Grounds.add(new MapObject(50,-39,455,224));
    }

    private void createMapBorders(){

        ////
        // environment bounds
        ////

        //left rock
        WorldBorders.add(new MapObject(-435,-37,580,375));
        // middle(left) rock
        WorldBorders.add(new MapObject(1065,-37,75,345));
        // inner middle left RadioActivePool
        WorldBorders.add(new MapObject(359,-115,150,295));
        // inner middle right RadioActivePool
        WorldBorders.add(new MapObject(918,-115,100,295));
        // right rock
        WorldBorders.add(new MapObject(1495,-39,74,345));

        ////
        // WORLD BOUNDS
        ////

        // create left world border
        WorldBorders.add(new MapObject(-650,200,580,3000));

        // create right world border
        WorldBorders.add(new MapObject(1989,200,500,1200));

        // create upper world border
        WorldBorders.add(new MapObject(-550,1200,3000,200));

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
        RightPlayerHealthHUD.dispose();
        LeftPlayerHealthHUD.dispose();
        RadioActivePoolAnimation.dispose();
        GameAmbience.dispose();
        guiMenu.dispose();
        background.dispose();
    }
}
