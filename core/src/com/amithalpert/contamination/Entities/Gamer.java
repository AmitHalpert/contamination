package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Tools.GameEntity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Gamer extends GameEntity {

    public Gamer(float width, float height, Body body){
        super(width, height, body);
        this.speed = 4f;

    }

    @Override
    public void update() {
        x = body.getPosition().x + 16f;
        y = body.getPosition().y + 16f;

    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
