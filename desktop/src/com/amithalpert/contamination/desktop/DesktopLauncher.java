package com.amithalpert.contamination.desktop;


import com.amithalpert.contamination.contamination;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("contamination");

		config.setWindowedMode(1920,1080);

		/*
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

		 */
		new Lwjgl3Application(new contamination(), config);
	}
}
