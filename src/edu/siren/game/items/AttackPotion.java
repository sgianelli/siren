package edu.siren.game.items;

import javax.swing.Icon;

import edu.siren.game.profile.Profile;

public class AttackPotion extends Item  {

	private int power = 1;
	public AttackPotion(Profile profile) {
		super("Attack Potion", profile);
		// TODO Auto-generated constructor stub
		Icon icon = createImageIcon("res/game/","");
//		
//		this.setIcon(icon);
	}


	@Override
	public void use() {
		getProfile().getGameStats().setAttack(getProfile().getGameStats().getAttack() + this.power);
		
	}

}
