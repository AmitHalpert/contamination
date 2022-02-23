package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.lang.*;
import java.util.LinkedList;


public class Player {

    // the player states
    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }

    // Player parameters
    float x, y;
    int width, height;
    double Xspeed, Yspeed;
    Rectangle hitBox;
    static boolean isFacingLeft;
    boolean Collision;
    boolean IsPlayerOnGround;
    boolean isPlayerHoldingGun;
    boolean isPlayerShooting;
    static Array<Bullet> bullets;

    // Animation parameters
    float idle_animation_time;
    Texture outputTexture;
    Texture playerTexture;

    // music and SFX
    private Sound gunshot;

    // create ObjectAnimation for every type of animation
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    // player with gun animations
    ObjectAnimation player_running_gun_animation;
    ObjectAnimation player_jumping_gun_animation;
    ObjectAnimation player_idle_gun_animation;

    //left Animation
    ObjectAnimation flipped_player_running_animation;
    ObjectAnimation flipped_player_jumping_animation;
    ObjectAnimation flipped_player_idle_animation;
    // left player with gun animations
    ObjectAnimation flipped_player_running_gun_animation;
    ObjectAnimation flipped_player_idle_gun_animation;
    ObjectAnimation flipped_player_jumping_gun_animation;



    playerState state;

    public Player(float x, float y){

        this.x = x;
        this.y = y;

        width = 170;
        height = 170;
        hitBox = new Rectangle(x, y, width, height);

        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun1.wav"));

        // initialize player's settings
        isFacingLeft = false;
        isPlayerHoldingGun = true;
        Collision = false;
        IsPlayerOnGround = false;
        isPlayerShooting = false;
        state = playerState.Idle;
        idle_animation_time = 0;


        // set up the player animations

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
        player_running_gun_animation.loadAnimation("player_running_with_gun_",4);
        player_jumping_gun_animation = new ObjectAnimation();
        player_jumping_gun_animation.loadAnimation("player_running_with_gun_",1);
        player_idle_gun_animation = new ObjectAnimation();
        player_idle_gun_animation.loadAnimation("player_idle_gun_",1);

        // player left animations
        flipped_player_running_animation = new ObjectAnimation();
        flipped_player_running_animation.loadAnimation("fliped_player_running_",4);
        flipped_player_jumping_animation = new ObjectAnimation();
        flipped_player_jumping_animation.loadAnimation("fliped_player_jumping_", 2);
        flipped_player_idle_animation = new ObjectAnimation();
        flipped_player_idle_animation.loadAnimation("fliped_player_idle_", 1);
        // player with a gun left animations
        flipped_player_running_gun_animation = new ObjectAnimation();
        flipped_player_running_gun_animation.loadAnimation("flipped_player_running_with_gun_",4);
        flipped_player_jumping_gun_animation = new ObjectAnimation();
        flipped_player_jumping_gun_animation.loadAnimation("flipped_player_running_with_gun_",1);
        flipped_player_idle_gun_animation = new ObjectAnimation();
        flipped_player_idle_gun_animation.loadAnimation("flipped_player_idle_gun_",1);



        outputTexture = playerTexture;
    }



    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder) {


        //Determine witch (playerState) state the player will be.
        GetPlayerState(delta);

        // keyboard input N Player movement
        PlayerInputHandling();

        // Detects if the player touches A MapObject and changes speeds
        collisionDetection(Ground,WorldBorder);

        // Changes the player X AND Y

        if(!GameScreen.isPaused){
            updatePlayerPos();
        }



        // checks which animation should play according to the Player's state
        switch (state) {
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
    public void GetPlayerState(float delta) {

        // checks if the player is moving up or down
        if (Yspeed > 0) {
            state = playerState.Jumping;
        }

        // checks if the player is moving left or right
        if (Xspeed != 0) {
            if (Xspeed != 0 && Yspeed == 0) {
                state = playerState.Running;
            }


        }
        else if (Xspeed == 0 && Yspeed == 0) {
            state = playerState.Idle;
        }

    }

    // keyboard input N Player movement
    public void PlayerInputHandling() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.N) && !GameScreen.isPaused){

            gunshot.play(0.1f);
            ShootBullets();
        }

        //Horizontal Player input
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && (Gdx.input.isKeyPressed(Input.Keys.LEFT)) || !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !GameScreen.isPaused) {
            Xspeed *= 0.8;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !GameScreen.isPaused) {
            Xspeed--;
            isFacingLeft = true;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !GameScreen.isPaused) {
            Xspeed++;
            isFacingLeft = false;
        }


        //Smooths Player Movement
        if (Xspeed > 0 && Xspeed < 0.80) Xspeed = 0;
        if (Xspeed < 0 && Xspeed > -0.80) Xspeed = 0;
        if (Xspeed > 12) Xspeed = 12;
        if (Xspeed < -12) Xspeed = -12;


        //vertical input
        if ((Gdx.input.isKeyPressed(Input.Keys.UP)) && !GameScreen.isPaused) {
            hitBox.y++;
            if (IsPlayerOnGround || Yspeed == -Yspeed) {
                Yspeed += 14;

            }
            hitBox.y--;
        }
        Yspeed -= 0.6;

    }

    // Detects if the player touches A MapObject
    public void collisionDetection(Array<MapObject> Ground,Array<MapObject> WorldBorder ) {

        //bounds Collision
        hitBox.x += Xspeed;
        for (MapObject borders: WorldBorder) {
            if (hitBox.overlaps(borders.hitBox)) {
                Xspeed -= Xspeed;
                Collision = true;
            }
            else {
                Collision = false;
            }

        }

        //Ground Collision
        hitBox.y += Yspeed;
        for (MapObject grounds : Ground) {
            if (hitBox.overlaps(grounds.hitBox)) {
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
    public void updatePlayerPos(){
        //Updates X AND Y Position of the player
        x += Xspeed;
        y += Yspeed;


        hitBox.x = x;
        hitBox.y = y;
    }

    public void ShootBullets() {
        Bullet bullet = new Bullet(x, y - 35);
        bullets.add(bullet);
    }

    public static Array<Bullet> getBullets(){
        return bullets;
    }

    public void dispose(){
        gunshot.dispose();
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
