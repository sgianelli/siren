package edu.siren.game.items;

import edu.siren.game.profile.GameStats;
import edu.siren.game.profile.Profile;

public class MagicMoveStone extends Item {

	private int movesAllowed;
	
	public MagicMoveStone(Profile profile) {

		this(profile, GameStats.DEFAULT_MOVES);

	}
	
	public MagicMoveStone(Profile profile, int movesAllowed) {
		
		// Call the Super 
		super("Magic Move Stone", profile);
		
		
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

	@Override
	public void use() {
		
		// Add Moves Allowed to the Game Stats
		int movesAllowed = getProfile().getGameStats().getMovesAllowed();
		
		// Increase the Moves Allowed
		getProfile().getGameStats().setMovesAllowed(movesAllowed + this.movesAllowed);
		
		
	}

	
	
}
