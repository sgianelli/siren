package edu.siren.renderer.tests;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayTest {

	private void setDisplayModeTest() throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		Display.setDisplayMode(modes[0]);
		Display.setFullscreen(false);
		Display.create();
	}

	public static void main(String[] args) throws LWJGLException,
			InterruptedException {
		new DisplayTest().setDisplayModeTest();
		Thread.sleep(2000);
		System.exit(0);
	}
}