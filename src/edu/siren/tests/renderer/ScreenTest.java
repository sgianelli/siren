package edu.siren.tests.renderer;

import org.lwjgl.LWJGLException;

import edu.siren.renderer.Screen;

public class ScreenTest {
    Screen screen;

    private void setDisplayModeTest() throws LWJGLException {
        screen = new Screen("Screen Test", 640, 480);
        while (screen.isOpened()) {
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException,
            InterruptedException {
        new ScreenTest().setDisplayModeTest();
    }
}