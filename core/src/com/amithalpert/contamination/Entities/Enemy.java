package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Entities.Objects.Bullet;
import com.amithalpert.contamination.Tools.MapObject;
import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Enemy {

    public enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }


    public Player.playerState state;

    // Initializing players' final Variables
    public final int PLAYER_WIDTH = 170;
    public final int PLAYER_HEIGHT = 170;

    public final float ANIMATIONS_TIME = 0.5f;
    public final float SHOOT_WAIT_TIME = 0.01f;
    public final int MOVEMENT_SPEED = 320;
    public final int JUMP_FORCE = 1050;
    public final float GRAVITATIONAL_FORCE = 15f;

    // player characteristics
    public float EnemyX;
    public float EnemyY;
    public int PlayerHealth;
    public int width;
    public int height;
    double velX, velY;
    boolean isFacingLeft;

    public boolean IsPlayerFrozen;
    public Rectangle PlayerHitBox;
    public Rectangle PlayerBounds;

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

    // SFX
    Music PlayerDeathSound;
    Sound gunshot;

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



    public Enemy(float x, float y){
        this.EnemyX = x;
        this.EnemyY = y;


        width = PLAYER_WIDTH;
        height = PLAYER_HEIGHT;
        PlayerBounds = new Rectangle(x, y, width, height);

        isFacingLeft = false;
        isPlayerHoldingGun = false;
        IsPlayerFrozen = false;
        state = Player.playerState.Idle;
        idle_animation_time = 0;
        dead_animation_time = 0;
        dead_elapsedTime = 0;
        PlayerDeathSound = Gdx.audio.newMusic(Gdx.files.internal("death-sound.mp3"));
        PlayerDeathSound.setVolume(0.3f);


        // Initializing gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun-shot-fx.wav"));


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



        // it is important to set the outputTexture to not be Null
        outputTexture = playerTexture;
    }




    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder, Array<MapObject> RadioActivePool) {


        if(state != Player.playerState.dead) {
            // the player's Hitbox for bullet collision
            PlayerHitBox = new Rectangle(EnemyX + 66, EnemyY, width, height - 115);
        }

        updatePlayerPosition();

        velX = 0;

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
                    // teleports "removes" the PlayerHitBox if player is dead
                    PlayerHitBox.x = 3000;

                    outputTexture = player_not_exiting;
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


    public void GetEnemyState() {


    }


    public void updatePlayerPosition(){

        //Updates X AND Y Position of the player
        EnemyX += velX;
        EnemyY += velY;


        PlayerBounds.x = EnemyX;
        PlayerBounds.y = EnemyY;

    }


}
