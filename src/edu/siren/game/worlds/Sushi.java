package edu.siren.game.worlds;

import java.io.IOException;

import javax.script.ScriptException;

import org.lwjgl.LWJGLException;

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Pikachu;

public class Sushi extends World {
    public Sushi() throws IOException, LWJGLException {
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
        diglett = new Pikachu();
        diglett.setPosition(100, 370);
        diglett.lastMovement = 2;
        diglett.controllable = false;
        diglett.id = "Pikachu";
        addEntity(diglett);
    }
    
    public void create() throws IOException
    {    
        // Load the real world
        try {
            JSWorld.load("res/game/maps/safari.js", this);
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
        activeOverlay = pixelCloudy;
    }
    
    public void guiDraw() {
        activeOverlay.bounds.x -= 0.5f;
        activeOverlay.bounds.y -= 0.75f;
        activeOverlay.bounds.x %= 4096;
        activeOverlay.bounds.y %= 4096;
        activeOverlay.createIndexVertexBuffer(10, 10);
        activeOverlay.draw();
    }

}
