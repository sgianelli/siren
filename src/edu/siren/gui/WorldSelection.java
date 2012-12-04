package edu.siren.gui;

import java.io.IOException;

import edu.siren.core.tile.World;
import edu.siren.renderer.Screen;

/**
 * Allow a user to Select A world
 * 
 * @author georgeamaya
 *
 */
public class WorldSelection implements Gui {

	// Containers
	private Screen screen;
	private GuiContainer gui;
	private Window window;
	
	// Image Selections
	private Image pangeaUnselect;
	private Image sushiUnselect;
	private Image pangeaSelect;
	private Image sushiSelect;
	
	// Selected World
	private String worldName;
	private boolean worldSelected;
	
	public WorldSelection(Screen screen) throws IOException {
		
		super();
		
		// Save the Screen
		this.screen = screen;
		
		// Create the Gui
		this.gui = new GuiContainer();
				
		// Build Guid Shit
		buildTheFuckingGui();
		
		// World Selected 
		worldSelected = false;

	}
	
	public World selectWorld() {
		
		// Run the Selection Screen
		run();
		
		// Get the world
		if (worldName.equals("Pangea")) {
			return new Pangea();
		} else {
			return new Sushi();
		}
		
		// Oops... Not Sure how we'll get here
		return null;
		
	}
	
	private void buildTheFuckingGui() throws IOException {
		
		// Create a Window
		window = new Window("World Selection");
		
		// Create Dimensions
		window.dimensions(screen.width, screen.height);

		// Image for prettiness
		Image b = new Image("res/game/gui/black-background.png");
		b.position(0, 0);
		window.add(b);	
		
		// Image for prettiness
		Image title = new Image("res/game/menu/world/choose-your-world.png");
		title.position(0, 388);
		window.add(title);

		// Image for
		Image worldsToChoose = new Image("res/game/menu/world/worlds-selection.png");
		worldsToChoose.position(0, 222);
		window.add(worldsToChoose);
		
		// Image for Pangea
		Image pangea = new Image("res/game/menu/world/pangea.png");
		pangea.position(0, 189);
		window.add(pangea);

		// Image for Sushi
		Image sushi = new Image("res/game/menu/world/sushi.png");
		sushi.position(256, 189);
		window.add(sushi);
		
		
		// Image for Play Button
		Image play = new Image("res/game/menu/world/play-game.png");
		play.position(127, 65);
		play.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				WorldSelection.this.worldSelected = true;
				return false;
			}
			
		});
		window.add(play);

		// -- RADIO BUTTONS -- //
		
		// Image pange unselect
		pangeaUnselect = new Image("res/game/menu/world/radio-unselected.png");
		pangeaUnselect.position(113, 158);
		pangeaUnselect.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {

				sushiSelect.hide();
				pangeaSelect.show();
				WorldSelection.this.worldName = "Pangea";
				return false;
			}
			
		});
		window.add(pangeaUnselect);

		// Image sushi unselect
		sushiUnselect = new Image("res/game/menu/world/radio-unselected.png");
		sushiUnselect.position(378, 158);
		sushiUnselect.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {

				sushiSelect.show();
				pangeaSelect.hide();
				WorldSelection.this.worldName = "Sushi";
				
				return false;
			}
			
		});		
		window.add(sushiUnselect);
		
		// Image pange unselect
		pangeaSelect = new Image("res/game/menu/world/radio-selected.png");
		pangeaSelect.position(113, 158);
		pangeaSelect.hide();
		window.add(pangeaSelect);

		// Image sushi unselect
		sushiSelect = new Image("res/game/menu/world/radio-selected.png");
		sushiSelect.position(378, 158);
		sushiSelect.hide();
		window.add(sushiSelect);

		// Add the gui 
		gui.add(window);
		
	}
	
	@Override
	public void run() {
		
		// Show the GUI while the Screen is Opened.
        while (screen.isOpened()) {
        	
        	// Clear the Screen
            screen.clear();
            
            // Redraw the Gui
            gui.draw();

            // Update the Screen
            screen.update();
            
            // If World Has Been Chosen, stop
            if (worldSelected) {
            	break;
            }
                        
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
