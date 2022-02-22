package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    contamination game;

    Texture playButton;
    Texture playButtonPressed;
    Texture exitButtonActive;
    Texture exitButton;
    Texture creditsB;

    public MainMenuScreen(contamination game){
        this.game = game;
        playButton = new Texture("playB.png");
        exitButton = new Texture("exitB.png");
        creditsB = new Texture("creditsB.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            dispose();
            Gdx.app.exit();
        }

        game.batch.begin();


        game.batch.draw(playButton,850,150,100,100);
        game.batch.draw(exitButton,700,150,100,100);
        game.batch.draw(creditsB,1000,150,100,100);

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

    }
}
