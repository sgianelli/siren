package edu.siren.game.gui;

import java.io.IOException;

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
    
    public Title(Screen screen) throws IOException {    
        this.screen = screen;
        final Window title = new Window("Title");
        Image background;
        background = new Image("res/game/gui/intro.png");
        
        {
            background.xywh(0, 0, 640, 480);
            background.onMouseUp(new ElementEvent() {
                public boolean event(Element element) {
                    gui.disable();
                    return false;
                }
            });
            title.add(background);
        }
        
        
        final Text prompt = new Text("click to start", 2);
        {
            prompt.position(180, 180);
            prompt.positioning(Element.Position.ABSOLUTE);
            prompt.onDraw(new ElementEvent() {
                public double dt = Element.getTime();
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
    
    public boolean running() {
        return gui.enabled();
    }

    // Intro is a special case Gui that asks for a screen instance
    public void run() {
        if (screen.nextFrame()) {
            gui.draw();
        } else {
            screen.cleanup();
        }
    }
    
    public GuiContainer getContainer() {
        return gui;
    }

}
