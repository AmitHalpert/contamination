package com.amithalpert.contamination.Screens;

import com.amithalpert.contamination.Entities.Gamer;
import com.amithalpert.contamination.Entities.Objects.AmmoDrop;
import com.amithalpert.contamination.Entities.Objects.Pool;
import com.amithalpert.contamination.Entities.Objects.Bullet;
import com.amithalpert.contamination.Entities.Player;
import com.amithalpert.contamination.Tools.MapBorder;
import com.amithalpert.contamination.Tools.TileMapHelper;
import com.amithalpert.contamination.contamination;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.GL20;
import com.amithalpert.contamination.Tools.ObjectAnimation;

import java.util.Iterator;


public class GameScreen implements Screen {

    final contamination game;


    Texture Tex;


    private SpriteBatch batch;
    private  World world;
    private  Box2DDebugRenderer box2DDebugRenderer;


    private Gamer gamer;

    // Main menu features
    float deltaTime;
    boolean IsGUI;
    public static boolean isPaused;
    float DropTimer;


    // SFX and music
    Music GameAmbience;

    // Screen
    OrthographicCamera camera;

    // Graphics
    Texture PressSpace;
    ObjectAnimation DrawAnimation;
    ObjectAnimation BluePlayerWinAnimation;
    ObjectAnimation OrangePlayerWinAnimation;
    ObjectAnimation AmmoNumbersTex;
    ObjectAnimation LeftPlayerHealthHUD;
    ObjectAnimation RightPlayerHealthHUD;
    ObjectAnimation RadioActivePoolAnimation;
    Texture background;
    Texture guiMenu;

    // world size
    static final float WORLD_WIDTH = Gdx.graphics.getWidth();
    static final float WORLD_HEIGHT = Gdx.graphics.getHeight();


    // The players Array
    public static Array<Player> Players;

    // tiled map
    private OrthogonalTiledMapRenderer Renderer;
    private TileMapHelper tileMapHelper;

    //map objects


    Array<Pool> Pools;
    Array<AmmoDrop> AmmoDrops;
    Array<MapBorder> Grounds;
    Array<MapBorder> WorldBorders;




    public GameScreen(final contamination game){
        this.game =  game;

        ////////////////////////
        // Renderers
        ////////////////////////
        // Camera and viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 320);
        batch = new SpriteBatch();

        ////////////////////////
        // box2d
        ////////////////////////
        world = new World(new Vector2(0,-25f), false);
        box2DDebugRenderer = new Box2DDebugRenderer();

        // tilemap
        this.tileMapHelper = new TileMapHelper(this);
        this.Renderer = tileMapHelper.setupMap();



        Tex = new Texture("player_idle_1.png");

        ////////////////////////
        // Set up parameters
        ////////////////////////
        IsGUI = false;
        isPaused = false;
        deltaTime = 0;
        DropTimer = 0;

        ////////////////////////
        // Set up players
        ////////////////////////
        Players = new Array<>();
        Players.add(new Player(400,500,Player.PlayersController.Orange));
        Players.add(new Player(400,500,Player.PlayersController.Blue));


        ////////////////////////
        // Set up SFX
        ////////////////////////
        GameAmbience = Gdx.audio.newMusic(Gdx.files.internal("GameAmbience.mp3"));
        GameAmbience.setLooping(true);
        GameAmbience.setVolume(0.06f);
        GameAmbience.play();

        ////////////////////////
        // Set up Graphics
        ////////////////////////
        // the map
        background = new Texture("genesis.png");

        // paused game menu
        guiMenu = new Texture("menugui.png");

        // Winner indicator
        PressSpace = new Texture("press-space.png");
        DrawAnimation = new ObjectAnimation();
        DrawAnimation.loadAnimation("draw-animation_",17);
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
        // Set up Map Objects
        ////////////////////////
        // Creates and places the map Objects
        Grounds = new Array<>();
        WorldBorders = new Array<>();
        AmmoDrops = new Array<>();
        Pools = new Array<>();
    }

    @Override
    public void show() {
    }

    private void update(){
        world.step(1 / 60f, 6, 2);
        cameraUpdate();

        game.batch.setProjectionMatrix(camera.combined);
        Renderer.setView(camera);

        gamer.update();

        if(Gdx.input.isKeyPressed(Keys.L)){
            dispose();
            Gdx.app.exit();
        }

    }

    private void cameraUpdate(){
        Vector3 position = camera.position;
        position.x = Math.round(gamer.getBody().getPosition().x * 16f * 10) / 10f;
        position.y = Math.round(gamer.getBody().getPosition().y * 16f * 10) / 10f;
        camera.position.set(position);


        camera.update();
    }


    @Override
    public void render(float deltaTime) {
        this.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();


        Renderer.render();


        // Pauses everything that is delta time affected.
        if(isPaused){
            deltaTime = 0;
        }

        gamer.update();

        ////////////////////////
        // Draw hierarchy
        ////////////////////////
        game.batch.begin();


        game.batch.draw(gamer.render(deltaTime),(gamer.getBody().getPosition().x * 16) - (50 / 2f), gamer.getBody().getPosition().y * 16 - (gamer.getHeight() / 2f) -12,50,59);


        game.batch.end();




    }




    ////////////////////////
    // Draw functions
    ////////////////////////


    public void DrawPlayers(){
        for (Player players : Players) {
            game.batch.draw(players.render(Gdx.graphics.getDeltaTime(), Grounds, WorldBorders, Pools), players.PlayerX, players.PlayerY, players.width, players.height);
        }

    }

    public void DrawAmmoDrops(float deltaTime){
        // draws the ammo drops
        for (AmmoDrop drops : AmmoDrops) {
            if(drops.IsExplosion){
                game.batch.draw(drops.update(deltaTime), drops.dropX - 100, drops.dropY - 100, 350, 350);
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
        game.batch.draw(Players.get(0).render(deltaTime, Grounds, WorldBorders, Pools), 1760,950,Players.get(0).width,Players.get(0).height);

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

        game.batch.draw(Players.get(1).render(deltaTime, Grounds, WorldBorders, Pools), -10,950,Players.get(1).width,Players.get(1).height);

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
            GameAmbience.play();
        }


        if(IsGUI){
            isPaused = true;
            GameAmbience.pause();
            game.batch.draw(guiMenu,MainMenuScreen.xCenter/2f+300f,MainMenuScreen.yCenter/2f+300f,400,400);
            //exit button

            // exit
            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 290 + 100 + 300 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 290 + 300){
                if(Gdx.input.isTouched()){

                    GameAmbience.stop();
                    dispose();
                    game.setScreen(new MainMenuScreen(game));
                }
            }

            // resume button
            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 500 + 100 + 300  && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 530 + 300){
                if(Gdx.input.isTouched()){
                    isPaused = false;
                    IsGUI = false;
                }
            }
            // options
            if(Gdx.input.getX() < MainMenuScreen.xCenter+100 && Gdx.input.getX() > MainMenuScreen.xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 400 + 100 + 300 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 400+40 + 300){
                if(Gdx.input.justTouched()){

                    }
                }
            }
        }

    public void DrawPlayersBullets(){

        // draws the blue's player bullets
        for(Bullet BluebulletsIndex : Players.get(0).getBullets()){
            game.batch.draw(BluebulletsIndex.update(deltaTime, Grounds, WorldBorders), BluebulletsIndex.bulletX, BluebulletsIndex.bulletY, BluebulletsIndex.width, BluebulletsIndex.height);
        }


        // draws the orange's player bullets
        for(Bullet OrangebulletsIndex : Players.get(1).getBullets()){
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

        // Add timer //

        if(Players.get(0).state == Player.playerState.dead && Players.get(1).state != Player.playerState.dead) {
            game.batch.draw(OrangePlayerWinAnimation.getFrame(delta), 1080 - 900 / 2f, 800, 700, 100);
        }
        else if(Players.get(1).state == Player.playerState.dead && Players.get(0).state != Player.playerState.dead){
            game.batch.draw(BluePlayerWinAnimation.getFrame(delta), 1080 - 900 / 2f, 800, 700, 100);
        }
        else if(Players.get(1).state == Player.playerState.dead && Players.get(0).state == Player.playerState.dead){
            // Draw, draw animation (both player lose)
            game.batch.draw(DrawAnimation.getFrame(delta), 1080 - 450 / 2f, 800, 200, 100);
        }

    }




    ////////////////////////
    // Collision functions
    ////////////////////////

    public void AmmoDropCollision(float delta){
        // ammo drop collision with grounds
        for(MapBorder GroundIndex : Grounds){
            for(AmmoDrop DropIndex : AmmoDrops){
                // freeze if the drop on ground
                if(DropIndex.DropHitBox.overlaps(GroundIndex.hitBox)){
                    DropIndex.freeze = true;
                }
            }
        }

        // ammo drop collision with RadioActivePools
        for(Pool RadioActivePoolIndex : Pools) {
            for (Iterator<AmmoDrop> Iter = AmmoDrops.iterator(); Iter.hasNext(); ) {
                AmmoDrop TempAmmoDrops = Iter.next();
                if (TempAmmoDrops.DropHitBox.overlaps(RadioActivePoolIndex.PoolHitBox)) {
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
        DropTimer += delta;
        if (DropTimer >= 5f) {
            AmmoDrop drop = new AmmoDrop(MathUtils.random(0, 1900), 1920);
            AmmoDrops.add(drop);
            DropTimer = 0;
        }

    }

    public void PlayersBulletCollisionHandling(){
        // removes the bullet if it overlaps WorldBorder
        for(Player playerIndex : Players) {
            for (MapBorder Borders : WorldBorders) {
                Array<Bullet> Playerbullets = playerIndex.getBullets();
                for (Iterator<Bullet> Iter = Playerbullets.iterator(); Iter.hasNext(); ) {
                    Bullet TempBullet = Iter.next();
                    if (TempBullet.hitBox.overlaps(Borders.hitBox)) {
                        Iter.remove();
                    }
                }
            }
        }


    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Gamer gamer){
        this.gamer = gamer;
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

        DrawAnimation.dispose();
        OrangePlayerWinAnimation.dispose();
        BluePlayerWinAnimation.dispose();
        AmmoNumbersTex.dispose();
        BluePlayerWinAnimation.dispose();
        PressSpace.dispose();
        RightPlayerHealthHUD.dispose();
        LeftPlayerHealthHUD.dispose();
        RadioActivePoolAnimation.dispose();
        GameAmbience.dispose();
        guiMenu.dispose();
        background.dispose();
    }
}
