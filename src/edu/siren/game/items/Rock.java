package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class Rock  extends Item {

	public Rock(Profile profile) {
		super("Rock", profile);
		this.setIcon("res/game/maps/assets/item-shop/rock.png");
		// TODO Auto-generated constructor stub
	}

	@Override
    public void use(Player player, BattleManager state) {
	}

}
