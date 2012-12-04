package edu.siren.game.items;

import edu.siren.game.profile.Profile;

public class CheatLostBattles  extends Item {
	private int power = 1;
	public CheatLostBattles(Profile profile) {
		super("Cheatyface", profile);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void use() {
		getProfile().getGameStats().setAttack(getProfile().getGameStats().getAttack() + this.power);
		
	}

}
