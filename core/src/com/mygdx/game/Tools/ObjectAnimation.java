package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

/**
 * The purpose of ObjectAnimation is to replace TextureAtlas(.pack),
 * by playing animations using only .PNGs,
 */

// animation for a game object
public class ObjectAnimation {
    final float targetDelta = 0.05f; // is the target time for elapsedTime
    public int currentFrame; // current frame in the animation array
    float elapsedTime; // elapsed time since last frame change
    public Array<Texture> frames; // stores the frames of the animation

    public ObjectAnimation(){
        frames = new Array<>();
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

    // returns the frame in the array by the wanted index
    public Texture getIndexFrame(int WantedFrame){
        frames.get(WantedFrame);

        return frames.get(WantedFrame);
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
