package edu.siren.game.items;

import edu.siren.game.profile.Profile;

public class SpecialPotion extends Item  {
	private int power = 1;
	public SpecialPotion(Profile profile) {
		super("Special Potion", profile);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void use() {
		getProfile().getGameStats().setSpecial(getProfile().getGameStats().getSpecial() + this.power);	
		
	}

}
