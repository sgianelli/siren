package edu.siren.game.worlds;

import java.io.IOException;

import javax.script.ScriptException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.tile.World;
import edu.siren.game.Player;

public class ItemShop extends World {
    private static final long serialVersionUID = 9009510687263211911L;

    public ItemShop() throws IOException, LWJGLException {
        super();
    }

    public void addPlayer(Player player) {
        player.setPosition(458, 297);
        player.follow = false;
        super.addPlayer(player);
    }
    
    public void create() throws IOException {    
        try {
            JSWorld.load("res/game/maps/item-shop.js", this);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
        this.getCamera().setPosition(-256, -224);
        this.getCamera().zoomOut();
    }
    
    public void guiDraw() {
        while(tempMetaArray.size() > 0 && Keyboard.next()) {
    		if (Keyboard.getEventKeyState()) {
	            if (Keyboard.getEventKey() == Keyboard.KEY_E) {
	                String item = tempMetaArray.get(0);
	                System.out.println("Purchasing: " + item);
	            }
    		}
        }
    }

}
