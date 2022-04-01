package com.mygdx.game.Screens;

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
import com.mygdx.game.*;
import com.mygdx.game.Entities.Objects.AmmoDrop;
import com.mygdx.game.Entities.Objects.Bullet;
import com.mygdx.game.Entities.Player;
import com.mygdx.game.Tools.MapObject;
import com.mygdx.game.Tools.ObjectAnimation;


import java.util.Iterator;



public class GameScreen implements Screen {

    final contamination game;

    // Main menu features
    float deltaTime;
    boolean IsGUI;
    public static boolean isPaused;
    float timeDrop;

    // SFX and music
    Music GameAmbience;

    //Screen
    OrthographicCamera camera;
    Viewport viewport;

    //graphics
    Texture PressSpace;
    ObjectAnimation BluePlayerWinAnimation;
    ObjectAnimation OrangePlayerWinAnimation;
    ObjectAnimation AmmoNumbersTex;
    ObjectAnimation LeftPlayerHealthHUD;
    ObjectAnimation RightPlayerHealthHUD;
    ObjectAnimation RadioActivePoolAnimation;
    Texture background;
    Texture guiMenu;

    // world parameters
    static final int WORLD_WIDTH = Gdx.graphics.getWidth();
    static final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    // The players Array
    public static Array<Player> Players;

    //World objects
    Array<AmmoDrop> AmmoDrops;
    Array<MapObject> Grounds;
    Array<MapObject> WorldBorders;
    Array<MapObject> RadioActivePools;



    public GameScreen(final contamination game){
        this.game =  game;

        // initialize parameters
        IsGUI = false;
        isPaused = false;


        // creates the players
        Players = new Array<>();
        Players.add(new Player(1500,400, Player.PlayersController.Blue));
        Players.add(new Player(400,500,Player.PlayersController.Orange));


        ////////////////////////
        // SFX
        ////////////////////////
        GameAmbience = Gdx.audio.newMusic(Gdx.files.internal("GameAmbience.mp3"));
        GameAmbience.setLooping(true);
        GameAmbience.setVolume(0.09f);
        GameAmbience.play();

        ////////////////////////
        // graphics
        ////////////////////////

        // the map
        background = new Texture("genesis.png");

        // paused game menu
        guiMenu = new Texture("menugui.png");

        // Winner indicator
        PressSpace = new Texture("press-space.png");
        OrangePlayerWinAnimation = new ObjectAnimation();
        OrangePlayerWinAnimation.loadAnimation("ORANGE_PLAYER_WINS_",9);
        BluePlayerWinAnimation = new ObjectAnimation();
        BluePlayerWinAnimation.loadAnimation("BLUE_PLAYER_WINS_",11);



        // right health bar
        RightPlayerHealthHUD = new ObjectAnimation();
        RightPlayerHealthHUD.loadAnimation("right-player-health_",4);
        // left health bar
        LeftPlayerHealthHUD = new ObjectAnimation();
        LeftPlayerHealthHUD.loadAnimation("left-player-health_",4);

        // RadioActive Pool
        RadioActivePoolAnimation = new ObjectAnimation();
        RadioActivePoolAnimation.loadAnimation("RadioActivePoolAnimation_",5);

        // AmmoNumbers
        AmmoNumbersTex = new ObjectAnimation();
        AmmoNumbersTex.loadAnimation("num_",6);

        ////////////////////////
        // Map Objects
        ////////////////////////
        Grounds = new Array<>();
        WorldBorders = new Array<>();
        RadioActivePools = new Array<>();
        AmmoDrops = new Array<>();
        // Creates and places the map Objects
        createGrounds();
        createMapBorders();
        createRadioActivePools();

        // Camera and viewport
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
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Pauses everything that is delta time affected.
        if(isPaused){
            deltaTime = 0;
        }

        // spawns AmmoDrop and collision
        AmmoDropCollision(deltaTime);

        // removes bullet if touches something
        PlayersBulletCollisionHandling();


        ////////////////////////
        // Draw hierarchy
        ////////////////////////
        game.batch.begin();

        // Draws map and the map animations
        DrawMap(deltaTime);
        // draws the ammo drops
        DrawAmmoDrops(deltaTime);
        // draw all the players
        DrawPlayers();
        // draw every player's bullets
        DrawPlayersBullets();
        // draw health bars and ammo amount for the player
        DrawPlayersHealthBarHUD();
        // shows which player won
        DrawWinnerPlayer(deltaTime);
        // draw gui and pauses the game
        DrawMenu();



        game.batch.end();
    }




    ////////////////////////
    // Draw functions
    ////////////////////////

    public void DrawMap(float deltaTime){
        // draw map
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        // Draws RadioActivePool Animation
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.3f * deltaTime), 569, 46, 284, 190);
        game.batch.draw(RadioActivePoolAnimation.getFrame(0.3f * deltaTime), 1209, 200, 213, 190);
    }

    public void DrawPlayers(){
        for (Player players : Players) {
            game.batch.draw(players.render(Gdx.graphics.getDeltaTime(), Grounds, WorldBorders, RadioActivePools), players.PlayerX, players.PlayerY, players.width, players.height);
        }
    }

    public void DrawAmmoDrops(float deltaTime){
        // draws the ammo drops
        for (AmmoDrop drops : AmmoDrops) {
            if(drops.IsExplosion){
                game.batch.draw(drops.update(deltaTime), drops.dropX - 100, drops.dropY, 350, 350);
            }
            else {
                game.batch.draw(drops.update(deltaTime), drops.dropX, drops.dropY, drops.width, drops.height);
            }
        }
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

        if(Players.get(0).PlayerHealth != 0) {
            switch (Players.get(0).PlayerGunAmmo) {
                case 5:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(5), 1725, 1012, 30, 33);
                    break;
                case 4:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(4), 1725, 1012, 30, 33);
                    break;
                case 3:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(3), 1725, 1012, 30, 33);
                    break;
                case 2:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(2), 1725, 1012, 30, 33);
                    break;
                case 1:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(1), 1725, 1012, 30, 33);
                    break;
                case 0:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(0), 1725, 1012, 30, 33);
                    break;
            }
        }




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

        if(Players.get(1).PlayerHealth != 0) {
            switch (Players.get(1).PlayerGunAmmo) {
                case 5:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(5), 170, 1012, 30, 33);
                    break;
                case 4:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(4), 170, 1012, 30, 33);
                    break;
                case 3:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(3), 170, 1012, 30, 33);
                    break;
                case 2:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(2), 170, 1012, 30, 33);
                    break;
                case 1:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(1), 170, 1012, 30, 33);
                    break;
                case 0:
                    game.batch.draw(AmmoNumbersTex.getIndexFrame(0), 170, 1012, 30, 33);
                    break;
            }
        }

    }

    public void DrawMenu(){

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !IsGUI) {
            IsGUI = true;
        }
        else if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && IsGUI){
            IsGUI = false;
            isPaused = false;
        }


        if(IsGUI){
            isPaused = true;
            game.batch.draw(guiMenu,MainMenuScreen.xCenter/2f+300f,MainMenuScreen.yCenter/2f+300f,400,400);
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

    public void DrawWinnerPlayer(float delta){

        // if any player dies draw PressSpace
        for(Player playerindex : Players){
            if(playerindex.state == Player.playerState.dead){
                game.batch.draw(PressSpace, 1080 - 550 / 2f, 600, 300, 55);
                // rematch the game
                if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
                    game.setScreen(new GameScreen(game));
                }
            }
        }


        if(Players.get(0).state == Player.playerState.dead && Players.get(1).state != Player.playerState.dead) {
            game.batch.draw(OrangePlayerWinAnimation.getFrame(delta), 1080 - 900 / 2f, 800, 700, 100);
        }
        if(Players.get(1).state == Player.playerState.dead && Players.get(0).state != Player.playerState.dead){
            game.batch.draw(BluePlayerWinAnimation.getFrame(delta), 1080 - 900 / 2f, 800, 700, 100);
        }


    }




    ////////////////////////
    // Collision functions
    ////////////////////////

    public void AmmoDropCollision(float delta){
        // ammo drop collision with grounds
        for(MapObject GroundIndex : Grounds){
            for(AmmoDrop DropIndex : AmmoDrops){
                // freeze if the drop on ground
                if(DropIndex.DropHitBox.overlaps(GroundIndex.hitBox)){
                    DropIndex.freeze = true;
                }
            }
        }

        // ammo drop collision with RadioActivePools
        for(MapObject RadioActivePoolIndex : RadioActivePools) {
            for (Iterator<AmmoDrop> Iter = AmmoDrops.iterator(); Iter.hasNext(); ) {
                AmmoDrop TempAmmoDrops = Iter.next();
                if (TempAmmoDrops.DropHitBox.overlaps(RadioActivePoolIndex.hitBox)) {
                    Iter.remove();
                }
                if(TempAmmoDrops.DeleteDrop){
                    Iter.remove();
                }
            }
        }

        // if bullet touches barrel:
        // remove the bullet and the barrel explodes
        for(Player playerIndex : Players) {
            for (Iterator<Bullet> BulletIter = playerIndex.getBullets().iterator(); BulletIter.hasNext(); ) {
                Bullet TempBullets = BulletIter.next();
                for (AmmoDrop TempAmmoDrops : AmmoDrops) {
                    if (TempAmmoDrops.DropHitBox.overlaps(TempBullets.hitBox)) {
                        TempAmmoDrops.freeze = true;
                        TempAmmoDrops.IsExplosion = true;
                        BulletIter.remove();
                    }
                }
            }
        }

        // removes barrel and increase PlayerGunAmmo
        for(Player playerIndex : Players) {
            for (Iterator<AmmoDrop> Iter = AmmoDrops.iterator(); Iter.hasNext(); ) {
                AmmoDrop AmmoDropsIndex = Iter.next();
                if (AmmoDropsIndex.DropHitBox.overlaps(playerIndex.PlayerHitBox) && playerIndex.PlayerGunAmmo != 5 && !AmmoDropsIndex.IsExplosion) {
                    playerIndex.PlayerGunAmmo = 5;
                    Iter.remove();
                }
                if(AmmoDropsIndex.DeleteDrop){
                    Iter.remove();
                }
            }
        }

        // kill the player if he touches the Explosion
        for(Player playerIndex : Players) {
            for (AmmoDrop DropIndex : AmmoDrops) {
                if (playerIndex.PlayerHitBox.overlaps(DropIndex.ExplosiveHitBox) && DropIndex.IsExplosion) {
                    playerIndex.PlayerHealth = 0;
                }
            }
        }


        // Spawns the drop in random X position every X time sec.
        timeDrop += delta;
        if (timeDrop >= 7f) {
            AmmoDrop drop = new AmmoDrop(MathUtils.random(0, 1900), 1920);
            AmmoDrops.add(drop);
            timeDrop = 0;
        }


    }

    public void PlayersBulletCollisionHandling(){

        // removes the bullet if it overlaps WorldBorder
        for (MapObject Borders : WorldBorders) {
            Array<Bullet> BluePlayerbullets = GameScreen.Players.get(0).getBullets();
            for(Iterator<Bullet> BlueIter = BluePlayerbullets.iterator(); BlueIter.hasNext();){
                Bullet TempBlueBullets = BlueIter.next();
                if(TempBlueBullets.hitBox.overlaps(Borders.hitBox)){
                    BlueIter.remove();
                }
            }
        }


        for (MapObject Borders : WorldBorders) {
            Array<Bullet> YellowPlayerbullets = GameScreen.Players.get(1).getBullets();
            for(Iterator<Bullet> YellowIter = YellowPlayerbullets.iterator(); YellowIter.hasNext();){
                Bullet TempYellowBullets = YellowIter.next();
                if(TempYellowBullets.hitBox.overlaps(Borders.hitBox)){
                    YellowIter.remove();
                }
            }
        }


    }




    ////////////////////////
    // MapObject functions
    ////////////////////////

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
