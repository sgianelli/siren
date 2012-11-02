package edu.siren.renderer.tests;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import edu.siren.core.Layer;
import edu.siren.core.Tile;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class TileTest {
    Screen screen;

    private void initializeScreen() throws LWJGLException {
        screen = new Screen("Texture Test", 640, 480);
    }

    private void setTileTest() throws LWJGLException, IOException {
        initializeScreen();

        screen.sync = 60;

        Layer layer = new Layer();
        layer.addTile(new Tile(0.0f, 0.0f, 1.0f, 1.0f), // Center
                new Tile(-2.5f, -2.5f, 1.0f, 1.0f)); // Left
        Camera camera = new Camera();
        Shader shader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        camera.bindToShader(shader);

        while (screen.isOpened()) {
            float x = Mouse.getX();
            float y = Mouse.getY();

            if (x < 50.0f && x > 0.0f) {
                x = -0.05f;
            } else if (x > 590.0f && x < 640.0f) {
                x = 0.05f;
            } else {
                x = 0;
            }

            if (y < 50.0f && y > 0.0f) {
                y = -0.05f;
            } else if (y > 430.0f && y < 480.0f) {
                y = 0.05f;
            } else {
                y = 0;
            }

            screen.clear();
            shader.use();
            camera.move(x, y);
            layer.draw();
            shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TileTest().setTileTest();
    }
}