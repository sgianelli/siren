package edu.siren.tests.renderer;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL13;

import edu.siren.renderer.IndexVertexBuffer;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;
import edu.siren.renderer.TexturePNG;
import edu.siren.renderer.Util;

public class TextureTest {
    Screen screen;

    private void initializeScreen() throws LWJGLException {
        screen = new Screen("Texture Test", 640, 480);
    }

    private void setTextureTest() throws LWJGLException, IOException {
        initializeScreen();

        IndexVertexBuffer vertices = Util.Shape.createQuad(new TexturePNG(
                "res/tests/img/grid.png", GL13.GL_TEXTURE0));

        Shader shader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        while (screen.isOpened()) {
            screen.clear();
            shader.use();
            vertices.draw();
            shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TextureTest().setTextureTest();
    }
}