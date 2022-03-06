package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet{

    public static final int BULLET_MOVEMENT_SPEED = 75;

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    float bulletX, bulletY;
    Rectangle hitBox;
    Texture outputTexture;
    ObjectAnimation bullet_animation;



    public Bullet(float x, float y, boolean IsBulletMovingLeft) {

        this.bulletX = x;
        this.bulletY = y;

        if(IsBulletMovingLeft){
            DirectionSpeed = -BULLET_MOVEMENT_SPEED;
        }
        else{
            DirectionSpeed = BULLET_MOVEMENT_SPEED;
        }


        hitBox = new Rectangle(bulletX, bulletY, 5,0.5f);
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

        BulletCollisionHandling(WorldBorder);

        return outputTexture;
    }


    public void BulletCollisionHandling(Array<MapObject> WorldBorder){

        for (MapObject Borders : WorldBorder) {
            Array<Bullet> BluePlayerbullets = BluePlayer.getBullets();
            for(Iterator<Bullet> BlueIter = BluePlayerbullets.iterator(); BlueIter.hasNext();){
                Bullet TempBlueBullets = BlueIter.next();
                if(TempBlueBullets.hitBox.overlaps(Borders.hitBox)){
                    BlueIter.remove();
                }
            }
        }


        for (MapObject Borders : WorldBorder) {
            Array<Bullet> YellowPlayerbullets = OrangePlayer.getYellowPlayerBullets();
            for(Iterator<Bullet> YellowIter = YellowPlayerbullets.iterator(); YellowIter.hasNext();){
                Bullet TempYellowBullets = YellowIter.next();
                if(TempYellowBullets.hitBox.overlaps(Borders.hitBox)){
                    YellowIter.remove();
                }
            }
        }



    }











}
