package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class EXPPotion  extends Item {
	private int power = 1;
	public EXPPotion(Profile profile) {
		super("Rare Candy", profile);
		this.setIcon("res/game/maps/assets/item-shop/exp-potion.png");
	}

	@Override
    public void use(Player player, BattleManager state) {
		getProfile().getGameStats().setExperience(getProfile().getGameStats().getExperience() + this.power);
	}

}
