package edu.siren.game.ai;

import edu.siren.game.entity.Entity;

public interface AI {
    public void attach(Entity entity);

    public void think();
}
