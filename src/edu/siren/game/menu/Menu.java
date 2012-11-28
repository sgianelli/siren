package edu.siren.game.menu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.KeyEvent;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public abstract class Menu implements Gui {

	// Game Components
	private GuiContainer gui;
	private Screen screen;
	
	// Menu Info
	private String name;
	
	// Key Events
	private Map<Integer,KeyEvent> keyEvents;
	
	/**
	 * Constructor to initialize the Menu
	 * 
	 * @param screen
	 */
	public Menu(String name, Screen screen) {

		// Save the Screen
		this.screen = screen;
		
		// Set Menu Name
		this.name = name;
		
		// Create Key Event Hash Map
		keyEvents = new HashMap<Integer, KeyEvent>();
		
		// Initialize the Menu Screen
		if ( !initialize() ) {
			System.err.println("Could Not Initialize the Menu");
		}
		
	}
	
	private boolean initialize() {
		
		// Build Success
		boolean success = true;
		
		try {
			
			// Create the GUI Container
			this.gui = new GuiContainer();
		
			// Create the Window
			Window window = new Window(this.name);
			window.dimensions(screen.width, screen.height);
			
			// Set Background Color
			screen.backgroundColor(0.26f, 0.26f, 0.26f, 1.0f);
						
			// Build the Menu
			build(window);
			
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
	
	public abstract void build(Window window) throws IOException;

	public void show() {
		this.run();
	}
	
	@Override
	public void run() {
		
		// Show the GUI while the Screen is Opened.
        while (screen.isOpened()) {
        	
        	// Escape Key Ends The Menu
        	if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        		break;
        	}
        	
        	// Check Other Keys
        	while(Keyboard.next()) {
	        	if (Keyboard.getEventKeyState()) {
	        		checkKeys(Keyboard.getEventKey());
	        	}
        	}
        	
        	// Clear the Screen
            screen.clear();
            
            // Redraw the Gui
            gui.draw();
            
            // Update the Screen
            screen.update();
            
        }

	}
	
	public String getName() {
		return this.name;
	}

	private void checkKeys(int keypressed) {
			
			KeyEvent event = this.keyEvents.get(keypressed);
			if (event != null) {
				event.event(keypressed);
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
	
	public void addKeyEvent(int key, KeyEvent event) {
		this.keyEvents.put(new Integer(key), event);
	}

	/**
	 * @return the screen
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * @param screen the screen to set
	 */
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
}
