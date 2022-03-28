package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet{

    public static final int BULLET_MOVEMENT_SPEED = 5900;

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    int width = 60 , height = 60;
    float bulletX, bulletY;
    Rectangle hitBox;
    Texture bulletTex;
    Texture bulletNotExisting;
    Texture outTexture;


    public Bullet(float x, float y, boolean IsPlayerFacingLeft) {

        this.bulletX = x;
        this.bulletY = y;
        hitBox = new Rectangle(bulletX, bulletY, width+43, height);

        // changes the direction and sprite width
        if((IsPlayerFacingLeft && this.width > 0) || (!IsPlayerFacingLeft && this.width < 0)){

            // changes the sprite width
            FlipBulletSprite(IsPlayerFacingLeft);

            // move left
            DirectionSpeed = -BULLET_MOVEMENT_SPEED;

        }
        else{
            // move right
            DirectionSpeed = BULLET_MOVEMENT_SPEED;
        }



        outTexture = new Texture("bullet.png");
        bulletNotExisting = new Texture("player_dead_5.png");
        bulletTex = new Texture("bullet.png");


    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder) {



        Xspeed = DirectionSpeed;

        // updates bullets position;
        bulletX += Xspeed * Gdx.graphics.getDeltaTime();
        hitBox.x = bulletX;
        hitBox.y = bulletY;

        Xspeed = 0;



        return outTexture;
    }

    public void FlipBulletSprite(boolean IsPlayerFacingLeft){
        width = width * -1;
        bulletX = bulletX + width+43 * -1;

            if (IsPlayerFacingLeft) {
            hitBox.width = (hitBox.width+43 * -1) / 1.3f;
            hitBox.x = hitBox.x + hitBox.width+43 * -1;
            }

    }

}
