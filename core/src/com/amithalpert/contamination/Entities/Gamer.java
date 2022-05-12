package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Tools.GameEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Gamer extends GameEntity {

    private int jumpCounter;

    public Gamer(float width, float height, Body body){
        super(width, height, body);
        this.speed = 4f;
        this.jumpCounter = 0;

    }

    @Override
    public void update() {
        x = body.getPosition().x * 16f;
        y = body.getPosition().y * 16f;


        handleUserInput();

    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private void handleUserInput(){
        velX = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            velX = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            velX = -1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && jumpCounter < 1){
            float force = body.getMass() * 18;
            body.setLinearVelocity(body.getLinearVelocity().x,0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(),true);
            jumpCounter ++;
        }

        // reset jumpcounter;
        if(body.getLinearVelocity().y == 0){
            jumpCounter = 0;
        }



        body.setLinearVelocity(velX * speed,body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);

    }


}
