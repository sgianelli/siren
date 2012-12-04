package edu.siren.game.menu;

import java.io.IOException;

import edu.siren.game.profile.GameStats;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class PowerStoreMenu extends Menu{

	private GameStats gameStats;
	private Text coins;
	
	public PowerStoreMenu(Screen screen) {

		// Call the Super
		super("Power Store", screen);
		
		

	}

	
	
	@Override
	public void build(Window window) throws IOException {
		
		// Set the Title
		//Text text = new Text(getName());
		//text.fontScaling(1);
		//text.position(130, 420);
		//window.add(text);
		Image powerUpTitle = new Image("res/game/menu/powerup.png", "");
		powerUpTitle.position(0,0);
		window.add(powerUpTitle);
		
		// Items 
		Text items = new Text("Items");
		items.fontScaling(2);
		items.position(30, 300);
		window.add(items);
		
		int y = 280;
		
		// Power Up              Power Up        ==   10 Coins
		Text powerUp = new Text("Power Up        ==   10 Coins");
		powerUp.fontScaling(3);
		powerUp.position(40, y);
		window.add(powerUp);
		
		// Health Up              Power Up        ==   10 Coins
		Text healthUp = new Text("Healing         ==   05 Coins");
		healthUp.fontScaling(3);
		healthUp.position(40, y-15);
		window.add(healthUp);
		
		// Experience               Power Up        ==   10 Coins
		Text experience = new Text("Experience      ==  100 Coins");
		experience.fontScaling(3);
		experience.position(40, y-30);
		window.add(experience);
		
		// Coins you have
		Text coinsYouHave = new Text("Coins Available: ");
		coinsYouHave.fontScaling(2);
		coinsYouHave.position(10, 20);
		window.add(coinsYouHave);
		
		// The Coins
		coins = new Text("0000");
		coins.fontScaling(2);
		coins.position(256,  20);
		window.add(coins);
	}

	@Override
	public void show() {
		
		int coinValue = gameStats.getCoins();
		coins.text(String.format("%04d", coinValue));
		
		this.run();
	}


	/**
	 * @return the gameStats
	 */
	public GameStats getGameStats() {
		return gameStats;
	}



	/**
	 * @param gameStats the gameStats to set
	 */
	public void setGameStats(GameStats gameStats) {
		this.gameStats = gameStats;
	}

	
	
}
