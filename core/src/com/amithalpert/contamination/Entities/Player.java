package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Entities.Objects.Bullet;
import com.amithalpert.contamination.Tools.MapObject;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.amithalpert.contamination.Screens.PlayerVComputerScreen;

import java.util.Iterator;

/**
 * The Player is not an abstract class, but It's kinda trying to be, inside it there's two players:
 * The Player class have global functions that affect every player,
 * And functions that created for each player.
 */
public class Player {


    // the PlayersController is used for managing a lot of player,
    // and to make it easy to understand which player you are working with.
    public enum PlayersController{
        Blue,
        Orange
    }


    // the playerState is used to represent the current state of the player (check GetPlayerState function).
    // for example if playerState is Running the player animation will change to running (check the animation switch)
    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }

    // Initializing players' final Variables
    public final int PLAYER_WIDTH = 130;
    public final int PLAYER_HEIGHT = 130;

    public final float ANIMATIONS_TIME = 0.5f;
    public final float SHOOT_WAIT_TIME = 0.5f;
    public final int MOVEMENT_SPEED = 320;
    public final int JUMP_FORCE = 1050;
    public final float GRAVITATIONAL_FORCE = 15f;


    // player characteristics
    public float PlayerX;
    public float PlayerY;
    public int PlayerHealth;
    public int width;
    public int height;
    double Xspeed, Yspeed;
    boolean isFacingLeft;

    public boolean IsPlayerFrozen;
    public Rectangle PlayerBounds;

    // gun parameters
    public int PlayerGunAmmo;
    float TimeBetweenShots;
    boolean isPlayerHoldingGun;
    Array<Bullet> bullets;

    // Animation parameters
    float dead_elapsedTime;
    float dead_animation_time;
    float idle_animation_time;
    Texture outputTexture;
    Texture playerTexture;

    // SFX
    final Music PlayerDeathSound;
    final Sound gunshot;


    ////////////////////////
    // types of the player animation and textures
    ////////////////////////
    Texture player_not_exiting;
    // create ObjectAnimation for every type of animation
    ObjectAnimation player_dead_animation;
    // right animations
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    // player with gun animations
    ObjectAnimation player_running_gun_animation;
    ObjectAnimation player_jumping_gun_animation;
    ObjectAnimation player_idle_gun_animation;
    //left animations
    ObjectAnimation flipped_player_running_animation;
    ObjectAnimation flipped_player_jumping_animation;
    ObjectAnimation flipped_player_idle_animation;
    // left player with gun animations
    ObjectAnimation flipped_player_running_gun_animation;
    ObjectAnimation flipped_player_idle_gun_animation;
    ObjectAnimation flipped_player_jumping_gun_animation;

    // creates the enums
    public playerState state;
    public PlayersController SelectedPlayer;

    public Player(float x, float y, PlayersController SelectedPlayer){
        this.PlayerX = x;
        this.PlayerY = y;
        this.SelectedPlayer = SelectedPlayer;

        // Initializing player's characteristics
        PlayerGunAmmo = 5;
        PlayerHealth = 3;
        width = PLAYER_WIDTH;
        height = PLAYER_HEIGHT;
        PlayerBounds = new Rectangle(PlayerX, PlayerY, 50, 70);

        isFacingLeft = false;
        isPlayerHoldingGun = false;
        IsPlayerFrozen = false;
        state = playerState.Idle;
        idle_animation_time = 0;
        dead_animation_time = 0;
        dead_elapsedTime = 0;
        PlayerDeathSound = Gdx.audio.newMusic(Gdx.files.internal("death-sound.mp3"));
        PlayerDeathSound.setVolume(0.3f);


        // Initializing gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun-shot-fx.wav"));

        ////////////////////////
        // set up the players animations
        ////////////////////////


                player_dead_animation = new ObjectAnimation();
                player_dead_animation.loadAnimation("player_dead_", 5);

                player_not_exiting = new Texture("player_dead_5.png");

                // player right animations
                player_running_animation = new ObjectAnimation();
                player_running_animation.loadAnimation("player_running_", 4);
                player_jumping_animation = new ObjectAnimation();
                player_jumping_animation.loadAnimation("player_jumping_", 2);
                player_idle_animation = new ObjectAnimation();
                player_idle_animation.loadAnimation("player_idle_", 1);
                playerTexture = new Texture(Gdx.files.internal("player_idle_1.png"));

                // player with a gun right animations
                player_running_gun_animation = new ObjectAnimation();
                player_running_gun_animation.loadAnimation("player_running_with_gun_", 4);
                player_jumping_gun_animation = new ObjectAnimation();
                player_jumping_gun_animation.loadAnimation("player_running_with_gun_", 1);
                player_idle_gun_animation = new ObjectAnimation();
                player_idle_gun_animation.loadAnimation("player_idle_gun_", 1);

                // player left animations
                flipped_player_running_animation = new ObjectAnimation();
                flipped_player_running_animation.loadAnimation("fliped_player_running_", 4);
                flipped_player_jumping_animation = new ObjectAnimation();
                flipped_player_jumping_animation.loadAnimation("fliped_player_jumping_", 2);
                flipped_player_idle_animation = new ObjectAnimation();
                flipped_player_idle_animation.loadAnimation("fliped_player_idle_", 1);
                // player with a gun left animations
                flipped_player_running_gun_animation = new ObjectAnimation();
                flipped_player_running_gun_animation.loadAnimation("flipped_player_running_with_gun_", 4);
                flipped_player_jumping_gun_animation = new ObjectAnimation();
                flipped_player_jumping_gun_animation.loadAnimation("flipped_player_running_with_gun_", 1);
                flipped_player_idle_gun_animation = new ObjectAnimation();
                flipped_player_idle_gun_animation.loadAnimation("flipped_player_idle_gun_", 1);



        // it is important to set the outputTexture to not be Null
        outputTexture = playerTexture;
    }



    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder, Array<MapObject> RadioActivePool) {
        // freezes everything
        if(!PlayerVComputerScreen.isPaused){


            //Determine which (playerState) state the player will be.
            GetBluePlayerState();
            // keyboard input and Player movement
            BluePlayerInputHandling(delta);



            // Detects if the player touches A MapObject
            collisionHandling(delta,Ground,WorldBorder,RadioActivePool);

           // freeze player to the current position
            if (!IsPlayerFrozen) {
                // updates the player X AND Y
                updatePlayerPosition();
            }
        }

        // PlayerParametersHandling is used to set limits to parameters.
        PlayerParametersHandling();


        ////////////////////////
        // The animation switch:
        ////////////////////////

        // checks which animation should play according to the Player's state
        // and outputs the animation
        switch (state) {

            case dead:
                PlayerDeathSound.play();
                IsPlayerFrozen = true;
                dead_animation_time += delta;
                if(dead_animation_time >= 0.04f) {
                    outputTexture = player_dead_animation.getFrame(delta);
                    dead_animation_time = 0;
                    dead_elapsedTime += delta;
                }
                if(dead_elapsedTime >= 0.2f){
                    // removes player
                    outputTexture = player_not_exiting;
                    this.dispose();
                }

                player_idle_gun_animation.resetAnimation();
                player_running_gun_animation.resetAnimation();
                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                flipped_player_jumping_animation.resetAnimation();
                flipped_player_idle_animation.resetAnimation();
                flipped_player_running_animation.resetAnimation();
                break;


            case Running:
                // gun running animation
                if(isPlayerHoldingGun && !isFacingLeft){
                    outputTexture = player_running_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_running_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_running_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    idle_animation_time = 0;
                }
                else
                    outputTexture = player_running_animation.getFrame(ANIMATIONS_TIME * delta);
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;


            case Jumping:

                if(isPlayerHoldingGun && !isFacingLeft){
                    outputTexture = player_jumping_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_jumping_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){

                    outputTexture = flipped_player_jumping_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    idle_animation_time = 0;
                }
                else

                if (player_jumping_animation.currentFrame >= player_jumping_animation.frames.size - 2) {
                    outputTexture = player_jumping_animation.frames.get(player_jumping_animation.currentFrame);
                } else {
                    outputTexture = player_jumping_animation.getFrame(ANIMATIONS_TIME * delta);
                }
                player_running_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Idle:

                if(isPlayerHoldingGun && !isFacingLeft){

                    outputTexture = player_idle_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_idle_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_idle_animation.getFrame(ANIMATIONS_TIME * delta);

                    flipped_player_running_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                }
                else

                if (idle_animation_time > 2f) {
                    outputTexture = player_idle_animation.getFrame(ANIMATIONS_TIME * delta);

                    if (idle_animation_time > 3f) {
                        idle_animation_time = 0;
                    }
                } else outputTexture = playerTexture;

                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                idle_animation_time += delta;
                break;
        }

        // Returns the (Player state) animation
        return outputTexture;
    }



    //###################
    // The player functions
    //###################


    // blue player
    public void BluePlayerInputHandling(float delta) {

        // weapon input
        TimeBetweenShots += delta;
        if(Gdx.input.isKeyJustPressed(Input.Keys.N) && TimeBetweenShots >= SHOOT_WAIT_TIME && PlayerGunAmmo != 0 && state != playerState.dead){
            TimeBetweenShots = 0;
            PlayerGunAmmo--;
            gunshot.play(0.3f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = false;
        }


        //vertical input
        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {

            if (Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE * Gdx.graphics.getDeltaTime();
            }

        }
        Yspeed -= GRAVITATIONAL_FORCE * Gdx.graphics.getDeltaTime();

    }

    public void GetBluePlayerState() {


        // checks if the player is moving up or down
        if (Yspeed > 0) {
            state = playerState.Jumping;
        } else if (Yspeed < 0) {
            state = playerState.Jumping;
        } else {


            // checks if the player is moving left or right
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Yspeed == 0 && !(Gdx.input.isKeyJustPressed(Input.Keys.UP))){
                state = playerState.Running;
                // check if the player is not moving
            } else if (Yspeed == 0 && Xspeed == 0 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                state = playerState.Idle;
            }
        }
        // checks if the player is dead
        if (PlayerHealth <= 0) {
            state = playerState.dead;
        }

    }

    // orange player
    public void OrangePlayerInputHandling(float delta) {
        TimeBetweenShots += delta;

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && TimeBetweenShots >= SHOOT_WAIT_TIME && PlayerGunAmmo != 0 && !PlayerVComputerScreen.isPaused && state != playerState.dead){
            TimeBetweenShots = 0;
            PlayerGunAmmo--;
            gunshot.play(0.3f);
            ShootBullets();
        }


        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = false;
        }


        //vertical input
        if ((Gdx.input.isKeyJustPressed(Input.Keys.W)) && !PlayerVComputerScreen.isPaused && state != playerState.dead) {
            if (Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE * Gdx.graphics.getDeltaTime();
            }
        }
        Yspeed -= GRAVITATIONAL_FORCE * Gdx.graphics.getDeltaTime();
    }

    public void GetOrangePlayerState() {

        // checks if the player is moving up or down
        if (Yspeed > 0) {
            state = playerState.Jumping;
        } else if (Yspeed < 0) {
            state = playerState.Jumping;
        } else {


            // checks if the player is moving left or right
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D) && Yspeed == 0) {
                state = playerState.Running;
                // check if the player is not moving
            } else if (Yspeed == 0 && Xspeed == 0 && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                state = playerState.Idle;
            }
        }
        // checks if the player is dead
        if (PlayerHealth <= 0) {
            state = playerState.dead;
        }
    }

    public void collisionHandling(float delta, Array<MapObject> Ground,Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {


        // Bullet collision
        Array<Bullet> EnemyBullets = PlayerVComputerScreen.enemy.bullets;
        for (Iterator<Bullet> iter = EnemyBullets.iterator(); iter.hasNext(); ) {
            Bullet B = iter.next();
            if (B.hitBox.overlaps(PlayerBounds)) {
                PlayerHealth--;
                iter.remove();
            }
        }



        // horizontal & borders Collision
        PlayerBounds.x += Xspeed;
        for (MapObject borders: WorldBorder) {
            if (PlayerBounds.overlaps(borders.hitBox)) {
                Xspeed -= Xspeed;
            }
        }


        // vertical & grounds  Collision
        PlayerBounds.y += Yspeed;
        for (MapObject grounds : Ground) {
            if (PlayerBounds.overlaps(grounds.hitBox)) {
                Yspeed -= Yspeed;
            }
        }
    }

    public void updatePlayerPosition(){

        //Updates X AND Y Position of the player
        PlayerX += Xspeed;
        PlayerY += Yspeed;

        PlayerBounds.x = PlayerX;
        PlayerBounds.y = PlayerY;

    }

    public void ShootBullets() {
        // creates a new bullet and add it to the array
        Bullet bullet;

        // check isFacingLeft and adjust where the bullet coming from.
        if(isFacingLeft){
            bullet = new Bullet(PlayerX + 170, PlayerY, true);
        }else{
            bullet = new Bullet(PlayerX, PlayerY, false);
        }

        bullets.add(bullet);
    }

    public void PlayerParametersHandling(){
        // stops the player when he's not moving
        Xspeed = 0;

        // if the player have ammo, diff from zero then true
        // this statement is for the animation switch
        isPlayerHoldingGun = PlayerGunAmmo != 0;

        // limit health
        if(PlayerHealth > 3){
            PlayerHealth = 3;
        }

        // limit ammo
        if(PlayerGunAmmo > 5){
            PlayerGunAmmo = 5;
        }

        // limit the jump force
        if(Yspeed > 1050) Yspeed = 1050;

    }

    public Array<Bullet> getBullets(){
        return bullets;
    }

    public void dispose(){

        gunshot.dispose();
        PlayerDeathSound.dispose();
        player_dead_animation.dispose();
        player_running_animation.dispose();
        player_jumping_animation.dispose();
        player_idle_animation.dispose();
        playerTexture.dispose();
        player_running_gun_animation.dispose();
        player_jumping_gun_animation.dispose();
        player_idle_gun_animation.dispose();

        flipped_player_running_animation.dispose();
        flipped_player_jumping_animation.dispose();
        flipped_player_idle_animation.dispose();
        flipped_player_running_gun_animation.dispose();
        flipped_player_jumping_gun_animation.dispose();
        flipped_player_idle_gun_animation.dispose();
    }

}