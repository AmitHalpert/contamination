package com.amithalpert.contamination.Tools;

import com.badlogic.gdx.math.Rectangle;

public class MapBorder {

    float x;
    float y;
    int width;
    int height;

    public Rectangle hitBox;

    public MapBorder(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        hitBox = new Rectangle(x, y, width, height);

    }
}
