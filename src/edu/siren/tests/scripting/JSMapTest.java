package edu.siren.tests.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.ai.Follower;
import edu.siren.game.ai.RandomWalker;
import edu.siren.game.characters.Villager;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Link;
import edu.siren.game.players.Pikachu;
import edu.siren.renderer.Screen;

public class JSMapTest {
    
    public static World reload() throws ScriptException, LWJGLException, IOException {
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
        
        World world = jsworld.world;
        
        return world;
    }
    
    public static void main(String[] args) 
            throws IOException, ScriptException, LWJGLException 
    {    
        Screen screen = new Screen("JSMapTest", 512, 448);
        World world = reload();
        Player player = new Link();
        Player pikachu = new Pikachu();
        player.x = 190;
        player.y = 64;
        pikachu.x = 190;
        pikachu.y = 32;
        player.follow = true;   
        pikachu.controllable = false;
        pikachu.setAI(new Follower(player));
        
        Player diglett = new Diglett();
        diglett.x = 180;
        diglett.y = 375;
        diglett.lastMovement = 2;
        diglett.controllable = false;
        
        Villager v = new Villager("res/tests/scripts/entities/villager-justin.json");
        v.setPosition(180, 400);
        v.setAI(new RandomWalker());
        world.addEntity(v);
        
        world.addEntity(player);
        world.addEntity(pikachu);
        world.addEntity(diglett);
        
        Player d = world.spawn("Diglett");
        d.x = 180;
        d.y = 500;
        d.lastMovement = 2;
        
        // Draw as usual
        boolean down = false;
        while (screen.nextFrame()) {
            world.draw();
            
            if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                if (down)
                    continue;
                world = reload();
                world.addEntity(player);
                world.getCamera().setPosition(-player.x, -player.y);
                world.getCamera().forceUpdate = true;
                down = true;
            } else {
                down = false;
            }
        }
        
        screen.cleanup();
    }
}
