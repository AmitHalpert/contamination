package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    // player characteristics
    float movementSpeed;


    // position & dimension
    float xPosition, yPosition;
    float width, height;

    //graphics
    TextureRegion playerTexture;

    public Player(float movementSpeed, float width, float height, float xCentre, float yCentre, TextureRegion playerTexture) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xCentre - width/2;
        this.yPosition = yCentre - height/2;
        this.width = width;
        this.height = height;
        this.playerTexture = playerTexture;
    }

    public void  draw(Batch batch){
        batch.draw(playerTexture,xPosition,yPosition,width,height);
    }
}
