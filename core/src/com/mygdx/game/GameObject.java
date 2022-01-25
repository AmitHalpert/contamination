package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject extends Rectangle {

    private float dx, dy;
    boolean isFacingLeft;

    public GameObject(float width, float height){
        super.width = width;
        super.height = height;
        dx = 0;
        dy = 0;
        isFacingLeft = false;
    }
    public void moveTo(float x, float y) {
        super.x = x;
        super.y = y;
    }

    public void moveBy(float dx, float dy) {
        super.x += dx;
        super.y += dy;
        this.dx = dx;
        this.dy = dy;
    }

    public void moveXBy(float dx) {
        super.x += dx;
        this.dx = dx;
    }

    public void moveYBy(float dy) {
        super.y += dy;
        this.dy = dy;
    }

    public float getDX() {
        return dx;
    }

    public float getDY() {
        return dy;
    }

    // flips the object
    public void flip(){
        super.width = (super.width * -1);
        super.x = (super.x + super.width * -1);
    }

}
