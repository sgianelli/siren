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

public class Pangea extends World {
    public Pangea() throws IOException, LWJGLException {
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
        diglett.setPosition(200, 770);
        diglett.lastMovement = 2;
        diglett.controllable = false;
        diglett.id = "diglett";
        
        addEntity(diglett);
    }
    
    public void addPlayer(Player player) {
    	super.addPlayer(player);
    	
    	player.setPositionCenter(20, 120);
    }
    
    public void create() throws IOException {    
        // Load the real world
        try {
            JSWorld.load("res/game/maps/viridian.js", this);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
//        createEntities();
        
//        player.setPositionCenter(0, 120);
        
        // Add the overlays
        veryCloudy = new Tile("res/tests/img/very-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        slightlyCloudy = new Tile("res/tests/img/slightly-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        pixelCloudy = new Tile("res/tests/img/pixel-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        plasma = new Tile("res/tests/img/plasma.png", 0, 0, 8096, 8096, 1, 1);
        activeOverlay = veryCloudy;
        
       rain.spriteY = 0;
       rain.spriteX = 0;
       rain.bounds.width = 8096;
       rain.bounds.height = 8096;
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
