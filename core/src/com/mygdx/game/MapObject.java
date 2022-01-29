package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class MapObject {

    float x;
    float y;
    int width;
    int height;

    Rectangle hitBox;

    public MapObject(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        hitBox = new Rectangle(x, y, width, height);

    }
}
