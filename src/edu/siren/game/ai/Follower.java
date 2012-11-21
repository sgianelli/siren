package edu.siren.game.ai;

import edu.siren.game.entity.Entity;

public class Follower implements AI {
    public Entity other;
    
    public Follower(Entity other) {
        this.other = other;
    }
    
    public void attach(Entity entity) {
    }

    public void think() {
    }

}
