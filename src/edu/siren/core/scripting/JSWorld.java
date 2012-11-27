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

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
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
                            String comment = tileEntry.optString("comment");
                            if (comment.length() > 0) continue;
                            
                            int x = tileEntry.getInt("x");
                            int y = tileEntry.getInt("y");
                            
                            // For a sprite
                            String spritename = tileEntry.optString("sprite");
                            String klass = tileEntry.optString("class");
                            String id = tileEntry.optString("id");
                            
                            if (spritename.length() > 0) {
                                int msec = tileEntry.getInt("time");
                                int frames = tileEntry.getInt("frames");
                                Sprite sprite = this.sprites.createSprite(
                                        new Animation("idle", spritename + "-", 1, frames, msec));
                                sprite.active = sprite.animations.get("idle");
                                sprite.spriteX = x;
                                sprite.spriteY = y;
                                sprite.klass = klass;
                                sprite.id = id;
                                layer.addDrawable(sprite);
                                continue;
                            }
                            
                            // These are all required for a tile
                            String tilename = tileEntry.getString("tile");
                            int w = tileEntry.optInt("w");
                            int h = tileEntry.optInt("h");
                            boolean solid = tileEntry.optBoolean("solid");
                            boolean tileable = tileEntry.optBoolean("tileable");
                            
                            // Create the new tile and add it to the layer.
                            Tile tile = new Tile(tilename, x, y, w, h, tileable);                    
                            tile.id = id;
                            tile.klass = klass;
                            tile.solid = solid;
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
