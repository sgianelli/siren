package edu.siren.game.entity;

import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import edu.siren.core.Animation;
import edu.siren.game.ai.AI;

public class Entity {
    protected AI ai;
    protected String name;
    protected JSONObject json;
    protected EntityStats entityStats;
    protected HashMap<String, Animation> animations;

    public class EntityStats {
        int health;
    }

    protected Entity(String config, AI ai) {
        try {
            String content = new Scanner(config).useDelimiter("\\Z").next();
            json = new JSONObject(content);
            this.ai = ai;
            this.name = json.getString("name");
            entityStats.health = json.getInt("health");
            this.ai.attach(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        // TODO Auto-generated method stub

    }
}
