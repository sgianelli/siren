package edu.siren.game;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.siren.core.Animation;
import edu.siren.core.AnimationFrame;
import edu.siren.core.Sprite;
import edu.siren.game.ai.AI;
import edu.siren.game.entity.Entity;
import edu.siren.game.entity.Interactable;

public class NPC extends Entity implements Interactable {

    public static final String[] baseAnimationsNPC = new String[] { "idle",
            "move_left", "move_right", "move_forward", "move_backward" };

    protected NPC(String config, AI ai) {
        super(config, ai);
        initializeAnimations();
    }

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
                    String spriteFile = sequence.getString(0);
                    int time = sequence.getInt(1);
                    Sprite sprite = new Sprite(spriteFile);
                    animation.addFrame(new AnimationFrame(sprite, time));
                }
                animations.put(n, animation);
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
