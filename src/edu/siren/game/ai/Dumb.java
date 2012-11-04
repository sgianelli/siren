package edu.siren.game.ai;

import edu.siren.game.entity.Entity;

public class Dumb implements AI {
    Entity entity;

    @Override
    public void attach(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void think() {
    }

}
