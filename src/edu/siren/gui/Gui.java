package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Shader;

public class Gui {
    public Font font;
    public Perspective2D perspective;
    public Shader shader;
    
    public ArrayList<Element> elements;
    
    public Gui() throws IOException {
        perspective = new Perspective2D();
        font = new Font("res/tests/fonts/nostalgia.png", 24);
        shader = new Shader("res/tests/glsl/2d-perspective.vert", 
                            "res/tests/glsl/2d-perspective.frag");
        perspective.bindToShader(shader);
        elements = new ArrayList<Element>();
    }
    
    public void add(Element element) {
        elements.add(element);
    }
    
    public void checkEvents() {
        float x = Mouse.getX();
        float y = Mouse.getY();
        boolean click = Mouse.isButtonDown(0);

        // Go through the elements
        for (Element element : elements) {
            
            // At any time an event can cause the other events to stop
            // propagating, this means that an event will stop altogether
            if (element.checkEvents(x, y, click)) {
                return;
            }
        }
    }
    
    public void draw() {
        shader.use();
        
        for (Element element : elements) {
            element.draw();
        }
        
        checkEvents();
        
        shader.release();
    }
}
