package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.GameStats;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class MagicMoveStone extends Item {

	private int movesAllowed;
	
	public MagicMoveStone(Profile profile) {

		this(profile, GameStats.DEFAULT_MOVES);

	}
	
	public MagicMoveStone(Profile profile, int movesAllowed) {
		// Call the Super 
		super("Magic Move Stone", profile);
		this.setIcon("res/game/maps/assets/item-shop/magic-stone.png");
	}

	/**
	 * @return the movesAllowed
	 */
	public int getMovesAllowed() {
		return movesAllowed;
	}

	/**
	 * @param movesAllowed the movesAllowed to set
	 */
	public void setMovesAllowed(int movesAllowed) {
		this.movesAllowed = movesAllowed;
	}

    public void use(Player player, BattleManager state) {
		// Increase the Moves Allowed
		player.maxMoves += 1;
		player.moves = player.maxMoves;
	}

	
	
}
