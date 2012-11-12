package edu.siren.game.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.tile.World;
import edu.siren.game.ai.AI;

public abstract class Entity {
    protected AI ai;
    protected String name;
    protected JSONObject json;
    protected EntityStats entityStats = new EntityStats();
    protected Sprite sprite = new Sprite();
    protected World world = null;
    
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
    
    public Rectangle getRect() {
        return sprite.getRect();
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

    abstract public void draw();

    abstract public void moveTo(int x, int y);
}
