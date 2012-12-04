package edu.siren.game.gui;

import java.io.IOException;

import edu.siren.audio.AudioUtil;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class Title implements Gui {
	
    final GuiContainer gui = new GuiContainer();
    
    Screen screen;
    
    private boolean startGame;
    
    public Title(Screen screen) throws IOException {   
    	
    	// Set the Screen
        this.screen = screen;
        
        // create window for the Title 
        Window title = new Window("Title");
        
        
        startGame = false;
        
        // Java throwers
        final Image javaThrowers = new Image("res/game/gui/java-thrower.png");
        final Image background = new Image("res/game/gui/logo-full.png");
        final Text prompt = new Text("click to start", 2);
        
        // Java thrower
        {
            AudioUtil.playWav("res/game/gui/sound/java-thrower.wav");
            javaThrowers.xywh(0, 0, 512, 448);
            javaThrowers.onMouseUp(new ElementEvent() {
                public boolean event(Element element) {
                    background.enable();
                    background.show();
                    javaThrowers.disable();
                    javaThrowers.hide();
                    prompt.enable();
                    return true;
                }
            });
            title.add(javaThrowers, 100);
        }
                
        
        // Load the Image Background
        {
            background.hide();
            background.disable();
            background.xywh(0, 0, 512, 448);
            background.onMouseUp(new ElementEvent() {
                @Override
				public boolean event(Element element) {
                    gui.disable();
                    startGame = true;
                    return false;
                }
            });
            title.add(background);
        }
        
        
        // prompt click
        {
            prompt.hide();
            prompt.disable();
            prompt.position(180, 190);
            prompt.positioning(Element.Position.ABSOLUTE);
            prompt.onDraw(new ElementEvent() {
                public double dt = Element.getTime();
                @Override
				public boolean event(Element element) {                   
                    if ((Element.getTime() - dt) > 500.0f) {
                        element.toggle();
                        dt = Element.getTime();
                    }
                    return false;
                }
            });
        }
            
         title.add(prompt, 10);
        
        gui.add(title);
    }
    
    @Override
	public boolean running() {
        return gui.enabled();
    }

    // Intro is a special case Gui that asks for a screen instance
    @Override
	public void run() {
        if (screen.nextFrame()) {
            gui.draw();
        } else {
            screen.cleanup();
        }
    }
    
    public boolean show() {
    	
        
        while (screen.isOpened()) {
        	
            screen.clear();
            gui.draw();
            screen.update();
            
            if (startGame) {
            	break;
            }
            
        }    	
    	
    	return startGame;
    	
    	
    }
    
    @Override
	public GuiContainer getContainer() {
        return gui;
    }

}
