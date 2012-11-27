package edu.siren.tests.game;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.gui.BattleScreen;
import edu.siren.game.players.Link;
import edu.siren.game.worlds.TestBox;
import edu.siren.gui.Gui;
import edu.siren.renderer.Screen;

public class TurnBasedTest {

    /**
     * @param args
     * @throws IOException 
     * @throws LWJGLException 
     */
    public static void main(String[] args) throws IOException, LWJGLException {
        /*
        Player p1 = new Pickachu();
        Player p2 = new Pickachu();
        
        Team teama = new Team(pikachu, 
        */
		Screen screen = new Screen("Screen", 512, 448);
        
        World world = new TestBox(1024, 1024);
        
        // Add the player
        Player player = new Link();
        player.setPosition(100, 100);
        world.addEntity(player);
        world.getCamera().setPosition(-200, -200);

        // Disable the camera
        world.getCamera().disable();

        // Start the intro
        Gui battlescreen = new BattleScreen(screen);
        while (screen.isOpened()) {
            screen.clear();
            world.draw();
            battlescreen.run();
            screen.update();
        }
        
        screen.cleanup();
    }

}
