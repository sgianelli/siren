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

    protected NPC(String config, AI ai) {
        super(config, ai);
        initializeAnimations();
    }

    @SuppressWarnings("unchecked")
    private boolean initializeAnimations() {
        try {
            JSONObject jsonAnims = json.getJSONObject("animations");
            for (String n : (Set<String>) jsonAnims.keySet()) {
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
            return true;
        } catch (JSONException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
