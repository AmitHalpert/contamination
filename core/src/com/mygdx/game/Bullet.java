package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Bullet{

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    float x, y;
    int width, height;
    boolean isVisible;
    Rectangle hitBox;
    Texture outputTexture;
    ObjectAnimation bullet_animation;



    public Bullet(float x, float y) {

        this.x = x;
        this.y = y;
        width = 70;
        height = 70;

        BulletDirection();


        hitBox = new Rectangle(x, y, width, height);

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
        x += Xspeed;
        hitBox.x = x;
        hitBox.y = y;


        outputTexture = bullet_animation.getFrame(delta);

        for (MapObject Borders : WorldBorder) {
            Array bullets = Player.getBullets();
            for (int w = 0; w < bullets.size; w++) {
                Bullet b = (Bullet) bullets.get(w);
                if (((Bullet) bullets.get(w)).hitBox.overlaps(Borders.hitBox)) {
                    bullets.removeIndex(w);
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
