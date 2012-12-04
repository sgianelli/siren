package edu.siren.game.menu;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

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
				
		// Set Title
		Text title = new Text(getName(), 1);
		title.position(152, getScreen().height - 50);
		window.add(title);

		// Movement Positions
		Text movement = new Text("Movement", 2);
		movement.position(25, 350);
		window.add(movement);
		Text moveLeft = new Text("A - Move Left", 3);
		moveLeft.position(35, 330);
		window.add(moveLeft);
		Text moveRight = new Text("D - Move Right", 3);
		moveRight.position(35, 315);
		window.add(moveRight);
		Text moveUp = new Text("W - Move Up", 3);
		moveUp.position(35, 300);
		window.add(moveUp);
		Text moveDown = new Text("S - Move Down", 3);
		moveDown.position(35, 285);
		window.add(moveDown);

		
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
