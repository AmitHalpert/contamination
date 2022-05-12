package com.amithalpert.contamination.Tools;

import com.badlogic.gdx.physics.box2d.*;

public class BodyHelper {

    public static Body createBody(float x, float y, float width, float height, Boolean isStatic, World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 16f, y / 16f);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / 16, height / 2 / 16);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }
}
