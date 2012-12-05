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
import edu.siren.core.tile.World.Environment;
import edu.siren.game.Player;
import edu.siren.game.gui.Intro;
import edu.siren.game.gui.Title;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Link;
import edu.siren.gui.Gui;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class JSMapTest {
    
    public static JSWorld reload() throws ScriptException, LWJGLException, IOException {
        // Invoke a screen and a new JavaScript engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");

        // Read in all the data
        FileInputStream stream = new FileInputStream(new File("res/game/maps/basic.js"));
        FileChannel fc = stream.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                fc.size());
        stream.close();
        String content = Charset.defaultCharset().decode(bb).toString();
                
        // Create some basic map bindings
        Bindings bindings = engine.createBindings();

        // Expose a wrapped JavaScript world object
        JSWorld jsworld = new JSWorld();
        bindings.put("World", jsworld);
        
        // Compile the engine
        Compilable compEngine = (Compilable)engine;
        CompiledScript cs = compEngine.compile(content);
        cs.eval(bindings); 
        
        return jsworld;
    }
    
    public static void main(String[] args) 
            throws IOException, ScriptException, LWJGLException 
    {    
        Screen screen = new Screen("JSMapTest", 512, 448);

        // Start the intro
        Gui intro = new Intro(screen);
        while (intro.running()) {
            intro.run();
        }
        
        // Load the real world
        JSWorld jsworld = reload();
        Player player = new Link();
        player.x = 160;
        player.y = 200;
        // player.snapToGrid(32, 32);
        player.controllable = true;
        player.follow = true;
        
        Player diglett = new Diglett();
        diglett.setPosition(180, 770);
        diglett.lastMovement = 2;
        diglett.controllable = false;
        diglett.id = "diglett";
        
        jsworld.world.addEntity(player);
        jsworld.world.addEntity(diglett);
        
        // Explicit 2D shader
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert",
                "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        
        jsworld.world.zoomIn();
        
        // Add the overlays
        Tile veryCloudy = new Tile("res/tests/img/very-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile slightlyCloudy = new Tile("res/tests/img/slightly-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile pixelCloudy = new Tile("res/tests/img/pixel-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile plasma = new Tile("res/tests/img/plasma.png", 0, 0, 8096, 8096, 1, 1);
        Tile activeOverlay = slightlyCloudy;
        
        SpriteSheet rainsheet = SpriteSheet.fromCSS
                ("res/game/sprites/rain.png",
                "res/game/sprites/rain.css");
        
        Sprite rain = rainsheet.createSprite(
                new Animation("dark-rain", "rain-", 1, 4, 100));
        
       rain.spriteY = 0;
       rain.spriteX = 0;
       rain.bounds.width = 8096;
       rain.bounds.height = 8096;
       
       // Disable the camera
        jsworld.world.changeEnvironment(Environment.DUSK, 500.0f);
        
        // Draw as usual
        boolean down = false;
        while (screen.nextFrame()) {
            jsworld.world.draw();
            
            shader.use();   
            rain.bounds.x -= 0.5f;
            rain.bounds.y -= 0.75f;
            rain.bounds.y %= 4096;
            rain.bounds.x %= 4096;
            rain.draw();
            activeOverlay.bounds.x -= 0.5f;
            activeOverlay.bounds.y -= 0.75f;
            activeOverlay.bounds.x %= 4096;
            activeOverlay.bounds.y %= 4096;
            activeOverlay.createIndexVertexBuffer(10, 10);
            activeOverlay.draw();
            shader.release();            
            
            if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                if (down)
                    continue;
                jsworld = reload();
                jsworld.world.addEntity(player);
                jsworld.world.getCamera().setPosition(-player.x, -player.y);
                jsworld.world.getCamera().forceUpdate = true;
                down = true;
            } else {
                down = false;
            }
            
            if (jsworld.world.battleManager != null) {
                jsworld.world.battleManager.think();
            }
        }
        
        if (jsworld.musicThread != null)
            jsworld.musicThread.interrupt();
        screen.cleanup();
    }
}
