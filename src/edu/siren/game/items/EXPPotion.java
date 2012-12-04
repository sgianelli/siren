package edu.siren.game.items;

import edu.siren.game.profile.Profile;

public class EXPPotion  extends Item {
	private int power = 1;
	public EXPPotion(Profile profile) {
		super("Rare Candy", profile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void use() {
		getProfile().getGameStats().setExperience(getProfile().getGameStats().getExperience() + this.power);
	}

}
