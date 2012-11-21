package edu.siren.game.gui;

import java.io.IOException;

import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class Menu implements Gui {

	// Game Components
	private GuiContainer gui;
	private Screen screen;
	
	/**
	 * Constructor to initialize the Menu
	 * 
	 * @param screen
	 */
	public Menu(Screen screen) {
		
		// Save the Screen
		this.screen = screen;
		
		try {
			
			// Create the GUI Container
			this.gui = new GuiContainer();
		
			// Create the Window
			Window window = new Window("Menu");
			window.dimensions(640, 480);
			
			// Create some test
			Text blah = new Text("Blah", 3);
			blah.position(300, 0);
			window.add(blah);
			
			// Add Window to GUI
			gui.add(window);
			
			
		} catch (IOException e) {
			
			// Error Creating Gui
			System.err.println("Error Creating Menu: ");
			System.err.println(e.getMessage());
			System.exit(1);
			
		}
		
	}

	@Override
	public void run() {
        gui.draw();

	}

	@Override
	public boolean running() {
		return gui.enabled();
	}

	@Override
	public GuiContainer getContainer() {
		return gui;
	}
	
}
