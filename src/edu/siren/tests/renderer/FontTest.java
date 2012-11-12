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
        Font font = new Font("res/tests/fonts/nostalgia.png", 24);
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert",
                "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        Keyboard.create();
        Mouse.create();
        while (screen.isOpened()) {
            int x = Mouse.getX();
            int y = Mouse.getY();
            screen.clear();
            shader.use();
            font.print(
                    "#include <stdio.h>\n#include <stdlib.h>\nvoid main()\n{\n\tint x = 0;\n}",
                    1, x, y);
            shader.release();
            screen.update();
        }
        screen.cleanup();

    }
}
