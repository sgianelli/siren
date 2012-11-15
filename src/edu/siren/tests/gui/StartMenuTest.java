package edu.siren.tests.gui;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Screen;

public class StartMenuTest {
    
    public static void main(String[] args) throws LWJGLException, IOException {
        Screen screen = new Screen("Gui Standalone Test", 640, 480);
        
        final GuiContainer gui = new GuiContainer();
        final Window title = new Window("Title");
        final Image background = new Image("res/tests/gui/black.png");
        {
            background.xywh(0, 0, 640, 480);
            background.onMouseUp(new ElementEvent() {
                @Override
				public boolean event(Element element) {
                    gui.disable();
                    return false;
                }
            });
            title.add(background, -1);
        }
        
        final Text logo = new Text("SIREN", 1);
        {
            logo.position(250, 300);
        
            final Text prompt = new Text("click to start", 2);
            {
                prompt.position(-20, -30);
                prompt.positioning(Element.Position.RELATIVE);
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
                logo.add(prompt); 
                
            }
            
            title.add(logo);

        }
        
        gui.add(title);
        
        while (screen.isOpened()) {
            screen.clear();
            gui.draw();
            screen.update();
        }
        
        screen.cleanup();        
    }

}
