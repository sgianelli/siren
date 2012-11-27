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
        layer.addTile(new Tile("res/tests/img/grass.png", 0, 0, 10000, 10000));
        layer2.addTile(new Tile("res/tests/img/grass.png", -10000, 0, 10000, 10000));

        // Create some catch phrases
        String phrases[] = { "Whoa!", "Hi!", "What are you up to?" };

        // Add random weeds
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(9000) - random.nextInt(50);
            int y = random.nextInt(9000) - random.nextInt(50);
            
            // Place the tree somewhere in the world
            Tile tile = new Tile("res/tests/img/weeds.png", x, y);
            
            // Add the tile to the current layer
            layer.addTile(tile);
        }
        
        // Add random characters
        for (int i = 0; i < 100; i++) {
        	
            int x = random.nextInt(9000) - random.nextInt(50);
            int y = random.nextInt(9000) - random.nextInt(50);
            
            // Place the character somewhere in the world
            Tile tile = new Tile("res/tests/img/characters/justin.png", x, y,
                    16.0f, 25.0f, 1, 1);

            // Put random text with the random characters
            layer.addIndexVertexBuffer(font.print(phrases[random.nextInt(3)],
                    3, x, y + 25, null, 0));
            
            // Add the tile to the current layer
            layer.addTile(tile);
        }

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
