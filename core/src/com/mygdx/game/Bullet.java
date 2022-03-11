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
    int width = 80 , height = 50;
    float bulletX, bulletY;
    Rectangle hitBox;
    Texture bulletTex;



    public Bullet(float x, float y, boolean IsPlayerFacingLeft) {

        this.bulletX = x;
        this.bulletY = y;

        // changes the direction and sprite width
        if(IsPlayerFacingLeft){

            DirectionSpeed = -BULLET_MOVEMENT_SPEED;
            width = width * -1;
        }
        else{

            DirectionSpeed = BULLET_MOVEMENT_SPEED;

        }



        hitBox = new Rectangle(bulletX, bulletY, width, height);

        bulletTex = new Texture("bullet.png");


    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder) {


        Xspeed += DirectionSpeed;
        if (Xspeed > BULLET_MOVEMENT_SPEED) Xspeed = BULLET_MOVEMENT_SPEED;
        if (Xspeed < -BULLET_MOVEMENT_SPEED) Xspeed = -BULLET_MOVEMENT_SPEED;


        // updates bullets position;
        bulletX += Xspeed;
        hitBox.x = bulletX;
        hitBox.y = bulletY;

        // removes the bullet if it overlaps WorldBorder
        BulletCollisionHandling(WorldBorder);


        return bulletTex;
    }


    public void BulletCollisionHandling(Array<MapObject> WorldBorder){

        for (MapObject Borders : WorldBorder) {
            Array<Bullet> BluePlayerbullets = GameScreen.Players.get(0).getBullets();
            for(Iterator<Bullet> BlueIter = BluePlayerbullets.iterator(); BlueIter.hasNext();){
                Bullet TempBlueBullets = BlueIter.next();
                if(TempBlueBullets.hitBox.overlaps(Borders.hitBox)){
                    BlueIter.remove();
                }
            }
        }


        for (MapObject Borders : WorldBorder) {
            Array<Bullet> YellowPlayerbullets = GameScreen.Players.get(1).getBullets();
            for(Iterator<Bullet> YellowIter = YellowPlayerbullets.iterator(); YellowIter.hasNext();){
                Bullet TempYellowBullets = YellowIter.next();
                if(TempYellowBullets.hitBox.overlaps(Borders.hitBox)){
                    YellowIter.remove();
                }
            }
        }


    }








}
