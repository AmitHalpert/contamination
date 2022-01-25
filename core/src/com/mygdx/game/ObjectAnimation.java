package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

// animation for a game object
public class ObjectAnimation {
    final float targetDelta = 0.05f; // is the target time for elapsedTime
    int currentFrame; // current frame in the animation array
    float elapsedTime; // elapsed time since last frame change
    Array<Texture> frames; // stores the frames of the animation

    public ObjectAnimation(){
        frames = new Array<Texture>();
        currentFrame = 0;
        elapsedTime = 0;
    }

    // returns the current frame
    public Texture getFrame(float delta) {
        elapsedTime += delta;

        // checks if the time since last frame change is greater than the target delta for changing frames
        if (elapsedTime >= targetDelta) {
            currentFrame++;
            elapsedTime = 0;
        }

        // makes sure the program won't call an element that's outside of the array bounds
        if (currentFrame >= frames.size - 1) {
            currentFrame = 0;
        }

        return frames.get(currentFrame);
    }

    // resets the animation
    public void resetAnimation(){
        currentFrame = 0;
        elapsedTime = 0;
    }

    // loads the frames of the animation
    public void loadAnimation(String fileName, int frameAmount){
        for (int i = 0; i < frameAmount; i++) {
            frames.add(new Texture(Gdx.files.internal(fileName + (i + 1) + ".png")));
        }
    }

    // disposes of the animation frames
    public void dispose() {
        for (Texture frame : frames){
            frame.dispose();
        }
    }
}
