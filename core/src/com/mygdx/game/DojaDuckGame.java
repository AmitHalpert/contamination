package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class DojaDuckGame extends Game {
	GameScreen gameScreen;

	private Music BackgroundMusic;

	@Override
	public void create () {
		gameScreen = new GameScreen();
		setScreen(gameScreen);

		// load the drop sound effect and the background music
		BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("planet.mp3"));
		BackgroundMusic.setLooping(true);
		BackgroundMusic.play();
		BackgroundMusic.setVolume(0.03f);


	}

	@Override
	public  void resize(int width, int height){
		gameScreen.resize(width, height);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		gameScreen.dispose();
		BackgroundMusic.dispose();
	}


}
