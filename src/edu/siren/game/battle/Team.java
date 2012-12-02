package edu.siren.game.battle;

import java.util.ArrayList;

import edu.siren.game.Player;

public class Team {
    public String name = "Unknown";
    public ArrayList<Player> players;
            
    public Team(String teamname, Player... members) {
        name = teamname;
        players = new ArrayList<Player>();
        for (Player player : members)
            players.add(player);
    }

    public void remove(Player member) {
    }
}
