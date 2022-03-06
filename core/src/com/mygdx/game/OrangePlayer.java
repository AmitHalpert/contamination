package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


public class OrangePlayer {

    // the player states
    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }

    public final float SHOOT_WAIT_TIME = 0.4f;

    // Player's parameters
    float x, y;
    int PlayerHealth;
    int width, height;
    double Xspeed, Yspeed;

    boolean IsPlayerFrozen;
    boolean isFacingLeft;
    boolean Collision;
    // for jumping
    boolean IsPlayerOnGround;
    // gun parameters
    float TimeBetweenShots;
    boolean isPlayerHoldingGun;
    static Array<Bullet> bullets;
    Rectangle YellowBounds;
    Rectangle YellowHitBox;

    // Animation parameters
    float dead_elapsedTime;
    float dead_animation_time;
    float idle_animation_time;
    Texture outputTexture;
    Texture playerTexture;

    // music and SFX
    Sound gunshot;

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


    playerState state;

    public OrangePlayer(float x, float y){

        this.x = x;
        this.y = y;

        // player characteristics
        PlayerHealth = 3;
        width = 170;
        height = 170;
        YellowBounds = new Rectangle(x, y, width, height);


        //set up gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();

        // SFX
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
        // set up the player animations
        ////

        player_dead_animation = new ObjectAnimation();
        player_dead_animation.loadAnimation("Orange-Player-Death_",5);
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
        player_running_gun_animation.loadAnimation("Orange-Player-Running_Gun_",4);
        player_jumping_gun_animation = new ObjectAnimation();
        player_jumping_gun_animation.loadAnimation("Orange-Player-Jumping_Gun_",1);
        player_idle_gun_animation = new ObjectAnimation();
        player_idle_gun_animation.loadAnimation("Orange-Player-Idle-gun_",1);





        // player left animations
        flipped_player_running_animation = new ObjectAnimation();
        flipped_player_running_animation.loadAnimation("flipped-orange-running_",4);
        flipped_player_jumping_animation = new ObjectAnimation();
        flipped_player_jumping_animation.loadAnimation("flipped-Orange-Player-Jumping_", 1);
        flipped_player_idle_animation = new ObjectAnimation();
        flipped_player_idle_animation.loadAnimation("flipped-Orange-Player-idle_gun_", 1);
        // player with a gun left animations
        flipped_player_running_gun_animation = new ObjectAnimation();
        flipped_player_running_gun_animation.loadAnimation("flipped-Orange-Player-Running-gun_",4);
        flipped_player_jumping_gun_animation = new ObjectAnimation();
        flipped_player_jumping_gun_animation.loadAnimation("flipped-Orange-Player-Jumping-gun_",1);
        flipped_player_idle_gun_animation = new ObjectAnimation();
        flipped_player_idle_gun_animation.loadAnimation("flipped-Orange-Player-idle_gun_",1);


        // default skin
        outputTexture = playerTexture;
    }



    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {

        if(!GameScreen.isPaused){
        // the player's Hit box for bullet collision
        YellowHitBox = new Rectangle(x-50, y-170, width-50, height);

        //Determine witch (playerState) state the player will be.
        GetPlayerState();

        // keyboard input N Player movement
        PlayerInputHandling(delta);

        // Detects if the player touches A MapObject and changes speeds
        collisionHandling(Ground,WorldBorder,RadioActivePool);

        // updates the player X AND Y
        if(!IsPlayerFrozen) {
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
                    dead_elapsedTime++;
                }
                if(dead_elapsedTime >= 13){
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
                    outputTexture = player_running_gun_animation.getFrame(delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_running_gun_animation.getFrame(delta);

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



    //###
    // The player functions
    //###


    //Determine witch (playerState) state the player will be.
    public void GetPlayerState() {
        if(PlayerHealth <= 0){
            state = playerState.dead;
        }

        // checks if the player is moving up or down
        if (Yspeed > 0) {
            state = playerState.Jumping;
        }

        // checks if the player is moving left or right
        if (Xspeed != 0 && state != playerState.dead) {
            if (Xspeed != 0 && Yspeed == 0) {
                state = playerState.Running;
            }


        }
        else if (Xspeed == 0 && Yspeed == 0 && state != playerState.dead ) {
            state = playerState.Idle;
        }

    }

    // keyboard input N Player movement
    public void PlayerInputHandling(float delta) {
        TimeBetweenShots += delta;

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && TimeBetweenShots >= SHOOT_WAIT_TIME && !GameScreen.isPaused && state != playerState.dead){
            TimeBetweenShots = 0;
            gunshot.play(0.01f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.D)) && (Gdx.input.isKeyPressed(Input.Keys.A)) || !(Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed *= 0.8;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed--;
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !GameScreen.isPaused && state != playerState.dead) {
            Xspeed++;
            isFacingLeft = false;
        }


        //Smooths Player Movement
        if (Xspeed > 0 && Xspeed < 0.80) Xspeed = 0;
        if (Xspeed < 0 && Xspeed > -0.80) Xspeed = 0;
        if (Xspeed > 10) Xspeed = 10;
        if (Xspeed < -10) Xspeed = -10;


        //vertical input
        if ((Gdx.input.isKeyPressed(Input.Keys.W)) && !GameScreen.isPaused && state != playerState.dead) {
            YellowHitBox.y++;
            if (IsPlayerOnGround || Yspeed == -Yspeed) {
                Yspeed += 18;

            }
            YellowHitBox.y--;
        }
        Yspeed -= 0.9;

    }

    // Detects if the player touches A MapObject
    public void collisionHandling(Array<MapObject> Ground,Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {

        Array<Bullet> bullets = BluePlayer.getBullets();
        for(Iterator<Bullet> iter = bullets.iterator(); iter.hasNext();){
            Bullet b = iter.next();
            if(b.hitBox.overlaps(YellowHitBox)){
                PlayerHealth--;
                iter.remove();
            }
        }


        for(MapObject Pools : RadioActivePool){
            if(YellowBounds.overlaps(Pools.hitBox)){
                PlayerHealth = 0;
            }
        }

        //bounds Collision
        YellowBounds.x += Xspeed;
        for (MapObject borders: WorldBorder) {
            if (YellowBounds.overlaps(borders.hitBox)) {
                Xspeed -= Xspeed;
                Collision = true;
            }
            else {
                Collision = false;
            }

        }

        //Ground Collision
        YellowBounds.y += Yspeed;
        for (MapObject grounds : Ground) {
            if (YellowBounds.overlaps(grounds.hitBox)) {
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

    // Change the player X AND Y
    public void updatePlayerPosition(){
        //Updates X AND Y Position of the player

        x += Xspeed;
        y += Yspeed;


        YellowBounds.x = x;
        YellowBounds.y = y;

    }

    public void ShootBullets() {

        Bullet bullet;
        if(isFacingLeft){
            bullet = new Bullet(x, y - 35, true);
        }
        else {
            bullet = new Bullet(x, y - 35, false);
        }
        bullets.add(bullet);
    }

    public static Array<Bullet> getYellowPlayerBullets(){
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
