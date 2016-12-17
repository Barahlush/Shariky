package com.shariky.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shariky.game.GameScreen;
import com.shariky.game.Shariky;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shariky";
		config.width = 480;
		config.height = 800;

		new LwjglApplication(new Shariky(), config);
	}
}
