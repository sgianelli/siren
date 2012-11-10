package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.Sys;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;

public abstract class Element {
    public enum Position { ABSOLUTE, RELATIVE };
    
    class Events {
        public ArrayList<ElementEvent> mouseHover, mouseDown, mouseUp;
        public ArrayList<ElementEvent> mouseEnter, mouseExit;
        public Events() {
            mouseHover = new ArrayList<ElementEvent>();
            mouseDown = new ArrayList<ElementEvent>();
            mouseUp = new ArrayList<ElementEvent>();
            mouseEnter = new ArrayList<ElementEvent>();
            mouseExit = new ArrayList<ElementEvent>();
        }
    };
    
    class State {
        public Position positioning = Position.ABSOLUTE;
        public float x, y, w, h;
        public Animation background;
        private boolean lastClickState = false;
        private boolean lastEntered = false;
        public String name = "Unknown";
    };
    
    public Events events = new Events();
    public State state = new State();
    public Element parent = null;
    public ArrayList<Element> children = new ArrayList<Element>();
    
    /**
     * Add support for adding children to this element.
     */
    public Element add(Element child) {
        if (this.equals(child)) {
            throw new IllegalArgumentException("Circular dependency.");
        }
        child.parent = this;
        this.children.add(child);
        return child;
    }
    
    /**
     * Create a new event to handle when the mouse hovers this element.
     */
    public void onMouseHover(ElementEvent event) {
        events.mouseHover.add(event);
    }

    /**
     * Create an event to handle when the mouse clicks this element.
     */
    public void onMouseDown(ElementEvent event) {
        events.mouseDown.add(event);
    }

    /**
     * Create an event to handle when the mouse clicks this element.
     */
    public void onMouseUp(ElementEvent event) {
        events.mouseUp.add(event);
    }

    /**
     * Create an event to handle when the mouse clicks this element.
     */
    public void onMouseEnter(ElementEvent event) {
        events.mouseEnter.add(event);
    }

    
    /**
     * Create an event to handle when the mouse clicks this element.
     */
    public void onMouseExit(ElementEvent event) {
        events.mouseExit.add(event);
    }

    /**
     * Get the name of the element.
     */
    public String name() {
        return this.state.name;
    }
        
    /**
     * Set the positioning of the element.
     */
    public void positioning(Position position) {
        this.state.positioning = position;
    }
    
    /**
     * Retrieve the positioning of the element.
     */
    public Position positioning() {
        return this.state.positioning;
    }
    
    /**
     * Set the position of the element.
     */
    public void position(float x, float y) {
        xy(x, y);
    }
    
    /**
     * Set the dimension of the element.
     */
    public void dimensions(float w, float h) {
        wh(w, h);
    }
    
    /**
     * Chains up through the parents to figure out its real position.
     */
    public float realX() {
        float x = this.state.x;
        if (this.parent != null && this.state.positioning == Position.RELATIVE) {
            x += this.parent.realX();
        }        
        return x;
    }

    /**
     * Chains up through the parents to figure out its real position.
     */
    public float realY() {
        float y = this.state.y;
        if (this.parent != null && this.state.positioning == Position.RELATIVE) {
            y += this.parent.realY();
        }        
        return y;
    }

    public void x(float x) {
        this.state.x = x;
    }
    
    public float x() {
        return this.state.x;
    }
    
    public void y(float y) {
        this.state.y = y;
    }
    
    public float y() {
        return this.state.y;
    }


    public void w(float w) {
        this.state.w = w;
    }
    
    public float w() {
        return this.state.w;
    }
    
    public void h(float h) {
        this.state.h = h;
    }
    
    public float h() {
        return this.state.h;
    }

    public void xy(float x, float y) {
        this.x(x);
        this.y(y);
    }
    
    public void wh(float w, float h) {
        this.w(w);
        this.h(h);
    }    
    
    public void xywh(float x, float y, float w, float h) {
        xy(x, y);
        wh(w, h);
    }
    
    /**
     * Add support for backgrounds that can be animated.
     */
    public void background(AnimationFrame... frames) {
        this.state.background = new Animation("Background");
        
        for (AnimationFrame frame : frames) {
            this.state.background.addFrame(frame);
        }
    }

    /**
     * Or the case that the background doesn't animate.
     * @throws IOException 
     */
    public void background(String pngFile) throws IOException {
        this.state.background = new Animation("Background");
        this.state.background.addFrame(new AnimationFrame(pngFile, 999999));
    }
    
    /**
     * Set the background color of the element.
     */
    public void backgroundColor(float r, float g, float b) {
    }
    
    /**
     * Handle the drawing of the base element.
     */
    public void draw() {
        float x = this.realX();
        float y = this.realY();
        
        // Only draw the background if it is specified
        if (this.state.background != null) {
            this.state.background.draw(x, y);
        }
        
        // Draw the children
        for (Element child : children) {
            child.draw();
        }
    }

    public boolean checkEvents(float mx, float my, boolean click) {
        
        // Check the children first
        for (Element child : children) {
            if (child.checkEvents(mx, my, click)) {
                state.lastClickState = click;
                System.out.println("Preventing propagation of events");
                return true;
            }
        }

        // Check the current node
        float x = realX(), y = realY();
        float t = y + h(), b = y;
        float l = x, r = x + w();
        
        // If we're not in the bounding box, then just fail
        if (mx <= l || mx >= r || my <= b || my >= t) {
            if (!state.lastEntered)
                return false;
            
            state.lastEntered = false;
            for (ElementEvent event : this.events.mouseExit) {
                if (event.event(this))
                    break;
            }
                
            return false;
        }
                
        // Handle mouse entered
        if (!state.lastEntered) {
            for (ElementEvent event : this.events.mouseEnter) {
                if (event.event(this))
                    break;
            }
        }
        
        state.lastEntered = true;
                    
        // Handle mouse hover
        for (ElementEvent event : this.events.mouseHover) {
            if (event.event(this))
                break;
        }

        // Ensure state propagates
        boolean mouseDown = click && !state.lastClickState;
        boolean mouseUp = !click && state.lastClickState;
        state.lastClickState = click;
        
        // Check state for mouse down
        if (mouseDown) {             
            for (ElementEvent event : this.events.mouseDown) {
                if (event.event(this))
                    break;
            }
            
        // Handle state for mouse release
        } else if (mouseUp) {
            for (ElementEvent event : this.events.mouseUp) {
                if (event.event(this))
                    break;
            }
        }
        
        return false;
    }
    
}