package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Mouse;

import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Shader;

public class GuiContainer extends Element {
    public Font font;
    public Perspective2D perspective;
    public Shader shader;
    
    public List <Element> elements;
    
    public GuiContainer() throws IOException {
        perspective = new Perspective2D();
        font = new Font("res/tests/fonts/proggy.png");
        shader = new Shader("res/tests/glsl/2d-perspective.vert", 
                            "res/tests/glsl/2d-perspective.frag");
        perspective.bindToShader(shader);
        elements = new ArrayList<Element>();
    }
    
    @Override
	public Element add(Element element, int priority) {
        element.priority(priority);
        element.parent = this;
        elements.add(element);
        Collections.sort(elements);
        return element;
    }
    
    @Override
	public Element add(Element element) {
        add(element, 0);
        return element;
    }
    
    public void checkEvents() {
        float x = Mouse.getX();
        float y = Mouse.getY();
        float dx = Mouse.getDX();
        float dy = Mouse.getDY();
        boolean click = Mouse.isButtonDown(0);

        // Go through the elements
        for (Element element : elements) {
                    	
            // At any time an event can cause the other events to stop
            // propagating, this means that an event will stop altogether
            if (element.checkEvents(x, y, dx, dy, click)) {
                return;
            }
        }
    }
    
    @Override
	public void draw() {
        if (disabled())
            return;
        
        shader.use();
        
        // Draw the children
        for (Element child : elements) {
            boolean noDraw = false;
            
            for (ElementEvent event : child.events.draw) {
                noDraw |= event.event(child);
                if (noDraw)
                    break;
            }
            
            if (noDraw)
                continue;
            
            child.draw();
        }
        
        checkEvents();
        
        shader.release();
    }

}
