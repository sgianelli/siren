package edu.siren.game.items;

import edu.siren.game.profile.Profile;

public class DefensePotion  extends Item {
	private int power = 1;
	public DefensePotion(String name, Profile profile) {
		super("Defence Potion", profile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void use() {
		getProfile().getGameStats().setDefense(getProfile().getGameStats().getDefense() + this.power);	
	}

}
