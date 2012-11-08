package edu.siren.core.tile;

import java.io.IOException;

import edu.siren.game.entity.Entity;
import edu.siren.game.entity.Interactable;


public class TriggerTile extends Tile {
    public boolean drawTrigger = true;
    public final static String TRIGGER_TEXTURE = "res/tests/img/trigger-tile.png";
    public Interactable interaction;
    
    public TriggerTile(float x, float y, float w, float h, Interactable interaction) 
            throws IOException {
        super(TRIGGER_TEXTURE, x, y, w, h, 4, 4);
    }

    public void draw() {
        if (drawTrigger) {
            super.draw();
        }
    }
    
    public void interact(Entity e, float x, float y) {
        if (e.touching(x, y)) {
            interaction.interact(e);
        }
    }
}
