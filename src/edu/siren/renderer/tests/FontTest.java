package edu.siren.renderer.tests;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.renderer.Font;
import edu.siren.renderer.Gui;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class FontTest {
    public static void main(String[] args) throws IOException, LWJGLException {
        Screen screen = new Screen("Screen Test", 640, 480);
        Font font = new Font("res/tests/fonts/nostalgia.png", 24);
        Gui gui = new Gui();
        Shader shader = new Shader("res/tests/glsl/gui.vert",
                "res/tests/glsl/gui.frag");
        gui.bindToShader(shader);
        Random random = new Random();
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
