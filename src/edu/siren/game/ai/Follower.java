package edu.siren.game.ai;

import edu.siren.game.entity.Entity;

public class Follower implements AI {
    public Entity other, self;
    
    public Follower(Entity other) {
        this.other = other;
    }
    
    public void attach(Entity entity) {
        this.self = entity;
    }

    public void think() {
    }

}
