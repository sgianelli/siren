package edu.siren.game.menu;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import edu.siren.gui.Image;
import edu.siren.gui.KeyEvent;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class HelpMenu extends Menu {

	public HelpMenu(Screen screen) {
		super("Help Menu", screen);
	}
	
	
	@Override
	public void build(Window window) throws IOException {
		
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
		
		addKeyEvent(Keyboard.KEY_A, new KeyAEvent());
		
	}
	
	private void printMessage(String message) {
		System.out.println("Message From Menu: " + getName());
		System.out.println("\t" + message);
	}
	
	private class KeyAEvent implements KeyEvent {

		@Override
		public boolean event(Integer key) {

			if (key.intValue() == Keyboard.KEY_A) {
				HelpMenu.this.printMessage("Hello... The Key Pressed was A");
			}
		
		
			return true;
		}
		
	}
	
}
