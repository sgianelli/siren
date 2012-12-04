package edu.siren.game.worlds;

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

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.core.tile.World.Environment;
import edu.siren.game.Player;
import edu.siren.game.gui.Intro;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Link;
import edu.siren.gui.Gui;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class PokemonWorld extends World {
    public PokemonWorld() throws IOException, LWJGLException {
        super();
    }

    public Tile veryCloudy;
    public Tile slightlyCloudy;
    public Tile pixelCloudy;
    public Tile plasma;
    public Tile activeOverlay;
    public SpriteSheet rainsheet;
    public Sprite rain;
    public Player diglett; 
    
    public void createEntities() throws IOException {
        diglett = new Diglett();
        diglett.setPosition(180, 770);
        diglett.lastMovement = 2;
        diglett.controllable = false;
        diglett.id = "diglett";
        addEntity(diglett);
    }
    
    public void create() throws IOException
    {    
        // Load the real world
        try {
            JSWorld.load("res/game/maps/pokemon-world.js", this);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
        createEntities();
        
        // Add the overlays
        veryCloudy = new Tile("res/tests/img/very-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        slightlyCloudy = new Tile("res/tests/img/slightly-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        pixelCloudy = new Tile("res/tests/img/pixel-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        plasma = new Tile("res/tests/img/plasma.png", 0, 0, 8096, 8096, 1, 1);
        activeOverlay = slightlyCloudy;
        
        rainsheet = SpriteSheet.fromCSS
                ("res/game/sprites/rain.png",
                "res/game/sprites/rain.css");
        
        rain = rainsheet.createSprite(
                new Animation("dark-rain", "rain-", 1, 4, 100));
        
       rain.spriteY = 0;
       rain.spriteX = 0;
       rain.bounds.width = 8096;
       rain.bounds.height = 8096;
    }
    
    public void guiDraw() {
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
    }
}
