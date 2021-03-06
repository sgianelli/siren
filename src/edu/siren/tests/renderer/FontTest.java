package edu.siren.tests.renderer;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class FontTest {
    public static void main(String[] args) throws IOException, LWJGLException {
        Screen screen = new Screen("Screen Test", 640, 480);
        Font font = new Font("res/tests/fonts/proggy.png");
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert",
                "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        Keyboard.create();
        Mouse.create();
        String s = "";
        for (int i = 1; i <= 256; i++) {
            if (i % 16 == 0) {
                s += "\n";
            }
            s += (char)(i - 1);
        }
        while (screen.isOpened()) {
            int x = Mouse.getX();
            int y = Mouse.getY();
            screen.clear();
            shader.use();
            font.print(s, 1, x, y);
            shader.release();
            screen.update();
        }
        screen.cleanup();

    }
}
