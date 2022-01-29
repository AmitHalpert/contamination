package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.lang.*;



public class Player{

    // the player states
    public enum playerState{
        Running,
        Falling,
        Jumping,
        Idle,
        HoldingGun
    }

    int x, y;
    int width, height;
    double Xspeed, Yspeed;
    Rectangle hitBox;

    public float fallTime;
    float idle_animation_time;
    Boolean isAffectedByGravity;
    Boolean isFacingLeft;
    Boolean canJump;
    Texture outputTexture;
    Texture playerTexture;
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    playerState state;

    public Player(int x, int y) {

        this.x = x;
        this.y = y;

        width = 200;
        height = 200;
        hitBox = new Rectangle(x, y, width, height);


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
        outputTexture = playerTexture;

    }

    public void PlayerInputHandling(Array<MapObject> Walls){
        //Horizontal Player input
        if ( (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && (Gdx.input.isKeyPressed(Input.Keys.LEFT)) || !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ){
            Xspeed *= 0.8;
        }
        else if( (Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ) Xspeed--;
        else if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT))) Xspeed++;

        //Smooths Player Input
        if(Xspeed > 0 && Xspeed < 0.75) Xspeed = 0;
        if(Xspeed < 0 && Xspeed > -0.75) Xspeed = 0;
        if(Xspeed > 7) Xspeed = 7;
        if(Xspeed < -7 ) Xspeed = -7;

        //vertical input
        if((Gdx.input.isKeyPressed(Input.Keys.UP))){
            hitBox.y--;
            for(MapObject i_walls : Walls){
                if(i_walls.hitBox.overlaps(hitBox)) Yspeed = 6;
            }
            hitBox.y ++;
        }
        Yspeed -= 0.5;

        //Horizontal Collision
        hitBox.x += Xspeed;
        for(MapObject i_wall : Walls){
            if(hitBox.overlaps(i_wall.hitBox)){
            hitBox.x -= Xspeed;
            while(!i_wall.hitBox.overlaps(hitBox)) hitBox.x += Math.signum(Xspeed);
            hitBox.x -= Math.signum(Xspeed);
            Xspeed = 0;
            x = (int) hitBox.x;
            }

        }

        //Vertical Collision
        hitBox.y += Yspeed;
        for(MapObject i_wall : Walls){
            if(hitBox.overlaps(i_wall.hitBox)){
                hitBox.y -= Yspeed;
                while(!i_wall.hitBox.overlaps(hitBox)) hitBox.y += Math.signum(Xspeed);
                hitBox.y -= Math.signum(Yspeed);
                Yspeed = 0;
                y = (int) hitBox.y;
            }

        }

        //Updates Position of the player

        x += Xspeed;
        y += Yspeed;

        hitBox.x = x;
        hitBox.y = y;
    }




    public Texture render(float delta, Array<MapObject> Walls) {

        PlayerInputHandling(Walls);


        // checks if the player is moving up or down
        if (Yspeed > 0) {
            fallTime += delta;
            state = playerState.Jumping;
        }

        else if (Yspeed < 0){
            fallTime += delta;
            state = playerState.Falling;
        }


        // checks if the player is moving left or right
        if (Xspeed != 0) {
            if (Xspeed != 0) {
                state = playerState.Running;
            }

            if (Xspeed < 0) {
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
        /*
        if ((isFacingLeft && width > 0) || (!isFacingLeft && width < 0)) {
            flip();

        }
        */




        return outputTexture;
    }


    /*
    // flips the Player
    public void flip(){
        width = (width * -1);
        x = (x + width * -1);
    }

     */



    public void dispose(){
        player_running_animation.dispose();
        player_jumping_animation.dispose();
    }

}
