package edu.siren.game.worlds;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;

import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;


public class TestBox extends World {

    public TestBox(int width, int height) throws IOException, LWJGLException {
        super();
    }
    
    @Override
	public void create() throws IOException {
        Random random = new Random();
        
        // Create a new layer
        Layer layer = new Layer();
        Layer layer2 = new Layer();
        
        // Add some grass
        layer.addTile(new Tile("res/tests/img/grass.png", -1000, -1000, 10000, 10000));
        layer2.addTile(new Tile("res/tests/img/grass.png", -10000, 0, 10000, 10000));

        // Add random weeds
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(9000) - random.nextInt(50);
            int y = random.nextInt(9000) - random.nextInt(50);
            
            // Place the tree somewhere in the world
            Tile tile = new Tile("res/tests/img/weeds.png", x, y);
            
            // Add the tile to the current layer
            layer.addTile(tile);
        }
        
        // Place the character somewhere in the world
        Tile t = new Tile("res/tests/img/characters/justin.png", 64, 0,
                24.0f, 24.0f, 1, 1);

        // Add the tile to the current layer
        layer.addTile(t);

        // Add random trees
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(9000) - random.nextInt(50);
            int y = random.nextInt(9000) - random.nextInt(50);
            
            // Place the tree somewhere in the world
            Tile tile = new Tile("res/tests/img/tree.png", x, y);
            
            // Add the tile to the current layer
            layer.addTile(tile);
        }
        
        

        // Add the layer to all the layers
        layers.add(layer);
        layers.add(layer2);

    }

}
