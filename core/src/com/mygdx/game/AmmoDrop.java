package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class AmmoDrop {

    public final int DROP_MOVEMENT_SPEED = -200;


    double Yspeed;
    int width = 120 , height = 120;
    float dropX, dropY;
    boolean freeze;
    Rectangle hitBox;
    Texture AmmoDropOntheGroundTexture;
    Texture ParaAmmoDropTexture;
    Texture outTexture;

    public AmmoDrop(float x, float y,boolean freeze){
        this.dropX = x;
        this.dropY = y;
        this.freeze = freeze;


        hitBox = new Rectangle(dropX, dropY, width, height);

        outTexture = new Texture("para_ammo_barrel.png");
        AmmoDropOntheGroundTexture = new Texture("ammo_barrel.png");
        ParaAmmoDropTexture = new Texture("para_ammo_barrel.png");
    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder){

        if(freeze){
            outTexture = AmmoDropOntheGroundTexture;
        }
        else {
            Yspeed = DROP_MOVEMENT_SPEED;
            // updates bullets position;
            dropY += Yspeed * delta;
            hitBox.x = dropX;
            hitBox.y = dropY;
        }



        return outTexture;
    }


}


