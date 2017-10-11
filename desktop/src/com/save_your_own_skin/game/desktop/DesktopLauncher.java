package com.save_your_own_skin.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.save_your_own_skin.game.World;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = World.WORLD_HEIGHT;
		config.width = World.WORLD_WIDTH;
		new LwjglApplication(new World(), config);


	}
}
