package edu.siren.game.characters;

import edu.siren.game.NPC;
import edu.siren.game.ai.RandomWalker;

public class Villager extends NPC {
    public Villager(String villerConfig) {
        super(villerConfig, new RandomWalker());
    }
}
