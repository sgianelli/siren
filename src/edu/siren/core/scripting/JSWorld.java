package edu.siren.core.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.LWJGLException;

import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;

/**
 * Wraps the core.tile.World class and provides additional functions
 * for operating on the world as a DOM-like object
 */
public class JSWorld {    
    public World world;
        
    /**
     * jQuery-like selector syntax
     */
    public Tile $(String selector) {
        if (selector.charAt(0) == '#') {
            return world.tiles.get(selector.substring(1));
        }
        return null;
    }
    
    public JSWorld() { }
        
    public void load(String map, String jsonTiles) throws IOException, LWJGLException {
        final String mapTiles = "res/game/maps/" + jsonTiles;

        world = new World() {
            public void create() throws IOException {                  
                // Read in the JSON file
                String content = "";
                FileInputStream stream = new FileInputStream(new File(mapTiles));
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                        fc.size());
                stream.close();
                content = Charset.defaultCharset().decode(bb).toString();  
                                
                // Load the tiles
                try {
                    JSONObject json = new JSONObject(content);  
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        JSONArray jsonTiles = json.getJSONArray(key);

                        // Create a new layer
                        Layer layer = new Layer(key);

                        for (int i = 0; i < jsonTiles.length(); i++) {
                            JSONObject tileEntry;
                            tileEntry = jsonTiles.getJSONObject(i);
                            
                            // These are all required
                            String tilename = tileEntry.getString("tile");
                            int x = tileEntry.getInt("x");
                            int y = tileEntry.getInt("y");
                            int w = tileEntry.getInt("w");
                            int h = tileEntry.getInt("h");
                            String id = null;
                            
                            // Optionally create an id for reference.
                            try {
                                id = (String) tileEntry.get("id");
                            } catch (JSONException e) {
                                id = null;
                            }
                                
                            // Create the new tile and add it to the layer.
                            Tile tile = new Tile(tilename, x, y, w, h);                    
                            tile.id = id;
                            layer.addTile(tile);
                        }
                        
                        // Add the layer to the world
                        this.addLayer(layer);
                    }

                } catch (JSONException e) {
                    System.err.println(e);
                }
            }
        };
    }

    public void draw() {
        world.draw();
    }

}
