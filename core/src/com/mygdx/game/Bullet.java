package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SortedIntList;

import java.util.Iterator;
import java.util.LinkedList;

public class Bullet{

    public static final int BULLET_MOVEMENT_SPEED = 65;

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    float bulletX, bulletY;
    Rectangle hitBox;
    Texture outputTexture;
    ObjectAnimation bullet_animation;



    public Bullet(float x, float y) {

        this.bulletX = x;
        this.bulletY = y;

        BulletDirection();


        hitBox = new Rectangle(bulletX, bulletY, 0.5f, 0.8f);
        bullet_animation = new ObjectAnimation();
        bullet_animation.loadAnimation("bullet_", 4);

    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder) {


        Xspeed += DirectionSpeed;
        if (Xspeed > BULLET_MOVEMENT_SPEED) Xspeed = BULLET_MOVEMENT_SPEED;
        if (Xspeed < -BULLET_MOVEMENT_SPEED) Xspeed = -BULLET_MOVEMENT_SPEED;


        // updates bullets position;
        bulletX += Xspeed;
        hitBox.x = bulletX;
        hitBox.y = bulletY;

        outputTexture = bullet_animation.getFrame(delta);

        for (MapObject Borders : WorldBorder) {
            Array<Bullet> bullets = Player.getBullets();
            for(Iterator<Bullet> iter = bullets.iterator(); iter.hasNext();){
                Bullet b = iter.next();
                if(b.hitBox.overlaps(Borders.hitBox)){
                    iter.remove();
                }
            }
        }


        return outputTexture;
    }

    public void BulletDirection(){
        if(Player.isFacingLeft){
            DirectionSpeed = -BULLET_MOVEMENT_SPEED;
        }
        else{
            DirectionSpeed = BULLET_MOVEMENT_SPEED;
        }
    }

}
