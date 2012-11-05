package edu.siren.renderer.tests;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.core.Layer;
import edu.siren.core.Tile;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Font;
import edu.siren.renderer.Gui;
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
        Layer layer = new Layer(BufferType.STATIC);
        layer.addTile(new Tile("res/tests/img/grass.png", 0, 0, 10000, 10000));
        Gui gui = new Gui();
        Shader shader = new Shader("res/tests/glsl/gui.vert",
                "res/tests/glsl/gui.frag");
        gui.bindToShader(shader);
        Font font = new Font("res/tests/fonts/nostalgia.png", 24);

        String phrases[] = { "Whoa!", "Hi!", "What are you up to?" };

        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt(9000) - random.nextInt(50);
            int y = random.nextInt(9000) - random.nextInt(50);
            Tile tile = new Tile("res/tests/img/characters/justin.png", x, y,
                    16.0f, 25.0f, 1, 1);
            layer.addIndexVertexBuffer(font.print(phrases[random.nextInt(3)],
                    3, x, y + 25, null, 0));
            layer.addTile(tile);
        }

        Camera camera = new Camera(640.0f / 480.0f);
        Shader basicshader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        camera.bindToShader(basicshader);
        Keyboard.create();

        while (screen.isOpened()) {
            float x = Mouse.getX();
            float y = Mouse.getY();

            if (x < 50.0f && x > 0.0f) {
                x = -15f;
            } else if (x > 590.0f && x < 640.0f) {
                x = 15f;
            } else {
                x = 0;
            }

            if (y < 50.0f && y > 0.0f) {
                y = -15f;
            } else if (y > 430.0f && y < 480.0f) {
                y = 15f;
            } else {
                y = 0;
            }

            screen.clear();
            basicshader.use();
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                camera.zoomOut();
            }

            layer.draw();
            camera.move(x, y);
            font.print("Text?", 1, 0, 0);

            basicshader.release();
            shader.use();
            int mx = Mouse.getX(), my = Mouse.getY();
            font.print("" + mx + "," + (my), 4, mx, my);
            font.print("Overlay?", 1, mx, my);
            shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TileStressTest().setTileTest();
    }
}