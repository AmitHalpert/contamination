package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet{

    public static final int BULLET_MOVEMENT_SPEED = 50;

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

            // changes the direction and sprite width
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
        bulletX += Xspeed;
        hitBox.x = bulletX;
        hitBox.y = bulletY;

        // removes the bullet if it overlaps WorldBorder
        BulletCollisionHandling(WorldBorder);



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


    public void BulletCollisionHandling(Array<MapObject> WorldBorder){


        for (MapObject Borders : WorldBorder) {
            Array<Bullet> BluePlayerbullets = GameScreen.Players.get(0).getBullets();
            for(Iterator<Bullet> BlueIter = BluePlayerbullets.iterator(); BlueIter.hasNext();){
                Bullet TempBlueBullets = BlueIter.next();
                if(TempBlueBullets.hitBox.overlaps(Borders.hitBox)){
                    outTexture = bulletNotExisting;
                    BlueIter.remove();
                }
            }
        }


        for (MapObject Borders : WorldBorder) {
            Array<Bullet> YellowPlayerbullets = GameScreen.Players.get(1).getBullets();
            for(Iterator<Bullet> YellowIter = YellowPlayerbullets.iterator(); YellowIter.hasNext();){
                Bullet TempYellowBullets = YellowIter.next();
                if(TempYellowBullets.hitBox.overlaps(Borders.hitBox)){
                    outTexture = bulletNotExisting;
                    YellowIter.remove();
                }
            }
        }

    }








}
