package com.amithalpert.contamination.Entities.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.amithalpert.contamination.Tools.ObjectAnimation;


public class AmmoDrop {

    public final int DROP_MOVEMENT_SPEED = -200;


    double Yspeed;
    public int width = 120;
    public int height = 120;
    public float dropX;
    public float dropY;
    public boolean freeze;
    float DropDeleteTimer;
    float ExplosionDropDeleteTimer;
    public boolean DeleteDrop;
    public boolean IsExplosion;
    public Rectangle DropHitBox;
    public Rectangle ExplosiveHitBox;

    // graphics
    ObjectAnimation ExplosionAnimation;
    Texture AmmoDropOntheGroundTexture;
    Texture ParaAmmoDropTexture;
    Texture outTexture;

    Music ExplosionSound;




    public AmmoDrop(float x, float y){
        this.dropX = x;
        this.dropY = y;


        freeze = false;
        IsExplosion = false;
        DeleteDrop = false;
        ExplosionDropDeleteTimer= 0;


        DropHitBox = new Rectangle(dropX, dropY, width, height);

        ExplosionAnimation = new ObjectAnimation();
        ExplosionAnimation.loadAnimation("BarrelExplosion_",14);
        outTexture = new Texture("para_ammo_barrel.png");
        AmmoDropOntheGroundTexture = new Texture("ammo_barrel.png");
        ParaAmmoDropTexture = new Texture("para_ammo_barrel.png");



    }

    public Texture update(float delta){


        ExplosiveHitBox = new Rectangle(dropX - 100 ,dropY,350,350);
        // freezes Drop. See in GameScreen AmmoDropCollision
        if(freeze){
            if(IsExplosion){
                outTexture = ExplosionAnimation.getFrame(0.8f * delta);
                ExplosionDropDeleteTimer += delta;
                if(ExplosionDropDeleteTimer >= 0.6f) {
                    ExplosionDropDeleteTimer = 0;
                    ExplosionDropDeleteTimer += delta;
                    DeleteDrop = true;
                }
            }
            else {

                outTexture = AmmoDropOntheGroundTexture;
                DropDeleteTimer += delta;
                if (DropDeleteTimer >= 15f) {
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
            ExplosiveHitBox.x = dropX ;
            ExplosiveHitBox.y = dropY;
        }


        return outTexture;
    }


}


