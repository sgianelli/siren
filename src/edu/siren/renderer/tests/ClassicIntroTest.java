package edu.siren.renderer.tests;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import edu.siren.audio.AudioUtil;
import edu.siren.core.Tile;
import edu.siren.core.World;
import edu.siren.game.characters.Villager;
import edu.siren.renderer.Font;
import edu.siren.renderer.FontFrame;
import edu.siren.renderer.FontSequence;
import edu.siren.renderer.Gui;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;
import edu.siren.renderer.Util;

public class ClassicIntroTest {
    
    public static void main(String[] args) throws IOException, LWJGLException {
        Screen screen = new Screen("Screen", 640, 480);
        World world = new World(1024, 1024);
        Font font = new Font("res/tests/fonts/nostalgia.png", 24);
        Random random = new Random();
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Villager v = new Villager(
                        "res/tests/scripts/entities/villager-justin.json");
                v.setPosition(random.nextInt(1024) - random.nextInt(1024) * 2, random.nextInt(1024) - random.nextInt(1024) * 2);
                world.addEntity(v);
            }
        }
        
        // Bind Gui and its shader
        Gui gui = new Gui();
        Shader shader = new Shader("res/tests/glsl/gui.vert",
                "res/tests/glsl/gui.frag");
        gui.bindToShader(shader);
        
        try { Thread.sleep(5000); } catch (Exception e) { }

        
        AudioUtil.playBackgroundMusic("res/tests/sounds/adagio.ogg");
        
        final String beep = "res/tests/sounds/beep.wav";
        
        FontSequence sequence = font.printFrames(
                false,
                new FontFrame("Long ago, in the beautiful\nkingdom of Siren surrounded\nby mountains and forests...", 2500, 1000, beep),
                new FontFrame("Legends told of an omnipotent\nand omniscent Golden Power\nthat resided in a hidden land.", 2500, 1000, beep),
                new FontFrame("Many creatures sought this\nGolden Power and failed.", 2000, 1000, beep),
                new FontFrame("The Gods, aware of their\nmistake separated the world\ninto pieces.", 2300, 1000, beep),
                new FontFrame("As generations carried through\nthe years the creatures forgot\nof their original kingdom of Siren.", 2800, 1000, beep),
                new FontFrame("As the years grew, so did\na darkening and evil force.", 2000, 1000, beep),
                new FontFrame("The Gods weakened by this force\nopened up a world for help.", 2100, 1000, beep),
                new FontFrame("\"Help us!\", they cried.", 1500, 500, beep),
                new FontFrame("\"Help us and fulfill your destiny.\"", 1700, 1000, beep),
                new FontFrame("          SIREN", 500, 5000));
        
        Tile white = new Tile("res/tests/img/bg.png", 0, 0, 640.0f, 480.0f, 1, 1);
        boolean intro = true;
        

        world.camera.enable = false;
        
        while (screen.isOpened()) {
            screen.clear();
            
            world.shader.use();
            world.draw();
            world.shader.release();

            if (intro) {
                shader.use();
                white.draw();
                sequence.draw(150, 260, 2);
                if (sequence.end()) {
                    intro = false;
                    world.camera.enable = true;
                }
                shader.release();
            }
                        
            screen.update();
        }
        screen.cleanup();
    }
}
