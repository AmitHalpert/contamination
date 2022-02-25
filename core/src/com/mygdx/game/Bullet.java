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

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    float xf, yf;
    int width, height;
    boolean isVisible;
    Rectangle hitBox;
    Texture outputTexture;
    ObjectAnimation bullet_animation;



    public Bullet(float x, float y) {

        this.xf = x;
        this.yf = y;

        BulletDirection();


        hitBox = new Rectangle(xf, yf, 0.001f, 0.001f);
        bullet_animation = new ObjectAnimation();
        bullet_animation.loadAnimation("bullet_", 4);

    }

    public Texture update(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder) {


        Xspeed += DirectionSpeed;
        if (Xspeed > 0 && Xspeed < 10) Xspeed = 0;
        if (Xspeed < 0 && Xspeed > -10) Xspeed = 0;
        if (Xspeed > 40) Xspeed = 40;
        if (Xspeed < -40) Xspeed = -40;


        // updates bullets position;
        xf += Xspeed;
        hitBox.x = xf;
        hitBox.y = yf;


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
            DirectionSpeed = -40;
        }
        else{
            DirectionSpeed = 40;
        }
    }

}
