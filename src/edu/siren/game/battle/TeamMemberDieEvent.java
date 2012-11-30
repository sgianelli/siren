package edu.siren.game.battle;

import edu.siren.game.Player;

public interface TeamMemberDieEvent {
    public void event(Player died, Team members);
}
