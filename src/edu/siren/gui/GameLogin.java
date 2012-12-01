package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.profile.Profile;
import edu.siren.game.profile.ProfileException;
import edu.siren.game.profile.ProfileManager;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;


/**
 * A Player can create a profile for saving. This class will allow a user to 
 * create a profile and then login.
 * 
 * 
 * @author georgeamaya
 *
 */

public class GameLogin implements Gui {

	// Game Components
	private GuiContainer gui;
	private Screen screen;

	// Windows
	private List<Window> windows;
	
	private enum WindowNames { Login, Register, Menu };
	private WindowNames currentWindow;
	
	private Sprite[] sprites;
	
	private Shader shader;
	private Perspective2D perspective;
	
	private ProfileManager profileManager;
	
	// Profile Input Shit
	private TextInput characterName;
	private String spriteName;
	private Text profileError;
	
	public GameLogin(Screen screen) throws IOException {
		
		// Call the Super
		super();
		
		// Save Screen 
		this.screen = screen;
		
		// Create Profile manager
		profileManager = new ProfileManager();
		
		// Create the LIst of Windows
		windows = new ArrayList<Window>();
		
		// Set Screen background Color
		screen.backgroundColor(0.26f, 0.26f, 0.26f, 1.0f);
		
		// Initialize the Gui components
		initializeGui();
				
	}
	
	public void initializeGui() throws IOException {
		
		// Create the GUI Container
		this.gui = new GuiContainer();
		
        perspective = new Perspective2D();
        shader = new Shader("res/tests/glsl/2d-perspective.vert", 
                            "res/tests/glsl/2d-perspective.frag");
        perspective.bindToShader(shader);
		
		// Set Background Color
		screen.backgroundColor(0.26f, 0.26f, 0.26f, 1.0f);
		
		// Add Error to Screen
		profileError = new Text("");
		profileError.position(20, 10);
		profileError.fontColor(1.0f, 0.0f, 0.0f);
		profileError.fontScaling(2);

		
		// Build the Login Components
		buildMenuLogin();
		
		// Build the Register
		buildRegister();
		
		// Build the Login 
		buildLogin();
		
		// Turn on Menu
		toggleWindow(WindowNames.Menu);
		
	}
	
	private void buildMenuLogin() throws IOException {
	
		// Create the Window
		Window window = new Window(WindowNames.Menu.toString());
		window.dimensions(screen.width, screen.height);
		windows.add(window);
		
		// Image for prettiness
		Image loginSplash = new Image("res/game/gui/login-splash.png");
		loginSplash.position(0, 0);
		window.add(loginSplash);
		
		// Create Login
		Text createProfileText = new Text("Create Profile", 2);
		{
			createProfileText.position(171, 330);
			
			
			createProfileText.onMouseDown(new ElementEvent(){

				@Override
				public boolean event(Element element) {

					toggleWindow(WindowNames.Register);
					
					return false;
				}
				
			});
			window.add(createProfileText);
		}
		
		// The OR
		Text orText = new Text("-- OR --", 2);
		{
			orText.position(210, 288);
			window.add(orText);
		}

		// Existing Login
		Text loginText = new Text("Load Profile", 2);
		{
			loginText.position(181, 246);
			window.add(loginText);
		}
		
		// Start Hidden
		window.hide();
		
		// Add the Window
		gui.add(window);
		
	}
	
	private void buildRegister() throws IOException {
		
		// Create the Window
		Window window = new Window(WindowNames.Register.toString());
		window.dimensions(screen.width, screen.height);
		windows.add(window);
		
		
		// Image for prettiness
		Image loginSplash = new Image("res/game/gui/login-splash.png");
		loginSplash.position(0, 0);
		window.add(loginSplash);
		
		// Set Title
		Text title = new Text("---------   Create a Profile   ---------", 2);
		title.position(15, 375);
		window.add(title);
		
		// Character Name Text
		Text characterText = new Text("Character Name");
		characterText.position(20, 325);
		characterText.fontScaling(2);
		window.add(characterText);
		
		// Enter Character Name
		characterName = new TextInput();
		{
			characterName.position(220, 325);
			characterName.background("res/game/gui/text-input.png");
			characterName.fontScaling(3);
			characterName.maxLength(20);

			characterName.fontColor(1.0f, 1.0f, 1.0f);
			characterName.backgroundColor(1.0f, 0.0f, 0.0f);
			characterName.padding(20.0f, 20.0f);
						
			window.add(characterName);
		}
		
		// Sprites
		sprites = new Sprite[3];
		
		// Get Sprite Sheet
        SpriteSheet spritesheet = SpriteSheet.fromCSS
                ("res/game/sprites/characters/sprites.png",
                "res/game/sprites/characters/sprites.css");
        
        SpriteSheet jesusspritesheet = SpriteSheet.fromCSS
                ("res/game/sprites/characters/jesus.png",
                "res/game/sprites/characters/jesus.css");      
        
        SpriteSheet pikachuspritesheet = SpriteSheet.fromCSS
                ("res/game/sprites/characters/pikachu.png",
                "res/game/sprites/characters/pikachu.css");          
        
		
        // This demonstrates using contiguous animation sequences by using
        // a conventional naming to generate animations
        Sprite link = spritesheet.createSprite(
        		new Animation("move-backward", "link-backward-", 1, 7, 100));
        Sprite jesus = jesusspritesheet.createSprite(
        		new Animation("move-right", "jesus-forward-", 1, 13, 50));
        Sprite pikachu = pikachuspritesheet.createSprite(
        		new Animation("move-right", "pikachu-left-", 1, 2, 50));        
        
		
        // Choose a Sprite
        Text chooseSprite = new Text("Choose Sprite");
        chooseSprite.position(20,270);
        chooseSprite.fontScaling(2);
        window.add(chooseSprite);
        
        // Put The little bastard somewhere
        link.spriteX = 220;
        link.spriteY = 265;
        Text linkName = new Text("Link");
        linkName.fontScaling(3);
        linkName.position(210, 250);
        window.add(linkName);

        // Sprite Clickers
        final Image pikachuClick = new Image("res/game/gui/sprite-click.png");
        final Image jesusClick = new Image("res/game/gui/sprite-click.png");
        
        // Sprite Lables
        final Text pikachuName = new Text("Pikachu");
        final Text jesusName = new Text("Chesus");
        
        // Place the Pikachu
        // Set X and Y
    	pikachu.spriteX = 275;
        pikachu.spriteY = 265;
        
        // Label it
        pikachuName.fontScaling(3);
        pikachuName.position(255, 250);
        
        // Add It
        window.add(pikachuName);
        
        // Sprite Clicker
        pikachuClick.position(255, 250);
        pikachuClick.priority(10);
        pikachuClick.onMouseDown(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.spriteName = "Pikachu";
				pikachuName.fontColor(1.0f, 0.0f, 0.0f);
				jesusName.fontColor(1.0f, 1.0f, 1.0f);
				return false;
			}
        	
        });

        window.add(pikachuClick);
        
        // Place the Jesus
        jesus.spriteX = 335;
        jesus.spriteY = 265;
        
        // Label Jesus
        jesusName.fontScaling(3);
        jesusName.position(325, 250);
        
        // Add Jesus
        window.add(jesusName);
        

        
        jesusClick.position(325, 250);
        jesusClick.priority(10);
        jesusClick.onMouseDown(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.spriteName = "Chesus";
				pikachuName.fontColor(1.0f, 1.0f, 1.0f);
				jesusName.fontColor(1.0f, 0.0f, 0.0f);
				return false;
			}
        	
        });

        window.add(jesusClick);
        
        
        // Save it...	
        sprites[0] = link;
        sprites[1] = jesus;
        sprites[2] = pikachu;
        
		// Create Profile Button
		Image createProfile = new Image("res/game/gui/create-profile.png");
		{
		
			createProfile.position(210, 210);
			createProfile.onMouseDown(new ProfileRegisterEvent());
			window.add(createProfile);
			
		}
		
		// Create Profile Button
		Image cancelButton = new Image("res/game/gui/cancel.png");
		{
		
			// Set Button Characterstics
			cancelButton.position(330, 210);
			cancelButton.onMouseDown(new ElementEvent(){

				@Override
				public boolean event(Element element) {

					toggleWindow(WindowNames.Menu);
					
					return false;
				}
				
			});
			window.add(cancelButton);
			
		}
		
		
		// Add Error Message to Window
		window.add(profileError);
		
		// Hide the Window
		window.hide();
		
		// Add to Gui
		gui.add(window);
		
	}
	
	private void buildLogin() {
		
	}
	
	private void toggleWindow(WindowNames windowName) {
		
		for (Window window : this.windows) {
			if (window.name().equalsIgnoreCase("Window <" + windowName.toString() + ">")) {
				currentWindow = windowName;
				window.show();
			} else {
				window.hide();
			}
			
		}
		
	}
	
	@Override
	public void run() {

		// Show the GUI while the Screen is Opened.
        while (screen.isOpened()) {
        	
        	// Clear the Screen
            screen.clear();
            
            // Redraw the Gui
            gui.draw();
            
            if ( currentWindow.equals(WindowNames.Register) ) {
	            shader.use();            
	            for (Sprite sprite : sprites) {
	            	sprite.draw();
	            }
	            shader.release();
            }
            
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
	
	
	private class ProfileRegisterEvent implements ElementEvent {

		@Override
		public boolean event(Element element) {

			// Clear Error Message 
			profileError.text("");
			
			// Validate the Profile
			Profile profile = new Profile();
			
			// Set Stuff
			profile.setName(characterName.text().trim());
			
			try {
				
				// Validate hte Profile
				profileManager.validate(profile);
				
			} catch(ProfileException pie ) {
			
				// Show the Error message
				profileError.text(pie.getMessage());
				
			}
			
			// Returning false... why? I don't know... 
			// Seems like a thing to do
			return false;
		
		}
		
	}

}