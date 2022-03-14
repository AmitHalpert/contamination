package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


public class Player {


    // the number of players
    public enum PlayersController{
        Blue,
        Orange
    }


    // the player states
    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }

    // Initializing players' final Variables
    public final float SHOOT_WAIT_TIME = 0.4f;
    public final int MOVEMENT_SPEED = 400;
    public final int JUMP_FORCE = 5;
    public final int GRAVITATIONAL_FORCE = 20;

    // player characteristics
    float PlayerX, PlayerY;
    int PlayerHealth;
    int width, height;
    double Xspeed, Yspeed;
    boolean isFacingLeft;
    boolean Collision;
    boolean IsPlayerOnGround;
    boolean IsPlayerFrozen;
    Rectangle PlayerHitBox;
    Rectangle PlayerBounds;

    // gun parameters
    float TimeBetweenShots;
    boolean isPlayerHoldingGun;
    Array<Bullet> bullets;

    // Animation parameters
    float dead_elapsedTime;
    float dead_animation_time;
    float idle_animation_time;
    Texture outputTexture;
    Texture playerTexture;

    // music and SFX
    private final Sound gunshot;


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

    // creates enums
    playerState state;
    PlayersController SelectedPlayer;

    public Player(float x, float y,PlayersController SelectedPlayer){

        this.PlayerX = x;
        this.PlayerY = y;
        this.SelectedPlayer = SelectedPlayer;



        PlayerHealth = 3;
        width = 170;
        height = 170;
        PlayerBounds = new Rectangle(x, y, width, height);



        //set up gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun1.wav"));

        // initialize player's settings
        isFacingLeft = false;
        isPlayerHoldingGun = true;
        Collision = false;
        IsPlayerOnGround = false;
        IsPlayerFrozen = false;
        state = playerState.Idle;
        idle_animation_time = 0;
        dead_animation_time = 0;
        dead_elapsedTime = 0;

        ////
        // set up the players animations
        ////

        // detect Player according to the PlayersController enum and apply the animation
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
                flipped_player_idle_animation.loadAnimation("flipped-Orange-Player-idle_gun_", 1);
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


        // default skin
        outputTexture = playerTexture;
    }



    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {
        if(!GameScreen.isPaused){


            // the player's Hit box for bullet collision
            PlayerHitBox = new Rectangle(PlayerX + 40, PlayerY, width, height-115);


            switch (SelectedPlayer){
                case Blue:
                    //Determine witch (playerState) state the player will be.
                    GetBluePlayerState();
                    // keyboard input and Player movement
                    BluePlayerInputHandling(delta);
                    break;

                case Orange:
                    //Determine witch (playerState) state the player will be.
                    GetOrangePlayerState();
                    // keyboard input and Player movement
                    OrangePlayerInputHandling(delta);
                    break;

            }




            // Detects if the player touches A MapObject
            collisionHandling(Ground,WorldBorder,RadioActivePool);

            if (!IsPlayerFrozen) {
                // updates the player X AND Y
                updatePlayerPosition();
            }
        }


        // checks which animation should play according to the Player's state
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
                    outputTexture = player_running_gun_animation.getFrame(0.002f);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_running_gun_animation.getFrame(0.002f);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_running_animation.getFrame(delta);

                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    idle_animation_time = 0;
                }
                else
                    outputTexture = player_running_animation.getFrame(delta);
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;


            case Jumping:

                if(isPlayerHoldingGun && !isFacingLeft){
                    outputTexture = player_jumping_gun_animation.getFrame(delta);

                    player_running_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_jumping_gun_animation.getFrame(delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){

                    outputTexture = flipped_player_jumping_animation.getFrame(delta);

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
                    outputTexture = player_jumping_animation.getFrame(delta);
                }
                player_running_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Idle:

                if(isPlayerHoldingGun && !isFacingLeft){

                    outputTexture = player_idle_gun_animation.getFrame(delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_idle_gun_animation.getFrame(delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_idle_animation.getFrame(delta);

                    flipped_player_running_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                }
                else

                if (idle_animation_time > 2f) {
                    outputTexture = player_idle_animation.getFrame(delta);

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
        if(Gdx.input.isKeyJustPressed(Input.Keys.N) && TimeBetweenShots >= SHOOT_WAIT_TIME && !GameScreen.isPaused && state != playerState.dead){
            TimeBetweenShots = 0;
            gunshot.play(0.01f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * delta;
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * delta;
            isFacingLeft = false;
        }



        //vertical input
        if ((Gdx.input.isKeyPressed(Input.Keys.UP)) && !GameScreen.isPaused && state != playerState.dead) {

            if (IsPlayerOnGround || Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE;
            }

        }
        Yspeed -= GRAVITATIONAL_FORCE * delta;

    }

    public void GetBluePlayerState() {


        // checks if the player is moving up or down
        if (Yspeed > 0) {
            state = playerState.Jumping;
        } else if (Yspeed < 0) {
            state = playerState.Jumping;
        } else {


            // checks if the player is moving left or right
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Yspeed == 0) {
                state = playerState.Running;
                // check if the player is not moving
            } else if (Yspeed == 0 && Xspeed == 0 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && TimeBetweenShots >= SHOOT_WAIT_TIME && !GameScreen.isPaused && state != playerState.dead){
            TimeBetweenShots = 0;
            gunshot.play(0.01f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = -MOVEMENT_SPEED * delta;
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed = MOVEMENT_SPEED * delta;
            isFacingLeft = false;
        }



        //vertical input
        if ((Gdx.input.isKeyPressed(Input.Keys.W)) && !GameScreen.isPaused && state != playerState.dead) {
            if (IsPlayerOnGround || Yspeed == -Yspeed) {
                Yspeed += JUMP_FORCE;
            }
        }
        Yspeed -= GRAVITATIONAL_FORCE * delta;

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
            } else if (Yspeed == 0 && Xspeed == 0 && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
                state = playerState.Idle;
            }
        }
        // checks if the player is dead
        if (PlayerHealth <= 0) {
            state = playerState.dead;
        }
    }

    // all players

    public void collisionHandling(Array<MapObject> Ground,Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {



        // Bullet collision
        // Blue Player
        Array<Bullet> bulletsB = GameScreen.Players.get(1).getBullets();
        for (Iterator<Bullet> iterb = bulletsB.iterator(); iterb.hasNext(); ) {
            Bullet bB = iterb.next();
            if (bB.hitBox.overlaps(GameScreen.Players.get(0).PlayerHitBox)) {
                GameScreen.Players.get(0).PlayerHealth--;
                iterb.remove();
            }
        }

        // Orange Player
        Array<Bullet> bulletsY = GameScreen.Players.get(0).getBullets();
        for (Iterator<Bullet> iter = bulletsY.iterator(); iter.hasNext(); ) {
            Bullet bY = iter.next();
            if (bY.hitBox.overlaps(GameScreen.Players.get(1).PlayerHitBox)) {
                GameScreen.Players.get(1).PlayerHealth--;
                iter.remove();
            }
        }



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
                Collision = true;
            }
            else {
                Collision = false;
            }

        }


        //vertical / grounds  Collision
        PlayerBounds.y += Yspeed;
        for (MapObject grounds : Ground) {
            if (PlayerBounds.overlaps(grounds.hitBox)) {
                Yspeed -= Yspeed;
                Collision = true;
                IsPlayerOnGround = true;
            }
            else {
                IsPlayerOnGround = false;
                Collision = false;
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
        bullet = new Bullet(PlayerX, PlayerY, isFacingLeft);
        bullets.add(bullet);
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