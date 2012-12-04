package edu.siren.game.characters;

import edu.siren.game.Actor;
import edu.siren.game.ai.RandomWalker;

public class Villager extends Actor {
    private static final long serialVersionUID = -6600294838112916778L;

    public Villager(String villerConfig) {
        super(villerConfig, new RandomWalker());
    }

    @Override
    public void think() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void renderStatus() {
        // TODO Auto-generated method stub
        
    }
}
