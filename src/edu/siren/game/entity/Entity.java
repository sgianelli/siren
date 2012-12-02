package edu.siren.game.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.ai.AI;
import edu.siren.renderer.Drawable;

public abstract class Entity implements Drawable {
    protected AI ai;
    protected String name;
    protected JSONObject json;
    protected EntityStats entityStats = new EntityStats();
    public Sprite sprite = new Sprite();
    protected World world = null;
    protected boolean move = true;
    protected int preventMovement = -1;
    public int lastMovement = 0;
    public ArrayList<Rectangle> preventCollision = new ArrayList<Rectangle>();
    public String id = "";
    
    public class EntityStats {
        int health;
    }

    public Entity(String config, AI ai) {
        FileInputStream stream = null;
        try {
            String content = "";
            stream = new FileInputStream(new File(config));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size());
            stream.close();
            content = Charset.defaultCharset().decode(bb).toString();
            json = new JSONObject(content);
            this.name = json.getString("name");
            entityStats.health = json.getInt("health");
            if (ai != null) {
                this.ai = ai;
                this.ai.attach(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean is(String what) {
        if (what.equals(":player")) {
            return this instanceof Player;
        } else if (what.equals(":controlledPlayer")) {
           return this instanceof Player && ((Player)this).controllable;
        }
        
        return false;
    }
    
    public void canMove(boolean move) {
        this.move = move;
    }
    
    public boolean canMove() {
        return this.move;
    }
    
    public void stop() {
        canMove(false);
        System.out.println("Stopping");
    }
    
    public Entity() {
    }
    
    public void stopCurrentMove() {
        preventMovement = lastMovement;
    }

    public Rectangle getRect() {
        return sprite.getRect();
    }
    
    public void preventTouching(Tile tile) {
        preventCollision.add(tile.bounds);
    }
    
    public boolean touching(float x, float y) {
        return false;
    }

    public void setAI(AI ai) {
        this.ai = ai;
        this.ai.attach(this);
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public World getWorld() {
        return this.world;
    }

    abstract public void draw();
    
    abstract public void think();

    abstract public void moveTo(float x, float y);

    public boolean stopped() {
        return !move;
    }
}
