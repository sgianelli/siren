package edu.siren.tests.gui;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.Window;
import edu.siren.gui.Element;
import edu.siren.renderer.Screen;

public class WindowTest {

    public static void main(String[] args) throws IOException, LWJGLException {
        Screen screen = new Screen("Gui Standalone Test", 640, 480);

        Gui gui = new Gui();
        Window basic = new Window("My Window #1");
        {
            basic.positioning(Element.Position.ABSOLUTE);
            basic.position(100, 100);
            basic.dimensions(100, 100);
            basic.background("res/tests/gui/red-window.png");
            
            
            // Handle mouse hover
            basic.onMouseHover(new ElementEvent() {
                public boolean event(Element window) {
                    window.x(window.x() + 0.05f);
                    window.y(window.y() + 0.05f);
                    return false;
                }
            });

            // Handle mouse click
            basic.onMouseDown(new ElementEvent() {
                public boolean event(Element window) {
                    System.out.println(window.name() + ": Down");
                    // Return true if you want the events to stop propagating
                    return false;
                }
            });

            // Handle mouse click
            basic.onMouseUp(new ElementEvent() {
                public boolean event(Element window) {
                    System.out.println(window.name() + ": Up");
                    // Return true if you want the events to stop propagating
                    return false;
                }
            });
            
            // Add a child
            Window child = new Window("Child #1");
            {
                child.positioning(Element.Position.RELATIVE);
                child.position(0, 25);
                child.dimensions(50, 50);
                child.background("res/tests/gui/green-window.png");
                
                child.onMouseDown(new ElementEvent() {
                    public boolean event(Element element) {
                        System.out.println(element.name() + ": Down.");
                        return false;
                    }
                });   

                Window subchild = new Window("Subchild #1");
                {
                    subchild.positioning(Element.Position.RELATIVE);
                    subchild.position(0, 15);
                    subchild.dimensions(25, 25);
                    subchild.background("res/tests/gui/purple-window.png");
                    subchild.onMouseDown(new ElementEvent() {
                        public boolean event(Element element) {
                            System.out.println(element.name() + ": Down.");
                            System.out.println(element.name() + ": No propagation.");
                            return true;
                        }
                    });                            
                    child.add(subchild);
                }

                basic.add(child);
            }
            
            /*
            Image logo = new Image();
            {
                logo.positioning(Element.RELATIVE);
                logo.position(100, 100);
                logo.dimensions(50, 50);
                window.add(logo);
            }
            
            Text text = new Text("Hello, world!");
            {
                text.positioning(Element.RELATIVE);
                text.position(25, 25);
                text.fontScaling(1);                
                window.add(text);
            }
            
            Input input = new TextInput();
            {
                input.positioning(Element.RELATIVE);
                input.position(10, 10);
                input.dimensions(100, 10);
                input.fontScaling(3);
                input.backgroundColor(0.50, 0.50, 0.50);
                
                // Handle a focused element
                input.onFocus(new ElementEvent {
                    public void event(Element input) {
                    }
                });                
                
                // Handle the element losing focus
                input.onBlur(new ElementEvent {
                    public void event(Element input) {
                    }
                });
                
                // Handle an input submitted
                input.onSubmit(new ElementEvent {
                    public void event(Element input) {
                    }
                });
                
                // Handle a key being pressed
                input.onKeyPress(new ElementEvent {
                    public void event(Element input) {
                    }
                });
                
                basic.add(input);
            }
            */
            gui.add(basic);
        }
        
        while (screen.isOpened()) {
            screen.clear();
            gui.draw();
            screen.update();
        }
    }

}
