package edu.siren.tests.gui;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.gui.ElementEvent;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.TextInput;
import edu.siren.gui.Window;
import edu.siren.gui.Element;
import edu.siren.renderer.Screen;

public class WindowTest {

    public static void main(String[] args) throws IOException, LWJGLException {
        Screen screen = new Screen("Gui Standalone Test", 640, 480);

        GuiContainer gui = new GuiContainer();
        Window basic = new Window("My Window #1");
        {
            basic.positioning(Element.Position.ABSOLUTE);
            basic.position(100, 100);
            basic.dimensions(100, 100);
            basic.background("res/tests/gui/red-window.png");
            
            
            // Handle mouse hover
            basic.onMouseHover(new ElementEvent() {
                @Override
				public boolean event(Element window) {
                    window.x(window.x() + 0.05f);
                    window.y(window.y() + 0.05f);
                    return false;
                }
            });

            // Handle mouse click
            basic.onMouseDown(new ElementEvent() {
                @Override
				public boolean event(Element window) {
                    System.out.println(window.name() + ": Down");
                    // Return true if you want the events to stop propagating
                    return false;
                }
            });

            // Handle mouse click
            basic.onMouseUp(new ElementEvent() {
                @Override
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
                    @Override
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
                        @Override
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
            
            Text text = new Text("Hello, world!");
            {
                text.positioning(Element.Position.RELATIVE);
                text.position(25, 25);
                text.fontScaling(1);  
                text.onMouseEnter(new ElementEvent() {
                    @Override
					public boolean event(Element element) {
                        System.out.println("Entered text area");
                        return false;
                    }
                });
                basic.add(text);
            }
            Text textBg = new Text("Click me to increase\nmy rendering priority.\nCurrently: 0");
            {
                textBg.positioning(Element.Position.ABSOLUTE);
                textBg.position(40, 400);
                textBg.background("res/tests/gui/black.png");
                textBg.fontScaling(1);  
                textBg.padding(20.0f, 20.0f);
                textBg.onMouseUp(new ElementEvent() {
                    @Override
					public boolean event(Element element) {
                        element.priority(element.priority() + 1);
                        Text t = (Text) element;
                        t.text("Click me to increase\nmy rendering priority.\nCurrently: " + element.priority());
                        return false;
                    }
                });
               basic.add(textBg);
            }
            
            final Image logo = new Image("res/tests/gui/picture.png", "A Picture");
            {
                logo.positioning(Element.Position.RELATIVE);
                logo.position(100, 200);
                logo.priority(1);
                logo.draggable(true);
                basic.add(logo);
            }


            Text dragme = new Text("Drag me (300, 25) !");
            {
                dragme.positioning(Element.Position.RELATIVE);
                dragme.position(300, 25);
                dragme.fontScaling(1);  
                dragme.draggable(true);
                dragme.onDragging(new ElementEvent() {
                    @Override
					public boolean event(Element element) {
                        Text t = (Text) element;
                        if (element.touching(logo)) {
                            t.text("I don't like this spot.");
                        } else {
                            t.text("Drag me (" + element.x() + ", " + element.y() + ") !");
                        }                            
                            
                        return false;
                    }
                });
                basic.add(dragme);
            }

            TextInput input = new TextInput();
            {
                input.positioning(Element.Position.ABSOLUTE);
                input.position(10, 40);
                input.fontScaling(3);
                input.maxLength(10);
                input.background("res/tests/img/text-input.png");
                input.fontColor(0.0f, 0.0f, 0.0f);
                input.padding(20.0f, 20.0f);
                
                // Handle a focused element
                input.onMouseEnter(new ElementEvent() {
                    public boolean event(Element input) {
                        return false;
                    }
                });                
                
                // Handle the element losing focus
                input.onMouseExit(new ElementEvent() {
                    public boolean event(Element input) {
                        return false;
                    }
                });
                
                // Handle an input submitted
                input.onSubmit(new ElementEvent() {
                    public boolean event(Element e) {
                        TextInput input = (TextInput) e;
                        System.out.println(input.text());
                        return false;
                    }
                });
                
                // Handle a key being pressed
                input.onKeyPress(new ElementEvent() {
                    public boolean event(Element input) {
                        return false;
                    }
                });
                
                basic.add(input);
            }
            
            gui.add(basic);
        }
        
        while (screen.isOpened()) {
            screen.clear();
            gui.draw();
            screen.update();
        }
        
        screen.cleanup();
    }

}
