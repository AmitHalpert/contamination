package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class AmmoDrop {

    public final int DROP_MOVEMENT_SPEED = -200;


    double Yspeed;
    int width = 120 , height = 120;
    float dropX, dropY;
    boolean freeze;
    float DropDeleteTimer;
    boolean DeleteDrop;
    boolean IsExplosion;
    Rectangle DropHitBox;
    Rectangle ExplosiveHitBox;

    // graphics
    ObjectAnimation ExplosionAnimation;
    Texture AmmoDropOntheGroundTexture;
    Texture ParaAmmoDropTexture;
    Texture outTexture;

    public AmmoDrop(float x, float y){
        this.dropX = x;
        this.dropY = y;


        freeze = false;
        IsExplosion = false;
        DeleteDrop = false;
        DropDeleteTimer = 0;

        DropHitBox = new Rectangle(dropX, dropY, width, height);
        ExplosiveHitBox = new Rectangle(dropX,dropY,400,400);


        outTexture = new Texture("para_ammo_barrel.png");
        AmmoDropOntheGroundTexture = new Texture("ammo_barrel.png");
        ParaAmmoDropTexture = new Texture("para_ammo_barrel.png");
    }

    public Texture update(float delta){

        // freezes Drop. see in GameScreen AmmoDropCollision
        if(freeze){
            if(IsExplosion){
                DropDeleteTimer += delta;
                if(DropDeleteTimer >= 1f) {
                    DeleteDrop = true;
                }

            }
            else {

                outTexture = AmmoDropOntheGroundTexture;
                DropDeleteTimer += delta;
                if (DropDeleteTimer >= 17f) {
                    DeleteDrop = true;
                }
            }
        }

        else {

            Yspeed = DROP_MOVEMENT_SPEED;
            // updates bullets position;
            dropY += Yspeed * delta;
            DropHitBox.x = dropX;
            DropHitBox.y = dropY;
            ExplosiveHitBox.x = dropX;
            ExplosiveHitBox.y = dropY;
        }


        return outTexture;
    }


}


