package com.mygdx.game.Sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Tools.ObjectAnimation;
import com.mygdx.game.Tools.MapObject;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Sprites.Objects.Bullet;
import com.mygdx.game.Sprites.Objects.AmmoDrop;

import java.util.Iterator;

/**
 * The Player is not an abstract class, but It's trying to be, inside it there's two players:
 * The Player class have global functions that affect every player,
 * And functions that created for each player purposely.
 *
 */
public class Player {


    // the PlayersController is used for managing a lot of player,
    // and to make it easy to understand which player you are working with.
    public enum PlayersController{
        Blue,
        Orange
    }


    // the playerState is used to represent the current state the player is in through the GetPlayerState function.
    // for example if playerState is Running the player animation will change to running
    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }

    // Initializing players' final Variables
    public final float ANIMATIONS_TIME = 0.5f;
    public final float SHOOT_WAIT_TIME = 0.4f;
    public final int MOVEMENT_SPEED = 320;
    public final int JUMP_FORCE = 7;
    public final float GRAVITATIONAL_FORCE = 15f;

    // player characteristics
    public float PlayerX;
    public float PlayerY;
    public int PlayerHealth;
    public int width;
    public int height;
    double Xspeed, Yspeed;
    boolean isFacingLeft;
    boolean Collision;
    boolean IsPlayerFrozen;
    public Rectangle PlayerHitBox;
    Rectangle PlayerBounds;


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
    final Sound gunshot;


    /////
    // types of the player animation and textures
    ////
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

    public Player(float x, float y,PlayersController SelectedPlayer){
        this.PlayerX = x;
        this.PlayerY = y;
        this.SelectedPlayer = SelectedPlayer;

        // Initializing player's characteristics
        PlayerGunAmmo = 5;
        PlayerHealth = 3;
        width = 170;
        height = 170;
        PlayerBounds = new Rectangle(x, y, width, height);

        isFacingLeft = false;
        isPlayerHoldingGun = false;
        Collision = false;
        IsPlayerFrozen = false;
        state = playerState.Idle;
        idle_animation_time = 0;
        dead_animation_time = 0;
        dead_elapsedTime = 0;


        // Initializing gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun1.wav"));

        ////
        // set up the players animations
        ////
        // detect Player according to the PlayersController enum and apply the animations
        switch (SelectedPlayer) {

            case Orange:

                player_dead_animation = new ObjectAnimation();
                player_dead_animation.loadAnimation("Orange-Player-Death_", 5);
                player_not_exiting = new Texture("player_dead_5.png");

                // player right animations
                player_running_animation = new ObjectAnimation();
                player_running_animation.loadAnimation("Orange-Player-Running_", 4);
                player_jumping_animation = new ObjectAnimation();
                player_jumping_animation.loadAnimation("Orange-Player-Jumping_", 2);
                player_idle_animation = new ObjectAnimation();
                player_idle_animation.loadAnimation("Orange-Player-Idle_", 1);
                playerTexture = new Texture(Gdx.files.internal("Orange-Player-Idle_1.png"));

                // player with a gun right animations
                player_running_gun_animation = new ObjectAnimation();
                player_running_gun_animation.loadAnimation("Orange-Player-Running_Gun_", 4);
                player_jumping_gun_animation = new ObjectAnimation();
                player_jumping_gun_animation.loadAnimation("Orange-Player-Jumping_Gun_", 1);
                player_idle_gun_animation = new ObjectAnimation();
                player_idle_gun_animation.loadAnimation("Orange-Player-Idle-gun_", 1);

                // player left animations
                flipped_player_running_animation = new ObjectAnimation();
                flipped_player_running_animation.loadAnimation("flipped-orange-running_", 4);
                flipped_player_jumping_animation = new ObjectAnimation();
                flipped_player_jumping_animation.loadAnimation("flipped-Orange-Player-Jumping_", 1);
                flipped_player_idle_animation = new ObjectAnimation();
                flipped_player_idle_animation.loadAnimation("flipped-Orange-Player-idle_", 1);
                // player with a gun left animations
                flipped_player_running_gun_animation = new ObjectAnimation();
                flipped_player_running_gun_animation.loadAnimation("flipped-Orange-Player-Running-gun_", 4);
                flipped_player_jumping_gun_animation = new ObjectAnimation();
                flipped_player_jumping_gun_animation.loadAnimation("flipped-Orange-Player-Jumping-gun_", 1);
                flipped_player_idle_gun_animation = new ObjectAnimation();
                flipped_player_idle_gun_animation.loadAnimation("flipped-Orange-Player-idle_gun_", 1);
                break;


            case Blue:

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
        }


        // it is important to set the outputTexture to not be Null
        outputTexture = playerTexture;
    }



    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder, Array<MapObject> RadioActivePool) {
        // freezes everything
        if(!GameScreen.isPaused){


           if(state != playerState.dead ) {
               // the player's Hit box for bullet collision
               PlayerHitBox = new Rectangle(PlayerX + 40, PlayerY, width, height - 115);
           }
           else{
               // teleports "removes" the PlayerHitBox if he is dead
               PlayerHitBox.x = 3000;

           }



            // selects the correct functions for the selected Player(color)
            switch (SelectedPlayer){
                case Blue:
                    //Determine which (playerState) state the player will be.
                    GetBluePlayerState();
                    // keyboard input and Player movement
                    BluePlayerInputHandling(delta);
                    break;

                case Orange:
                    //Determine which (playerState) state the player will be.
                    GetOrangePlayerState();
                    // keyboard input and Player movement
                    OrangePlayerInputHandling(delta);
                    break;
            }


            // Detects if the player touches A MapObject
            collisionHandling(delta,Ground,WorldBorder,RadioActivePool);

           // freeze player to the current position
            if (!IsPlayerFrozen) {
                // updates the player X AND Y
                updatePlayerPosition();
            }
        }

        // PlayerParametersHandling is used to set limits to parameters: gun-ammo,etc.
        PlayerParametersHandling();


        //###################
        // The animation switch:
        //###################

        // checks which animation should play according to the Player's state
        // and outputs the animation
        switch (state) {

            case dead:

                IsPlayerFrozen = true;
                dead_animation_time += delta;
                if(dead_animation_time >= 0.04f) {
                    outputTexture = player_dead_animation.getFrame(delta);
                    dead_animation_time = 0;
                    dead_elapsedTime += delta;
                }
                if(dead_elapsedTime >= 0.2f){
                    outputTexture = player_not_exiting;
                    dispose();
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
            gunshot.play(0.01f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = false;
        }


        //vertical input
        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP)) && !GameScreen.isPaused && state != playerState.dead) {

            if (Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE;
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && TimeBetweenShots >= SHOOT_WAIT_TIME && PlayerGunAmmo != 0 && !GameScreen.isPaused && state != playerState.dead){
            TimeBetweenShots = 0;
            PlayerGunAmmo--;
            gunshot.play(0.01f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = false;
        }



        //vertical input
        if ((Gdx.input.isKeyJustPressed(Input.Keys.W)) && !GameScreen.isPaused && state != playerState.dead) {
            if (Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE;
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


        /////
        // Bullet collision
        /////

        switch (SelectedPlayer) {
            case Blue:
                 // Blue Player
               Array<Bullet> bulletsB = GameScreen.Players.get(1).getBullets();
               for (Iterator<Bullet> iterb = bulletsB.iterator(); iterb.hasNext(); ) {
                   Bullet bB = iterb.next();
                   if (bB.hitBox.overlaps(GameScreen.Players.get(0).PlayerHitBox)) {
                       GameScreen.Players.get(0).PlayerHealth--;
                       iterb.remove();
                }
            }
            break;

            case Orange:
                 // Orange Player
                 Array<Bullet> bulletsY = GameScreen.Players.get(0).getBullets();
                 for (Iterator<Bullet> iter = bulletsY.iterator(); iter.hasNext(); ) {
                     Bullet bY = iter.next();
                     if (bY.hitBox.overlaps(GameScreen.Players.get(1).PlayerHitBox)) {
                         GameScreen.Players.get(1).PlayerHealth--;
                         iter.remove();
                     }
            }
            break;

        }


        // kills you if you touch RadioActivePool
        for(MapObject Pools : RadioActivePool){
            if(PlayerBounds.overlaps(Pools.hitBox)){
                PlayerHealth = 0;
            }
        }

        //horizontal / borders Collision
        PlayerBounds.x += Xspeed;
        for (MapObject borders: WorldBorder) {
            if (PlayerBounds.overlaps(borders.hitBox)) {
                Xspeed -= Xspeed;

            }
        }


        //vertical / grounds  Collision
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
            bullet = new Bullet(PlayerX + 220, PlayerY, true);
        }else{
            bullet = new Bullet(PlayerX + 50, PlayerY, false);
        }


        bullets.add(bullet);
    }

    public void PlayerParametersHandling(){

        // stops the player if he's now moving
        Xspeed = 0;

        // if player don't have ammo set isPlayerHoldingGun to FALSE
        // if the player have ammo set to TRUE
        isPlayerHoldingGun = PlayerGunAmmo != 0;

        // limit health
        if(PlayerHealth > 3){
            PlayerHealth = 3;
        }

        // limit ammo
        if(PlayerGunAmmo > 5){
            PlayerGunAmmo = 5;
        }
    }

    public Array<Bullet> getBullets(){
        return bullets;
    }

    public void dispose(){
        gunshot.dispose();
        player_dead_animation.dispose();
        player_running_animation.dispose();
        player_jumping_animation.dispose();
        player_idle_animation.dispose();
        player_idle_gun_animation.dispose();
        player_jumping_gun_animation.dispose();
        player_running_gun_animation.dispose();

        flipped_player_running_gun_animation.dispose();
        flipped_player_jumping_gun_animation.dispose();
        flipped_player_idle_gun_animation.dispose();
        flipped_player_running_animation.dispose();
        flipped_player_jumping_animation.dispose();
        flipped_player_idle_animation.dispose();

    }

}