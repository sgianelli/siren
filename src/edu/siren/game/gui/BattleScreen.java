package edu.siren.game.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Font;
import edu.siren.renderer.Screen;

public class BattleScreen implements Gui {

	// Game Components
    final private GuiContainer gui = new GuiContainer();
	private Screen screen;
	private Image banner;
	
	/**
	 * Constructor to initialize the Menu
	 * 
	 * @param screen
	 * @throws IOException 
	 */
	public BattleScreen(Screen screen) throws IOException {
		// Save the Screen
		this.screen = screen;
        Font font = gui.font;
        Window window = new Window("Battle Screen");
	}
	
	@Override
	public void run() {
        // Redraw the Gui
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