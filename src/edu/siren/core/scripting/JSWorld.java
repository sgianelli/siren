package edu.siren.core.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.battle.Team;
import edu.siren.game.battle.TeamMemberDieEvent;
import edu.siren.game.battle.TeamVictoryEvent;
import edu.siren.game.entity.Entity;
import edu.siren.gui.Gui;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Shader;

/**
 * Wraps the core.tile.World class and provides additional functions
 * for operating on the world as a DOM-like object
 */
public class JSWorld {    
    public World world;
        
    /**
     * jQuery-like selector syntax
     */
    public Object $(String selector) {
        if (selector.charAt(0) == '#') {
            return world.tiles.get(selector.substring(1));
        } else if (selector.charAt(0) == '%') {
            return world.getEntityById(selector.substring(1));
        }
        return null;
    }
    
    public World save() {
        return world;
    }
    
    /**
     * @return Current resolution timer.
     */
    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    public World fightable(String jsonTiles, Team a, Team b,
            TeamMemberDieEvent aMemberDieEvent, TeamVictoryEvent aVictoryEvent,
            TeamMemberDieEvent bMemberDieEvent, TeamVictoryEvent bVictoryEvent) 
                    throws IOException, LWJGLException
    {
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert",
                "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        
        // Draw the battle sequence
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < Display.getWidth(); i += 96) {
            for (int j = 0; j < Display.getHeight(); j+= 96) {
                Tile tile = new Tile("res/game/gui/black.png", i, j, 96, 96);
                tiles.add(tile);
            }
        }
        
        double ftime = getTime();
        int drawTiles = 0;
        
        while (drawTiles < tiles.size()) {
            double ctime = getTime();
            if ((ctime - ftime) > 15) {
                drawTiles++;
                ftime = ctime;
            }
            
            shader.use();
            for (int i = 0; i < drawTiles; i++) {
                tiles.get(i).draw();
            }
            shader.release();
            Display.update();
        }
                
        
        load("Battle Sequence", jsonTiles);
        
        Camera camera = world.getCamera();
        camera.setPosition(-256, -224);
        
        // Add A team
        int x = 256 - (a.players.size() * 32), y = 32;
        for (Player player : a.players) {
            player.setPosition(x, y);
            player.follow = false;
            player.controllable = false;
            player.lastMovement = 1;
            player.drawStatus = true;
            player.snapToGrid(32, 32);
            world.addEntity(player);
            x += 32;
        }
        
        x = 256 - (b.players.size() * 32);
        y = 416;
        for (Player player : b.players) {
            player.setPosition(x, y);
            player.snapToGrid(32, 32);
            player.lastMovement = 2;
            player.follow = false;
            player.drawStatus = true;
            player.controllable = true;
            world.addEntity(player);
        }
        
        
        ftime = getTime();
        drawTiles = tiles.size() - 1;
        while (drawTiles >= 0) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            world.draw();
            double ctime = getTime();
            if ((ctime - ftime) > 15) {
                drawTiles--;
                ftime = ctime;
            }
            
            shader.use();
            for (int i = 0; i < drawTiles; i++) {
                tiles.get(i).draw();
            }
            shader.release();
            Display.update();
        }
        
        return world;
    }
    
    public Team createTeam(String teamname, Player[] players) {
        return new Team(teamname, players);
    }
    
    public Entity asEntity(Object object) {
        return (Entity) object;
    }
    
    public Tile asTile(Object object) {
        return (Tile) object;
    }
    
    public Player spawn(String name, int x, int y) {
        return world.spawn(name, x, y);
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
