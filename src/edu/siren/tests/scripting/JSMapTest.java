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

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.players.Link;
import edu.siren.renderer.Screen;

public class JSMapTest {
    
    public static void main(String[] args) 
            throws IOException, ScriptException, LWJGLException 
    {    
        // Invoke a screen and a new JavaScript engine
        Screen screen = new Screen("JSMapTest", 640, 480);
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
        
        // Create a player and it to the generated world
        World world = jsworld.world;
        Player player = new Link();
        player.follow = true;        
        world.addEntity(player);
        
        // Draw as usual
        while (screen.nextFrame()) {
            world.draw();
        }
        
        screen.cleanup();
    }
}
