package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;


public class Player extends GameObject {

    // the player states
    public enum playerState{
        Running,
        Jumping,
        Idle,
    }

    public float fallTime;
    float idle_animation_time;
    Boolean isAffectedByGravity;
    Boolean canJump;
    Texture outputTexture;
    Texture playerTexture;
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    playerState state;

    public Player(float x, float y) {
        super(48 * 3, 32 * 3);
        fallTime = 1f;
        isAffectedByGravity = false;
        canJump = false;
        state = playerState.Idle;
        idle_animation_time = 0;

        //set up the player animations
        player_running_animation = new ObjectAnimation();
        player_running_animation.loadAnimation("player_running_", 4);
        player_jumping_animation = new ObjectAnimation();
        player_jumping_animation.loadAnimation("player_jumping_", 2);
        player_idle_animation = new ObjectAnimation();
        player_idle_animation.loadAnimation("player_idle_", 1);
        playerTexture = new Texture(Gdx.files.internal("player_idle_1.png"));


    }

    public Texture render(float delta) {
        // checks if the player is moving up or down
        if (super.getDY() > 0) {
            fallTime += delta;
            state = playerState.Jumping;
        }


        else if (delta != 0) {
            state = playerState.Idle;
        }

        // checks if the player is moving left or right
        if (super.getDX() != 0) {
            if (super.getDY() == 0) {
                state = playerState.Running;
            }

            if (super.getDX() < 0) {
                isFacingLeft = true;
            }

            else {
                isFacingLeft = false;
            }
        }

        else if (delta != 0 && state == playerState.Running) {
            state = playerState.Idle;
        }

        // checks which animation should play according to the state enum
        switch (state) {
            case Running:
                outputTexture = player_running_animation.getFrame(delta);
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;


            case Jumping:
                if (player_jumping_animation.currentFrame >= player_jumping_animation.frames.size - 2){
                    outputTexture = player_jumping_animation.frames.get(player_jumping_animation.currentFrame);
                }

                else {
                    outputTexture = player_jumping_animation.getFrame(delta);
                }
                player_running_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Idle:
                if (idle_animation_time > 2f) {
                    outputTexture = player_idle_animation.getFrame(delta);

                    if (idle_animation_time > 3f) {
                        idle_animation_time = 0;
                    }
                }

                else outputTexture = playerTexture;

                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                idle_animation_time += delta;
                break;
        }

        // checks if the last movement has been to the left and mirrors the texture
        if ((super.isFacingLeft && super.width > 0) || (!super.isFacingLeft && super.width < 0)) {
            flip();
        }

        return outputTexture;
    }

    public void dispose(){
        player_running_animation.dispose();
        player_jumping_animation.dispose();
    }

}
