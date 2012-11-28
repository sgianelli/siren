package edu.siren.game.status;

import java.io.IOException;

import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.renderer.Font;
import edu.siren.renderer.Screen;

public class GameStatus extends GuiContainer {

	// Window Elements 
	private Screen screen;
	private Font font;
	
	// Status Information
	private String timeOfDay;
	private int health;
	
	// Game Constants
	public final static int MAX_HEALTH = 11;

	// Components
	private Text timeOfDayText;
	private Text timeOfDayValue;
	private Text healthText;
	private Image[] healthImages;
	
	public GameStatus(Screen screen) throws IOException {
		
		// Call Super
		super();
		
		// Save the Screen
		this.screen = screen;
		
		// Set Dimensions
		this.dimensions(screen.width, screen.height);
		
		// Set Health
		setHealth(MAX_HEALTH);
		
		// Build the Game Status
		buildComponents();
		
	}
	
	private void buildComponents() {
		
		try {

			// Set the Font Sprite 
			font = new Font("res/tests/fonts/proggy.png");
			
			// Create the Status Bottom Panel
			Image statusPanel = new Image("res/game/menu/status-panel.png", "");
			statusPanel.position(2, 4);
			add(statusPanel);
			
			// Create Time of Day Text
			timeOfDayText = new Text("Time of Day");
			timeOfDayText.position(6, 25);
			timeOfDayText.fontScaling(2);
			add(timeOfDayText);
			
			// Create Time of Day
			timeOfDayValue = new Text(getTimeOfDay());
			timeOfDayValue.position(10, 5);
			timeOfDayValue.fontScaling(2);
			add(timeOfDayValue);
			
			// Create Health
			healthText = new Text("Health");
			healthText.position(screen.width -105 -2, 25);
			healthText.fontScaling(2);
			add(healthText);
			
			healthImages = new Image[MAX_HEALTH];
			int x = 373;
			int y = 5;
			for(int i=0; i<healthImages.length; i++) {
				
				// Create Health Image
				healthImages[i] = new Image("res/game/menu/health.png", "");
				healthImages[i].position(x, y);
				
				add(healthImages[i]);
				
				// Increment x Position
				x += 12;
				
			}

			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
			
		
	}
	

	
	public void update() {

		// Update Time of Day
		timeOfDayValue.text(getTimeOfDay());

		// Update Health Status
		updateHealth();
		
		// Draw the World
		draw();

	}
	
	
	private void updateHealth() {
		
		for (int i=0; i<MAX_HEALTH; i++) {
			
			if (i < getHealth()) {
				healthImages[i].show();
			} else {
				healthImages[i].hide();
			}
			
		}
		
		
	}
	
	
	/**
	 * @return the timeOfDay
	 */
	public String getTimeOfDay() {
		return timeOfDay;
	}

	/**
	 * @param timeOfDay the timeOfDay to set
	 */
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		if (health >= 0 && health <= MAX_HEALTH) {
			this.health = health;
		}
	}
	
	
	

}
