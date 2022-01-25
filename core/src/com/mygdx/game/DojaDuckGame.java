package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class DojaDuckGame extends Game {
	GameScreen gameScreen;
	public SpriteBatch batch;
	public BitmapFont font;
	private Music BackgroundMusic;

	@Override
	public void create () {
		this.setScreen(new GameScreen(this));
		setScreen(gameScreen);
		batch = new SpriteBatch();
		font = new BitmapFont();



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
		batch.dispose();
		font.dispose();
		gameScreen.dispose();
		BackgroundMusic.dispose();
	}


}
