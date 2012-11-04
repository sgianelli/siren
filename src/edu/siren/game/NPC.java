package edu.siren.game;

import java.io.IOException;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.siren.core.Animation;
import edu.siren.core.AnimationFrame;
import edu.siren.game.ai.AI;
import edu.siren.game.entity.Entity;
import edu.siren.game.entity.Interactable;

public class NPC extends Entity implements Interactable {
    public int desiredX = 0;
    public int desiredY = 0;
    public int x = 0;
    public int y = 0;
    public int speed = 0;

    protected NPC(String config, AI ai) {
        super(config, ai);
        initializeAnimations();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.desiredX = x;
        this.desiredY = y;
    }

    @SuppressWarnings("unchecked")
    private boolean initializeAnimations() {
        try {
            speed = json.getInt("speed");
            JSONObject jsonAnims = json.getJSONObject("animations");
            for (String n : (Set<String>) jsonAnims.keySet()) {
                System.out.println(n);
                JSONArray animCollection = jsonAnims.getJSONArray(n);
                Animation animation = new Animation(n);
                for (int i = 0; i < animCollection.length(); i++) {
                    JSONArray sequence = animCollection.getJSONArray(i);
                    if (sequence.length() < 2) {
                        System.err.println("Expected animation sequence");
                        return false;
                    }
                    String texture = sequence.getString(0);
                    int time = sequence.getInt(1);
                    System.out.println("Adding animation: " + texture
                            + ", time: " + time);
                    animation.addFrame(new AnimationFrame(texture, time));
                }
                sprite.animations.put(n, animation);
            }
            sprite.active = sprite.animations.get("idle");
            return true;
        } catch (JSONException e) {
            System.out.println(e);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void moveTo(int x, int y) {
        this.desiredX = x;
        this.desiredY = y;
    }

    public void draw() {
        ai.think();

        // Go twards X
        if (Math.abs(x - desiredX) > speed) {
            if (x < desiredX) {
                this.x += speed;
            } else if (x >= desiredX) {
                this.x -= speed;
            }
        }

        // Go towards Y
        if (Math.abs(y - desiredY) > speed) {
            if (y < desiredY) {
                y += speed;
            } else if (y >= desiredY) {
                y -= speed;
            }
        }

        sprite.spriteX = this.x;
        sprite.spriteY = this.y;

        sprite.draw();
    }
}
