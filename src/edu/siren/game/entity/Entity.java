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

import edu.siren.core.Sprite;
import edu.siren.game.ai.AI;

public class Entity {
    protected AI ai;
    protected String name;
    protected JSONObject json;
    protected EntityStats entityStats = new EntityStats();
    protected Sprite sprite = new Sprite();

    public class EntityStats {
        int health;
    }

    protected Entity(String config, AI ai) {
        FileInputStream stream = null;
        try {
            String content = "";
            stream = new FileInputStream(new File(config));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size());
            stream.close();
            content = Charset.defaultCharset().decode(bb).toString();
            System.out.println(content);
            json = new JSONObject(content);
            this.ai = ai;
            this.name = json.getString("name");
            entityStats.health = json.getInt("health");
            this.ai.attach(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        sprite.draw();
    }
}
