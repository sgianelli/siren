package edu.siren.tests.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.core.tile.World.Environment;
import edu.siren.game.Player;
import edu.siren.game.gui.Intro;
import edu.siren.game.gui.Title;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Link;
import edu.siren.game.worlds.ItemShop;
import edu.siren.gui.Gui;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class ItemShopTest {
    
    public static void main(String[] args) 
            throws IOException, ScriptException, LWJGLException 
    {    
        Screen screen = new Screen("JSMapTest", 512, 448);
        World world = new ItemShop();

        // Start the intro
        /*
        Gui intro = new Intro(screen);
        while (intro.running()) {
            intro.run();
        }
        */
        
        // Load the real world
        Player player = new Link();
        player.controllable = true;
        player.follow = false;
        world.addPlayer(player);
        
        // Explicit 2D shader
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert",
                "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        
        while (screen.nextFrame()) {
            world.draw();
            
            if (world.gameOver)
                break;
        }
        
        world.cleanup();
        screen.cleanup();
    }
}
