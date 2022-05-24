package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Entities.Objects.BulletOld;
import com.amithalpert.contamination.Tools.GameEntity;
import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Player extends GameEntity {


    // the playerState is used to represent the current state of the player (check GetPlayerState function).
    // for example if playerState is set to Running the player animation will change to running (check the animation switch)
    private enum playerState {
        Running,
        Jumping,
        Idle,
        dead
    }


    public boolean onGround;
    private Array<BulletOld> bullets;
    private boolean isFacingLeft;
    private int jumpCounter;


    // player with gun animations
    ObjectAnimation player_running_gun_animation;
    ObjectAnimation player_jumping_gun_animation;
    ObjectAnimation player_idle_gun_animation;

    // left player with gun animations
    ObjectAnimation flipped_player_running_gun_animation;
    ObjectAnimation flipped_player_idle_gun_animation;
    ObjectAnimation flipped_player_jumping_gun_animation;

    Texture outputTexture;

    public playerState state;


    public Player(float width, float height, Body body){
        super(width, height, body);
        body.setUserData("player");
        this.speed = 4f;
        this.jumpCounter = 0;
        this.isFacingLeft = false;
        this.onGround = false;
        this.bullets = new Array<>();
        this.state = playerState.Idle;



        ////////////////////////
        // types of the player animations and textures
        ////////////////////////
        
        // player with a gun right animations
        player_running_gun_animation = new ObjectAnimation();
        player_running_gun_animation.loadAnimation("player_running_with_gun_", 4);
        player_jumping_gun_animation = new ObjectAnimation();
        player_jumping_gun_animation.loadAnimation("player_running_with_gun_", 1);
        player_idle_gun_animation = new ObjectAnimation();
        player_idle_gun_animation.loadAnimation("player_idle_gun_", 1);
        
        // player with a gun left animations
        flipped_player_running_gun_animation = new ObjectAnimation();
        flipped_player_running_gun_animation.loadAnimation("flipped_player_running_with_gun_", 4);
        flipped_player_jumping_gun_animation = new ObjectAnimation();
        flipped_player_jumping_gun_animation.loadAnimation("flipped_player_running_with_gun_", 1);
        flipped_player_idle_gun_animation = new ObjectAnimation();
        flipped_player_idle_gun_animation.loadAnimation("flipped_player_idle_gun_", 1);
        
        outputTexture = new Texture("player_idle_1.png");
    }

    @Override
    public void update() {
        x = body.getPosition().x * 16f;
        y = body.getPosition().y * 16f;


        handleUserInput();
    }



    @Override
    public Texture render(float delta) {

        getPlayerState();


        switch (state){
            case Running:
                if (isFacingLeft) {
                    outputTexture = flipped_player_running_gun_animation.getFrame(0.5f * delta);
                }
                else {
                    outputTexture = player_running_gun_animation.getFrame(0.5f * delta);
                }
                break;

            case Jumping:
                if(isFacingLeft){
                    outputTexture = flipped_player_jumping_gun_animation.getFrame(delta);
                }
                else {
                    outputTexture = player_jumping_gun_animation.getFrame(delta);
                }
                break;

            case Idle:
                if(isFacingLeft){
                    outputTexture = flipped_player_idle_gun_animation.getFrame(delta);
                }
                else{
                    outputTexture = player_idle_gun_animation.getFrame(delta);
                }
                break;
        }





        return outputTexture;
    }

    private void handleUserInput(){
        velX = 0;

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            ShootBullets();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
            velX = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            velX = -1;
        }

        System.out.printf("\n " + onGround);
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && onGround){
            float force = body.getMass() * 18;
            body.setLinearVelocity(body.getLinearVelocity().x,0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(),true);
            jumpCounter ++;
        }

        // reset jump-counter;
        if(body.getLinearVelocity().y == 0){
            jumpCounter = 0;
        }


        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y);
    }


    private void ShootBullets() {
        // creates a new bullet and add it to the array
        BulletOld bullet;

        // check isFacingLeft and adjust where the bullet coming from.
        if(isFacingLeft){
            bullet = new BulletOld((body.getPosition().x  * 16) - (20 / 2f), body.getPosition().y * 16 - (15  / 2f), true);
        }else{
            bullet = new BulletOld((body.getPosition().x * 16) - (20 / 2f), body.getPosition().y * 16 - (15 / 2f), false);
        }

        bullets.add(bullet);
    }



    private void getPlayerState(){

        // check if the player is jumping
        if(body.getLinearVelocity().y != 0){
            state = playerState.Jumping;
        }

        else if(body.getLinearVelocity().y == 0){
            state = playerState.Idle;
        }

        // check if the player is moving left or right
        if(velX != 0){
            if(velY == 0 && state != playerState.Jumping) {
                state = playerState.Running;
            }
            isFacingLeft = velX < 0;
        }

        else if(state == playerState.Running){
            state = playerState.Idle;
        }

    }


    public Array<BulletOld> getBullets(){
        return bullets;
    }



}
