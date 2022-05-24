package com.amithalpert.contamination.Screens;

import com.amithalpert.contamination.Entities.Objects.Bullet;
import com.amithalpert.contamination.Entities.Player;
import com.amithalpert.contamination.Entities.Objects.AmmoDrop;
import com.amithalpert.contamination.Entities.Objects.Pool;
import com.amithalpert.contamination.Entities.Objects.BulletOld;
import com.amithalpert.contamination.Tools.*;
import com.amithalpert.contamination.contamination;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.GL20;


public class GameScreen implements Screen {

    final contamination game;




    private SpriteBatch batch;
    private static World world;
    private  Box2DDebugRenderer box2DDebugRenderer;


    private static Player player;
    private Bullet bullet;

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

        this.bullet = new Bullet(40,100, false);




        // ContactListener for collision
        world.setContactListener(new WorldContactListener());



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

        player.update();
        bullet.update();

        if(Gdx.input.isKeyPressed(Keys.L)){
            dispose();
            Gdx.app.exit();
        }

    }

    private void cameraUpdate(){
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * 16f * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y + 10 * 16f * 10) / 10f;
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

        player.update();

        ////////////////////////
        // Draw hierarchy
        ////////////////////////
        game.batch.begin();

        game.batch.draw(player.render(deltaTime),player.getBody().getPosition().x * 16 - (50 / 2f), player.getBody().getPosition().y * 16 - (player.getHeight() / 2f) -13,50,59);

        for(BulletOld bulletIndex : player.getBullets()){
            game.batch.draw(bulletIndex.update(deltaTime), bulletIndex.bulletX, bulletIndex.bulletY - 2, bulletIndex.width, bulletIndex.height);
        }

        game.batch.end();

        box2DDebugRenderer.render(world, camera.combined.scl(16f));
    }




    ////////////////////////
    // Draw functions
    ////////////////////////

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


    public static World getWorld() {
        return world;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public static Player getPlayer() {
        return player;
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
