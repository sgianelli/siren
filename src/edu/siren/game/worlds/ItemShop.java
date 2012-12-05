package edu.siren.game.worlds;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.script.ScriptException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.scripting.JSWorld;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.items.Item;
import edu.siren.game.profile.Profile;

public class ItemShop extends World {
    private static final long serialVersionUID = 9009510687263211911L;
    public boolean mouseDown = false;

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
        try {
            if (!mouseDown && Keyboard.isKeyDown(Keyboard.KEY_E)) {
                mouseDown = true;
                int coins = Profile.active.getGameStats().getCoins();
                String item = tempMetaArray.get(0);
                System.out.println("Purchasing: " + item);
                String[] pair = item.split(":");
                String itemName = pair[0];
                int cost = Integer.parseInt(pair[1]);
                System.out.println("Trying to buy " + itemName + " for " + cost);
                if (cost > coins) {
                    System.out.println("You don't have enough money");
                    return;
                }
                Profile.active.addCoins(-cost);
                
                // Oh this line is sexy.
                Item itemClass = (Item) Class.forName("edu.siren.game.items." + itemName).getDeclaredConstructors()[0].newInstance(Profile.active);
                Profile.active.getGameStats().addItem(itemClass);
                System.out.println("Purchased " + itemName + " for " + cost + " coins");
            } else if (!Keyboard.isKeyDown(Keyboard.KEY_E)) {
                mouseDown = false;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
