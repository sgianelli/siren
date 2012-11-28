package edu.siren.game.menu;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
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
		
		// Initialize the Menu Screen
		if ( initialize() ) {
			

		}
		
	}
	
	public boolean initialize() {
		
		// Build Success
		boolean success = true;
		
		try {
			
			// Create the GUI Container
			this.gui = new GuiContainer();
		
			// Create the Window
			Window window = new Window("Menu");
			window.dimensions(screen.width, screen.height);
			
			// Create some test
			Text blah = new Text("Blah", 3);
			blah.position(300, 100);
			window.add(blah);
			
			// Add Image of Some Kind
			Image grass = new Image("res/tests/img/grass.png");
			Image grass1 = new Image("res/tests/img/grass.png");
			Image grass2 = new Image("res/tests/img/grass.png");
			Image grass3 = new Image("res/tests/img/grass.png");
			grass.xy(0, 0);
			grass1.xy(100, 100);
			grass2.xy(200, 200);
			grass3.xy(500, 440);
			window.add(grass);
			window.add(grass1);
			window.add(grass2);
			window.add(grass3);
			
			
			// Add Window to GUI
			gui.add(window);
			

			
			
		} catch (IOException e) {
			
			// Error Creating Gui
			System.err.println("Error Creating Menu: ");
			System.err.println(e.getMessage());
			success = false;
			
		}
		
		// We made it this far, so
		return success;
		
		
	}

	@Override
	public void run() {
		
		// Show the GUI while the Screen is Opened.
        while (screen.isOpened()) {
        	
        	// Stop Displaying the Menu
        	if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        		break;
        	}
        	
        	// Clear the Screen
            screen.clear();
            
            // Redraw the Gui
            gui.draw();
            
            // Update the Screen
            screen.update();
            
        }

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
