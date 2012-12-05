package edu.siren.game.battle;

import java.util.ArrayList;

import edu.siren.game.Player;

public class Team {
    public String name = "Unknown";
    public ArrayList<Player> players = new ArrayList<Player>();
    public int money = 0;
            
    public Team(String teamname, Player... members) {
        name = teamname;
        for (Player player : members)
            players.add(player);
    }

    public Team() { }

    public void remove(Player member) {
    }
}
