package com.amithalpert.contamination.Entities.Objects;

import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Pool {

    public final int width = 284;
    public final int height = 190;
    public float PoolX;
    public float PoolY;
    public Rectangle PoolHitBox;

    ObjectAnimation PoolAnimation;
    Texture outTexture;

    public Pool(int x, int y){
        this.PoolX = x;
        this.PoolY = y;


        PoolHitBox = new Rectangle(PoolX, PoolY, width, height);

        PoolAnimation = new ObjectAnimation();
        PoolAnimation.loadAnimation("RadioActivePoolAnimation_",5);
        outTexture = new Texture("RadioActivePoolAnimation_1.png");
    }

    public Texture update(float delta){


        outTexture = PoolAnimation.getFrame(0.3f * delta);


        return outTexture;
    }

}
