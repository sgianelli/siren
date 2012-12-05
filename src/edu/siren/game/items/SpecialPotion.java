package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class SpecialPotion extends Item  {
	private int power = 1;
	public SpecialPotion(Profile profile) {
		super("Special Potion", profile);
		this.setIcon("res/game/maps/assets/item-shop/special-potion.png");
	}

	@Override
    public void use(Player player, BattleManager state) {
	}

}
