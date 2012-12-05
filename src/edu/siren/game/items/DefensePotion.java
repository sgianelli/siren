package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class DefensePotion  extends Item {
	private int power = 1;
	public DefensePotion(String name, Profile profile) {
		super("Defence Potion", profile);
		this.setIcon("res/game/maps/assets/item-shop/defense-potion.png");
	}

	public void use(Player player, BattleManager state) {
	    player.attack += this.power;
	}

}
