package edu.siren.game.gui;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.core.tile.World;
import edu.siren.game.worlds.Pangea;
import edu.siren.game.worlds.Sushi;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Window;
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
	private Image rockoUnselect;
	
	private Image pangeaSelect;
	private Image sushiSelect;
	private Image rockoSelect;
	
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
	
	public World selectWorld() 
	        throws IOException, LWJGLException, InstantiationException, 
	        IllegalAccessException, ClassNotFoundException 
    {
		// Run the Selection Screen
		run();
		
		// Create World;
		World world = null;
		if (worldName != null) {
			world = (World) Class.forName("edu.siren.game.worlds." + worldName).newInstance();
		}
        return world;
	}
	
	private void buildTheFuckingGui() throws IOException {
		
		// Create a Window
		window = new Window("World Selection");
		
		// Create Dimensions
		window.dimensions(screen.width, screen.height);

		// Image for
		Image worldsToChoose = new Image("res/game/menu/world/menu.png");
		worldsToChoose.position(0, 0);
		window.add(worldsToChoose);
		
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
		
		Window itemshop = new Window("Item Shop");
		{
		    itemshop.dimensions(100, 100);
		    itemshop.position(410, 5);
		    itemshop.onMouseUp(new ElementEvent() {
		        public boolean event(Element element) {
		            WorldSelection.this.worldSelected = true;
    				WorldSelection.this.worldName = "ItemShop";
		            return false;
		        }
		    });
		    window.add(itemshop);
		}

		// -- RADIO BUTTONS -- //
		
		// Image pange unselect
		pangeaUnselect = new Image("res/game/menu/world/radio-unselected.png");
		pangeaUnselect.position(95, 140);
		pangeaUnselect.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {

				sushiSelect.hide();
				pangeaSelect.show();
				rockoSelect.hide();
				WorldSelection.this.worldName = "Pangea";
				return false;
			}
			
		});
		window.add(pangeaUnselect);

		// Image sushi unselect
		sushiUnselect = new Image("res/game/menu/world/radio-unselected.png");
		sushiUnselect.position(250, 140);
		sushiUnselect.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {

				sushiSelect.show();
				pangeaSelect.hide();
				rockoSelect.hide();
				WorldSelection.this.worldName = "Sushi";
				
				return false;
			}
			
		});		
		window.add(sushiUnselect);
		
		// Image sushi unselect
		rockoUnselect = new Image("res/game/menu/world/radio-unselected.png");
		rockoUnselect.position(410, 140);
		rockoUnselect.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {

				sushiSelect.hide();
				pangeaSelect.hide();
				rockoSelect.show();
				WorldSelection.this.worldName = "Rocko";
				
				return false;
			}
			
		});		
		window.add(rockoUnselect);
		
		// Image pange unselect
		pangeaSelect = new Image("res/game/menu/world/radio-selected.png");
		pangeaSelect.position(95, 140);
		pangeaSelect.hide();
		window.add(pangeaSelect);

		// Image sushi unselect
		sushiSelect = new Image("res/game/menu/world/radio-selected.png");
		sushiSelect.position(250, 140);
		sushiSelect.hide();
		window.add(sushiSelect);
		
		// Image rocko unselect
		rockoSelect = new Image("res/game/menu/world/radio-selected.png");
		rockoSelect.position(410, 140);
		rockoSelect.hide();
		window.add(rockoSelect);

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
