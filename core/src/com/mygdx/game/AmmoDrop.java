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
    Texture DropNotExisting;

    public AmmoDrop(float x, float y){
        this.dropX = x;
        this.dropY = y;
        freeze = false;


        hitBox = new Rectangle(dropX, dropY, width, height);

        DropNotExisting = new Texture("player_dead_5.png");
        outTexture = new Texture("para_ammo_barrel.png");
        AmmoDropOntheGroundTexture = new Texture("ammo_barrel.png");
        ParaAmmoDropTexture = new Texture("para_ammo_barrel.png");
    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder, Array<MapObject> RadioActivePool){



        collisionHandling(Ground, RadioActivePool);

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

    public void collisionHandling(Array<MapObject> Ground,Array<MapObject> RadioActivePools){
        // collisionHandling

        // freeze if on ground
        for(int i = 0; i < Ground.size; i++) {
            if(Ground.get(i).hitBox.overlaps(hitBox)){
                freeze = true;
            }
        }

        for(int i = 0; i < RadioActivePools.size; i++) {
            if(RadioActivePools.get(i).hitBox.overlaps(hitBox)){
                outTexture = DropNotExisting;
            }
        }



    }


}


