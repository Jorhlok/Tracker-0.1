package net.jorhlok.tracker0_1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.jorhlok.tracker0_1.tracker0_1;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 1000;
                config.height = 480;
                config.title = "JPSG160X Tracker 0.1";
		new LwjglApplication(new tracker0_1(  ), config);
//		new LwjglApplication(new tracker0_1( new JavaxAudioDevice() ), config);
	}
}
