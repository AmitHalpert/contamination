package com.amithalpert.contamination.Entities.Objects;

import com.amithalpert.contamination.Screens.GameScreen;
import com.amithalpert.contamination.Tools.BodyHelper;
import com.amithalpert.contamination.Tools.GameEntity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Bullet{

    float x, y, velX, velY, speed;
    float width, height;
    Body body;



    public Bullet(int bulletX, int bulletY, boolean IsPlayerFacingLeft){
        body = BodyHelper.createBody(bulletX, bulletY, 10, 5, 0 ,false, GameScreen.getWorld());
        body.setUserData("bullet");
        x = body.getPosition().x;
        y = body.getPosition().y;
        this.velX = 0;
        this.velY = 0;
        this.speed = 4;

    }


    public void update() {
        x = body.getPosition().x * 16f;
        y = body.getPosition().y * 16f;

        velX = 1;

        body.setLinearVelocity(velX * speed, 0);
    }

    public Texture render(float delta) {
        return null;
    }

}
