package com.amithalpert.contamination.Screens;

import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.amithalpert.contamination.contamination;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class CreditsScreen implements Screen {

    contamination game;

    float elapsedTime;
    float deltaTime;

    //SFX
    Music CreditsMusic;
    boolean isMusicPlaying;

    // graphics
    ObjectAnimation CreditsText;
    Texture BlankFrame;


    public CreditsScreen(contamination game){
        this.game = game;

        CreditsMusic = Gdx.audio.newMusic(Gdx.files.internal("creditsmusic.mp3"));
        CreditsMusic.setVolume(0.05f);
        CreditsMusic.play();
        isMusicPlaying = true;

        CreditsText = new ObjectAnimation();
        CreditsText.loadAnimation("creditsText_",7);
        BlankFrame = new Texture("creditsText_1.png");


        elapsedTime = 0;
        deltaTime = 0;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();



        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && isMusicPlaying){
            CreditsMusic.setVolume(0);
            isMusicPlaying = false;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !isMusicPlaying){
            CreditsMusic.setVolume(0.05f);
            isMusicPlaying = true;
        }


        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }

        // timer to end the credits
        elapsedTime += deltaTime;
        if(elapsedTime >= 57f){
            CreditsMusic.stop();
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }

        game.batch.begin();
        game.batch.draw(CreditsText.getFrame(0.005f * Gdx.graphics.getDeltaTime()),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        CreditsMusic.dispose();
        CreditsText.dispose();
        BlankFrame.dispose();

    }
}
