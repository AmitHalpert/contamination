package com.amithalpert.contamination.Tools;
import com.amithalpert.contamination.Entities.Player;
import com.amithalpert.contamination.Screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {



    @Override
    public void beginContact(Contact contact) {

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();


        if(fA.getBody() != null && fB.getBody() != null){
            if(fA.getBody().getUserData().equals("player") && fB.getBody().getUserData().equals("immovable")){
                GameScreen.getPlayer().onGround = true;
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();



        if(fA.getBody() != null && fB.getBody() != null){
            if(fA.getBody().getUserData().equals("player") && fB.getBody().getUserData().equals("immovable")){
                GameScreen.getPlayer().onGround = false;
            }
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
