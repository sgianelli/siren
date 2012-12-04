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
	
	// Create Profile Manager
	private ProfileManager profileManager;
	
	// Profile Input Shit
	private String selectedSprite;
	private Text profileError;
	
	// Sprite Click Images
	private Image linkClick;
	private Image pikachuClick;
	private Image jesusClick;
	private Image diglettClick;
	private Image peachClick;
	
	// Sprite Labels
	private Text linkName;
	private Text pikachuName;
	private Text jesusName;
	private Text diglettName;
	private Text peachName;
	private Text[] spriteNames;
	
	// Register Login Complete
	private boolean registerLoginComplete;
	
	// Profile Logged In
	private Profile profile;
	
	// World Selection
	private WorldSelection worldSelection;
	
	
	public GameLogin(Screen screen) throws IOException {
		
		// Call the Super
		super();
		
		// Save Screen 
		this.screen = screen;
		
		// Create Profile manager
		profileManager = new ProfileManager();
		
		// Create World Selector
		worldSelection = new WorldSelection(screen);
		
		// Create the LIst of Windows
		windows = new ArrayList<Window>();
		
		// Set Screen background Color
		screen.backgroundColor(0.26f, 0.26f, 0.26f, 1.0f);
		
		// Initialize the Gui components
		initializeGui();
		
		// Register Login Complete
		registerLoginComplete = false;
				
	}
	
	public void initializeGui() throws IOException {
		
		// Create the GUI Container
		this.gui = new GuiContainer();
		
		// Create the 2D Perspective and the Shader
        perspective = new Perspective2D();
        shader = new Shader("res/tests/glsl/2d-perspective.vert", 
                            "res/tests/glsl/2d-perspective.frag");
        perspective.bindToShader(shader);
		
		// Set Background Color
		screen.backgroundColor(0.26f, 0.26f, 0.26f, 1.0f);
		
		// Add Error to Screen
		profileError = new Text("");
		profileError.position(20, 210);
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
	
		// Starting x 
		int x = 171;
		int y = 265;
		
		// Create the Window
		Window window = new Window(WindowNames.Menu.toString());
		window.dimensions(screen.width, screen.height);
		windows.add(window);
		
		// Image for prettiness
		Image loginSplash = new Image("res/game/gui/intro.png");
		loginSplash.position(0, 0);
		window.add(loginSplash);
		
		// Create Login
		Text createProfileText = new Text("Create Profile", 2);
		{
			// create Profile Properties
			createProfileText.position(x, y);
			createProfileText.onMouseUp(new ElementEvent(){
				@Override
				public boolean event(Element element) {
					toggleWindow(WindowNames.Register);
					return false;
				}
				
			});
			
			// Add Text To Window
			window.add(createProfileText);
		}
		
		// The OR
		Text orText = new Text("-- OR --", 2);
		{
			orText.position(x+40, y-42);
			window.add(orText);
		}

		// Existing Login
		Text loginText = new Text("Load Profile", 2);
		{
			// create Profile Properties
			loginText.position(x+10, y-84);
			loginText.onMouseUp(new ElementEvent(){
				@Override
				public boolean event(Element element) {
					toggleWindow(WindowNames.Login);
					return false;
				}
				
			});
			
			// Add Text To Window
			window.add(loginText);
		}		
		
		// Start Hidden
		window.disable();
		
		// Add the Window
		gui.add(window);
		
	}
	
	private void buildRegister() throws IOException {
		
		// Initialize positions
		int y = 400;
		
		// Create the Window
		Window window = new Window(WindowNames.Register.toString());
		window.dimensions(screen.width, screen.height);
		windows.add(window);
		
		
		// Image for prettiness
		Image loginSplash = new Image("res/game/gui/login.png");
		loginSplash.position(0, 0);
		window.add(loginSplash);
		
		// Set Title
		Image createProfileTitle = new Image("res/game/gui/create-profile-title.png");
		createProfileTitle.position(0, 387);
		window.add(createProfileTitle);
		
		// Character Name Text
		Text characterText = new Text("Character Name");
		characterText.position(20, y-50);
		characterText.fontScaling(2);
		window.add(characterText);
		
		// Enter Character Name
		TextInput characterName = new TextInput();
		{
			characterName.position(220, y-50);
			characterName.background("res/game/gui/text-input.png");
			characterName.fontScaling(2.0f);
			characterName.maxLength(20);

			characterName.fontColor(1.0f, 1.0f, 1.0f);
			characterName.backgroundColor(1.0f, 0.0f, 0.0f);
			characterName.padding(20.0f, 20.0f);
						
			window.add(characterName);
		}
		
		// Sprites
		sprites = new Sprite[5];
		spriteNames = new Text[5];
		
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
        		new Animation("move-backward", "jesus-left-", 1, 13, 50));
        Sprite pikachu = pikachuspritesheet.createSprite(
        		new Animation("move-backward", "pikachu-left-", 1, 2, 50));        
        Sprite diglett = spritesheet.createSprite(
        		new Animation("move-backward", "diglett-backward-", 1, 2, 50));        
        Sprite peach = spritesheet.createSprite(
        		new Animation("move-backward", "peach-car-backward-", 1, 2, 50));        
		
        // Choose a Sprite
        Text chooseSprite = new Text("Choose Sprite");
        chooseSprite.position(20,y-105);
        chooseSprite.fontScaling(2);
        window.add(chooseSprite);
        
        // Link Name
        linkName = new Text("Link");
        linkName.fontScaling(3);
        linkName.position(210, y-125);
        spriteNames[0] = linkName;
        window.add(linkName);

        // Pikachu Name
        pikachuName = new Text("Pikachu");
        pikachuName.fontScaling(3);
        pikachuName.position(253, y-125);
        spriteNames[1] = pikachuName;
        window.add(pikachuName);
        
        // Jesus Name
        jesusName = new Text("Chesus");
        jesusName.fontScaling(3);
        jesusName.position(323, y-125);
        spriteNames[2] = jesusName;
        window.add(jesusName);

        // Diglett Name
        diglettName = new Text("Diglett");
        diglettName.fontScaling(3);
        diglettName.position(382, y-125);
        spriteNames[3] = diglettName;
        window.add(diglettName);        

        // Peach Name
        peachName = new Text("Peach");
        peachName.fontScaling(3);
        peachName.position(450, y-125);
        spriteNames[4] = peachName;
        window.add(peachName);        
        
        // Sprite Locations
        link.spriteX = 220;
        link.spriteY = y-110;
        pikachu.spriteX = 273;
        pikachu.spriteY = y-110;
        jesus.spriteX = 335;
        jesus.spriteY = y-110;
        diglett.spriteX = 402;
        diglett.spriteY = y-110;
        peach.spriteX = 457;
        peach.spriteY = y-110;

        
        // Sprite Clickers
        pikachuClick = new Image("res/game/gui/sprite-click.png");
        jesusClick = new Image("res/game/gui/sprite-click.png");
        linkClick = new Image("res/game/gui/sprite-click.png");
        diglettClick = new Image("res/game/gui/sprite-click.png");
        peachClick = new Image("res/game/gui/sprite-click.png");

        // Link Click
        linkClick.position(linkName.x(), linkName.y());
        linkClick.priority(10);
        linkClick.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.selectedSprite = "Link";
				toggleSpriteName();
				return false;
			}
        	
        });
        window.add(linkClick);        
        
        // Pikachu Click
        pikachuClick.position(pikachuName.x(), pikachuName.y());
        pikachuClick.priority(10);
        pikachuClick.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.selectedSprite = "Pikachu";
				toggleSpriteName();
				return false;
			}
        	
        });
        window.add(pikachuClick);
                
        // Jesus Click
        jesusClick.position(jesusName.x(), jesusName.y());
        jesusClick.priority(10);
        jesusClick.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.selectedSprite = "Chesus";
				toggleSpriteName();
				return false;
			}
        	
        });
        window.add(jesusClick);
        
        // diglett Click
        diglettClick.position(diglettName.x(), diglettName.y());
        diglettClick.priority(10);
        diglettClick.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.selectedSprite = "Diglett";
				toggleSpriteName();
				return false;
			}
        	
        });
        window.add(diglettClick);        

        // Peach Click
        peachClick.position(peachName.x(), peachName.y());
        peachClick.priority(10);
        peachClick.onMouseUp(new ElementEvent(){

			@Override
			public boolean event(Element element) {
				GameLogin.this.selectedSprite = "Peach";
				toggleSpriteName();
				return false;
			}
        	
        });
        window.add(peachClick);        
        
        
        // Save it...	
        sprites[0] = link;
        sprites[1] = jesus;
        sprites[2] = pikachu;
        sprites[3] = diglett;
        sprites[4] = peach;
        
		// Create Profile Button
		Image createProfile = new Image("res/game/gui/create-profile.png");
		{
		
			createProfile.position(210, y-165);
			createProfile.onMouseUp(new ProfileRegisterEvent(characterName));
			window.add(createProfile);
			
		}
		
		// Create Profile Button
		Image cancelButton = new Image("res/game/gui/cancel.png");
		{
		
			// Set Button Characterstics
			cancelButton.position(330, y-165);
			cancelButton.onMouseUp(new ElementEvent(){
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
		window.disable();
		
		// Add to Gui
		gui.add(window);
		
	}
	
	/**
	 * Build the Screen so the player can log in.
	 * 
	 */
	private void buildLogin() throws IOException {
		
		// Initial positions
		int y = 400;
		
		// Create the Window
		Window window = new Window(WindowNames.Login.toString());
		window.dimensions(screen.width, screen.height);
		windows.add(window);
		
		// Image for prettiness
		Image loginSplash = new Image("res/game/gui/login.png");
		loginSplash.position(0, 0);
		window.add(loginSplash);
		
		// Set Title
		Image loadProfileTitle = new Image("res/game/gui/load-profile-title.png");
		loadProfileTitle.position(0, 387);
		window.add(loadProfileTitle);
		
		// Character Name
		Text characterText = new Text("Character Name");
		characterText.position(20, y-50);
		characterText.fontScaling(2);
		window.add(characterText);
		
		// Enter Character Name
		TextInput characterName = new TextInput();
		{
			characterName.position(220, y-50);
			characterName.background("res/game/gui/text-input.png");
			characterName.fontScaling(3);
			characterName.maxLength(20);

			characterName.fontColor(1.0f, 1.0f, 1.0f);
			characterName.backgroundColor(1.0f, 0.0f, 0.0f);
			characterName.padding(20.0f, 20.0f);
						
			window.add(characterName);
		}
		
		// Load Profile Button
		Image loadProfile = new Image("res/game/gui/load-profile.png");
		{
		
			loadProfile.position(210, y-100);
			loadProfile.onMouseUp(new ProfileLoadEvent(characterName));
			window.add(loadProfile);
			
		}
		
		// Cancel Button
		Image cancelButton = new Image("res/game/gui/cancel.png");
		{
		
			// Set Button Characterstics
			cancelButton.position(330, y-100);
			cancelButton.onMouseUp(new ElementEvent(){

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
		window.disable();
		
		// Add Window to gui
		gui.add(window);
		
	}
	
	private void resetScreens() {
		
		// Reset Errors
		profileError.text("");
		
		// Set Sprite Name Colors
		linkName.fontColor(1.0f, 1.0f, 1.0f);
		pikachuName.fontColor(1.0f, 1.0f, 1.0f);
		jesusName.fontColor(1.0f, 1.0f, 1.0f);
		diglettName.fontColor(1.0f, 1.0f, 1.0f);
		
		// Selected Sprite
		selectedSprite = "";
		
	}
	
	private void toggleWindow(WindowNames windowName) {
		
		// Reset Screenas
		resetScreens();
		
		// Find Window and Show it...
		for (Window window : this.windows) {
			if (window.name().equalsIgnoreCase("Window <" + windowName.toString() + ">")) {
				currentWindow = windowName;
				window.show();
				window.enable();
			} else {
				window.hide();
				window.disable();
			}
			
		}
		
	}
	
	/**
	 * Toggle the Selected Sprite Name
	 * 
	 */
	private void toggleSpriteName() {
		
		// Look through all sprites
		for (Text spriteName : spriteNames) {
			
			// Set Color to Red for selected sprite
			if (spriteName.textState.text.equalsIgnoreCase(this.selectedSprite)) {
				spriteName.fontColor(1.0f, 0.0f, 0.0f);	
			} else {
				spriteName.fontColor(1.0f, 1.0f, 1.0f);
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
            
            // If Profile/Login complete... Leave
            if (registerLoginComplete) {
            	break;
            }
            
        }
        
        // Before we Leave
        profile.setWorld(worldSelection.selectWorld());
        		
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

		private TextInput characterName;
		
		public ProfileRegisterEvent(TextInput characterName) {
			this.characterName = characterName;
		}
		
		@Override
		public boolean event(Element element) {

			// Clear Error Message 
			profileError.text("");
			
			// Validate the Profile
			Profile profile = new Profile();
			
			// Set Character Name
			profile.setName(characterName.text().trim());
			
			// Set Sprite Name
			profile.setSpriteName(selectedSprite.trim());
			
			try {
				
				// Validate hte Profile
				profileManager.validate(profile);
					
				// If No Errors were thrown
				// create the profile
				profileManager.save(profile);
				
				
			} catch(ProfileException pie ) {
			
				// Show the Error message
				profileError.text(pie.getMessage());
				
				// An Error Occurred... So, Let's not continue
				return false;
				
			}
			
			// After Saving profile... Start the game
			registerLoginComplete = true;
			
			// Save the Profile
			GameLogin.this.profile = profile;
			
			// Returning false... why? I don't know... 
			// Seems like a thing to do
			return false;
		
		}
		
	}

	private class ProfileLoadEvent implements ElementEvent {

		private TextInput characterName;
		
		public ProfileLoadEvent(TextInput characterName) {
			this.characterName = characterName;
		}
		
		@Override
		public boolean event(Element element) {

			// Clear Error Message 
			profileError.text("");
			
			// Validate the Profile
			Profile profile = null;
						
			try {
				
				// Get the Profile
				profile = profileManager.get(characterName.text().trim());
				
				// If we don't have a profile, 
				if (profile == null) {
					profileError.text("Could not load profile for " 
							+ characterName.text().trim());
				}	
				
			} catch(ProfileException pie ) {
			
				// Show the Error message
				profileError.text(pie.getMessage());
				
				// An Error Occurred... So, Let's not continue
				return false;
				
			}
			
			// After Saving profile... Start the game
			registerLoginComplete = true;
			
			// Save the Profile
			GameLogin.this.profile = profile;
			
			// Returning false... why? I don't know... 
			// Seems like a thing to do
			return false;
		
		}
		
	}


	public boolean loginComplete() {
		return registerLoginComplete;
	}
	
	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	
	
}
