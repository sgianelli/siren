package edu.siren.game.menu;

import java.io.IOException;

import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class PowerStoreMenu extends Menu{

	public PowerStoreMenu(Screen screen) {

		// Call the Super
		super("Power Store", screen);
		
		

	}

	@Override
	public void build(Window window) throws IOException {
		
		// Set the Title
		Text text = new Text(getName());
		text.fontScaling(2);
		text.position(125, 420);
		window.add(text);
		
		// Items 
		
		
		
	}

}
