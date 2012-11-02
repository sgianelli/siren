package edu.siren.renderer.tests;

import java.io.IOException;
import java.util.Random;

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
        screen = new Screen("Texture Test", 640, 480);
    }

    private void setTileTest() throws LWJGLException, IOException {
        initializeScreen();

        screen.sync = 60;

        Random random = new Random();
        Layer layer = new Layer();
        layer.addTile(new Tile("res/tests/img/grass.png", 0, 0, 10000, 10000));

        for (int i = 0; i < 10000; i++) {
            Tile tile = new Tile("res/tests/img/weeds.png",
                    random.nextInt(10000), random.nextInt(10000) + 10.0f,
                    13.0f, 7.0f, 1, 1);
            layer.addTile(tile);
        }

        for (int i = 0; i < 500; i++) {
            Tile tile = new Tile("res/tests/img/tree.png", random.nextInt(9000)
                    - random.nextInt(50), random.nextInt(9000)
                    + random.nextInt(50), 72.0f, 88.0f, 1, 1);
            layer.addTile(tile);
        }

        for (int i = 0; i < 500; i++) {
            Tile tile = new Tile("res/tests/img/characters/justin.png",
                    random.nextInt(9000) - random.nextInt(50),
                    random.nextInt(9000) + random.nextInt(50), 16.0f, 25.0f, 1,
                    1);
            layer.addTile(tile);
        }

        Camera camera = new Camera(640.0f / 480.0f);
        Shader shader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        camera.bindToShader(shader);
        Keyboard.create();

        while (screen.isOpened()) {
            float x = Mouse.getX();
            float y = Mouse.getY();

            if (x < 50.0f && x > 0.0f) {
                x = -50f;
            } else if (x > 590.0f && x < 640.0f) {
                x = 50f;
            } else {
                x = 0;
            }

            if (y < 50.0f && y > 0.0f) {
                y = -50f;
            } else if (y > 430.0f && y < 480.0f) {
                y = 50f;
            } else {
                y = 0;
            }

            screen.clear();
            shader.use();
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                camera.zoomOut();
            }

            if (camera.move(x, y))
                ;
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