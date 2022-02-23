package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    contamination game;

    static final int xCenter = GameScreen.WORLD_WIDTH / 2;
    static final int yCenter = GameScreen.WORLD_HEIGHT / 2;

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

        playButton = new Texture("start.png");
        playButtonPressed = new Texture("pstart.png");
        exitButton = new Texture("exit.png");
        exitButtonPressed = new Texture("pexit.png");
        creditsButton = new Texture("credits.png");
        creditsButtonPressed = new Texture("pcredits.png");
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


        if(Gdx.input.getX() < xCenter+100 && Gdx.input.getX() > xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 400 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 400){
            game.batch.draw(playButtonPressed,xCenter-100,400,200,100);
            if(Gdx.input.isTouched()){
                ContaminationMusic.stop();
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        } else{
            game.batch.draw(playButton,xCenter-100,400,200,100);
        }

        if(Gdx.input.getX() < xCenter+100 && Gdx.input.getX() > xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 250 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 250){
            game.batch.draw(exitButtonPressed,xCenter-100,250,200,100);
            if(Gdx.input.isTouched()){
                dispose();
                Gdx.app.exit();
            }

        } else{
            game.batch.draw(exitButton,xCenter-100,250,200,100);
        }

        if(Gdx.input.getX() < xCenter+100 && Gdx.input.getX() > xCenter-100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() < 100 + 100 && GameScreen.WORLD_HEIGHT - Gdx.input.getY() > 100){
            game.batch.draw(creditsButtonPressed,xCenter-100,100,200,100);
        } else{
            game.batch.draw(creditsButton,xCenter-100,100,200,100);
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
