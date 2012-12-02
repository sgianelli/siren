package edu.siren.game.battle;

import edu.siren.game.Player;

public class BattleManager {
    public Team red, blue;
    public Team active;
    
    public BattleManager(Team red, Team blue) {
        this.red = red;
        this.blue = blue;
        this.active = red;
    }
    
    /**
     * Switch to the next team
     */
    public Team next() {
        // Reset the number of moves each player can make for the next
        // frame of the game.
        for (Player player : active.players) {
            player.moves = player.maxMoves;
        }
        active = (active.equals(red) ? blue : red);
        return active;
    }
    
    /**
     * Attempt to select a team member at x, y
     */
    public Player select(Team team, int x, int y) {
        for (Player player : team.players) {
            if (player.moves > 0 && player.getRect().contains(x, y))
                return player;
        }
        return null;
    }
    
    /**
     * Move a player to a grid unit
     */
    public boolean actionMove(Player player, int x, int y) {
        int dx = (int) Math.abs(player.sprite.spriteX - x);
        int dy = (int) Math.abs(player.sprite.spriteY - y);
        int gx = (int) (dx / player.gridWidth);
        int gy = (int) (dy / player.gridHeight);
        if ((gx + gy) < player.moves)
            return false;
        player.moves -= (gx + gy);
        player.moveTo(x, y);
        return true;
    }
}
