package edu.siren.renderer.tests;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL13;

import edu.siren.renderer.Screen;
import edu.siren.renderer.TexturePNG;
import edu.siren.renderer.Util;

public class TextureTest {
    Screen screen;

    private void initializeScreen() throws LWJGLException {
        screen = new Screen("Texture Test", 640, 480);
    }

    private void setTextureTest() throws LWJGLException, IOException {
        initializeScreen();

        Util.Shape.createQuad(new TexturePNG("tests/img/grid.png",
                GL13.GL_TEXTURE0));

        while (screen.isOpened()) {
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TextureTest().setTextureTest();
    }
}