package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class TransformationPotion extends Item  {
	private int power = 1;
	public TransformationPotion(Profile profile) {
		super("Transformation Potion", profile);
		this.setIcon("res/game/maps/assets/item-shop/transformation-potion.png");
	}

	@Override
    public void use(Player player, BattleManager state) {
	}

}
