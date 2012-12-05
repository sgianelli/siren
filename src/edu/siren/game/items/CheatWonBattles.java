package edu.siren.game.items;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

public class CheatWonBattles  extends Item {
	
	private int power = 1;
	
	public CheatWonBattles(Profile profile) {
		super("Cheatyface", profile);
		this.setIcon("res/game/maps/assets/item-shop/cheat-win.png");
	}

	public void use(Player player, BattleManager state) {
	    player.attack += this.power;
	}

}
