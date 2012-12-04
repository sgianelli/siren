package edu.siren.game.status;

import java.io.IOException;

import edu.siren.game.profile.GameStats;
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
	private int experience;

	// Components
	private Text timeOfDayText;
	private Text timeOfDayValue;
	private Text experienceText;
	private Image[] experienceImages;
	
	public GameStatus(Screen screen) throws IOException {
		
		// Call Super
		super();
		
		// Save the Screen
		this.screen = screen;
		
		// Set Dimensions
		this.dimensions(screen.width, screen.height);
				
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
			
			// Create Player Level
			experienceText = new Text("Experience");
			experienceText.position(screen.width -125 -2, 25);
			experienceText.fontScaling(2);
			add(experienceText);
			
			experienceImages = new Image[GameStats.MAX_EXPERIENCE];
			int x = 373;
			int y = 2;
			for(int i=0; i<experienceImages.length; i++) {
				
				// Create experience Image
				experienceImages[i] = new Image("res/game/menu/experience.png", "");
				experienceImages[i].position(x, y);
				
				add(experienceImages[i]);
				
				// Increment x Position
				x += 12;
				
			}

			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
			
		
	}
	

	
	public void update(GameStats gameStats) {

		// Update Time of Day
		timeOfDayValue.text(getTimeOfDay());

		// Update Experiece
		this.experience = gameStats.getExperience();
		
		// Update experience Status
		updateexperience();
		
		// Draw the World
		draw();

	}
	
	
	private void updateexperience() {
		
		for (int i=0; i<GameStats.MAX_EXPERIENCE; i++) {
			
			if (i < getExperience()) {
				experienceImages[i].show();
			} else {
				experienceImages[i].hide();
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
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(int experience) {
		if (experience >= 0 && experience <= GameStats.MAX_EXPERIENCE) {
			this.experience = experience;
		}
	}
	
	
	

}
