package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    contamination game;

    //SFX
    Music ContaminationMusic;

    // graphics
    Texture background;

    Texture playButton;
    Texture playButtonPressed;
    Texture exitButton;
    Texture exitButtonPressed;
    Texture creditsButton;
    Texture creditsButtonPressed;

    public MainMenuScreen(contamination game){
        this.game = game;

        ContaminationMusic = Gdx.audio.newMusic(Gdx.files.internal("MenuMusic.mp3"));

        background = new Texture("MenuBackground.png");

        playButton = new Texture("playB.png");
        playButtonPressed = new Texture("playBP.png");
        exitButton = new Texture("exitB.png");
        exitButtonPressed = new Texture("exitBP.png");
        creditsButton = new Texture("creditsB.png");
        creditsButtonPressed = new Texture("credisBP.png");
        ContaminationMusic.setLooping(true);
        ContaminationMusic.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);


        game.batch.begin();
        game.batch.draw(background,0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);


        if(Gdx.input.getX() < 910 + 100-50 && Gdx.input.getX() > 910 -50 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 150 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 150){
            game.batch.draw(playButtonPressed,850+15,150,100,100);
            if(Gdx.input.isTouched()){
                ContaminationMusic.stop();
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        } else{
            game.batch.draw(playButton,850+15,150,100,100);
        }

        if(Gdx.input.getX() < 765 + 100-50 && Gdx.input.getX() > 800 - 80 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 150 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 150){
            game.batch.draw(exitButtonPressed,700+15,150,100,100);
            if(Gdx.input.isTouched()){
                dispose();
                Gdx.app.exit();
            }

        } else{
            game.batch.draw(exitButton,700+15,150,100,100);
        }

        if(Gdx.input.getX() < 1060 + 100-50 && Gdx.input.getX() > 1099 - 80 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 150 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 150){
            game.batch.draw(creditsButtonPressed,1000+15,150,100,100);
        } else{
            game.batch.draw(creditsButton,1000+15,150,100,100);
        }


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
        ContaminationMusic.dispose();
        playButton.dispose();
        playButtonPressed.dispose();
        exitButton.dispose();
        exitButtonPressed.dispose();
        creditsButton.dispose();
        creditsButtonPressed.dispose();

    }
}
