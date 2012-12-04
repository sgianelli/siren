package edu.siren.game.items;

import edu.siren.game.profile.Profile;

public class TransformationPotion extends Item  {
	private int power = 1;
	public TransformationPotion(Profile profile) {
		super("Transformation Potion", profile);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void use() {
		getProfile().getGameStats().setDefense(getProfile().getGameStats().getDefense() + this.power);	
	}

}
