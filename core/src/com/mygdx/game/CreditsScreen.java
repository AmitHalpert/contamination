package com.mygdx.game;

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

    // graphics
    ObjectAnimation CreditsText;
    Texture output;
    Texture BlankFrame;


    public CreditsScreen(contamination game){
        this.game = game;

        CreditsMusic = Gdx.audio.newMusic(Gdx.files.internal("creditsmusic.mp3"));
        CreditsMusic.setVolume(0.4f);
        CreditsMusic.play();

        CreditsText = new ObjectAnimation();
        CreditsText.loadAnimation("creditsText_",21);
        BlankFrame = new Texture("creditsText_1.png");


        output = BlankFrame;
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

        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            dispose();
            Gdx.app.exit();
        }

        game.batch.begin();

        elapsedTime += deltaTime;
        if(elapsedTime >= 169f){
            CreditsMusic.stop();
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }

        game.batch.draw(output,0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);

        output = CreditsText.getFrame(0.0001f);


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
        output.dispose();
        BlankFrame.dispose();

    }
}
