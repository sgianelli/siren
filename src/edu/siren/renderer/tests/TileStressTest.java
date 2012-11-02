package edu.siren.renderer.tests;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.core.Layer;
import edu.siren.core.Tile;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class TileStressTest {
    Screen screen;

    private void initializeScreen() throws LWJGLException {
        screen = new Screen("Texture Test", 1280, 1024);
    }

    private void setTileTest() throws LWJGLException, IOException {
        initializeScreen();

        screen.sync = 60;

        Layer layer = new Layer();
        for (float i = -100.0f; i < 100f; i++) {
            for (float j = -10.0f; j < 10f; j++) {
                layer.addTile(new Tile("res/tests/img/weeds.png", i + 10.0f,
                        j + 10.0f, 10.0f, 10.0f));
            }
        }

        Camera camera = new Camera();
        Shader shader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        camera.bindToShader(shader);
        Keyboard.create();

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
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                System.out.println("Down Z");
                camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                System.out.println("Down X");
                camera.zoomOut();
            }
            camera.move(x, y);
            layer.draw();
            shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TileStressTest().setTileTest();
    }
}