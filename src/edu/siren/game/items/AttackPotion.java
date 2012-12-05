package edu.siren.game.items;

import javax.swing.Icon;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class AttackPotion extends Item  {

	private int power = 1;
	public AttackPotion(Profile profile) {
		super("Attack Potion", profile);
		this.setIcon("res/game/maps/assets/item-shop/attack-potion.png");
	}


	@Override
	public void use(Player player, BattleManager state) {
	    player.attack += this.power;
	}

}
