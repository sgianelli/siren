package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;

public abstract class Element implements Comparable<Element> {
    public enum Position { ABSOLUTE, RELATIVE };
    
    class Events {
        public ArrayList<ElementEvent> mouseHover, mouseDown, mouseUp;
        public ArrayList<ElementEvent> mouseEnter, mouseExit;
        public ArrayList<ElementEvent> dragStart, dragging, dragEnd;
        public Events() {
            mouseHover = new ArrayList<ElementEvent>();
            mouseDown = new ArrayList<ElementEvent>();
            mouseUp = new ArrayList<ElementEvent>();
            mouseEnter = new ArrayList<ElementEvent>();
            mouseExit = new ArrayList<ElementEvent>();
            dragStart = new ArrayList<ElementEvent>();
            dragging = new ArrayList<ElementEvent>();
            dragEnd = new ArrayList<ElementEvent>();
        }
    };
    
    class State {
        public Position positioning = Position.ABSOLUTE;
        public float x, y, w, h;
        public Animation background;
        public boolean lastClickState = false;
        public boolean lastEntered = false;
        public boolean display = true;
        public boolean active = true;
        public String name = "Unknown";
        public int priority = 0;
        public boolean draggable = false;
        public boolean dragging = false;
    };
    
    public Events events = new Events();
    public State state = new State();
    public Element parent = null;
    public List<Element> children = new ArrayList<Element>();
    
    /**
     * Comparator
     */
    public int compareTo(Element other) {
        if (other.priority() > priority()) {
            return -1;
        } else if (other.priority() < priority()) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Add support for adding children to this element.
     */
    public Element add(Element child, int priority) {
        if (this.equals(child)) {
            throw new IllegalArgumentException("Circular dependency.");
        }
        child.parent = this;
        child.state.priority = priority;
        this.children.add(child);
        reprioritize();
        return child;
    }
    
    /**
     * Add an element with the default priority
     */
    public Element add(Element child) {
        add(child, child.state.priority);
        return child;
    }
    
    /**
     * Handles when the element starts to be drag
     */
    public void onDragStart(ElementEvent event) {
        events.dragStart.add(event);
    }

    /**
     * Handles when the element is dragging
     */
    public void onDragging(ElementEvent event) {
        events.dragging.add(event);
    }

    /**
     * Handles when the element stops being dragged
     */
    public void onDragEnd(ElementEvent event) {
        events.dragEnd.add(event);
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
     * Hide the element.
     * A hidden element will not draw, but the events may fire.
     * This cascades to its children.
     */
    public void hide() {
        this.state.display = false;
    }
    
    public boolean hidden() {
        return !this.state.display;
    }
    
    /**
     * Show the element
     * A shown element will draw
     */
    public void show() {
        this.state.display = true;
    }
    
    public boolean shown() {
        return this.state.display;
    }
    
    /**
     * Disable the element
     * A disabled element will still draw, but the events will not fire.
     * This cascades to its children.
     */
    public void disable() {
        this.state.active = false;
    }
    
    public boolean disabled() {
        return !this.state.active;
    }
    
    /**
     * Enable the element
     * An enabled element will fire all its events
     */
    public void enable() {
        this.state.active = true;
    }
        
    public boolean enabled() {
        return this.state.active;
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
        this.state.background.width(w);
    }
    
    public float w() {
        return this.state.w;
    }
    
    public void h(float h) {
        this.state.h = h;
        this.state.background.height(h);
    }
    
    public float h() {
        return this.state.h;
    }

    public void xy(float x, float y) {
        this.x(x);
        this.y(y);
    }
    
    public void wh(float w, float h) {
        this.state.w = w;
        this.state.h = h;
        if (this.state.background != null)
            this.state.background.dimensions(w, h);
    }    
    
    public void xywh(float x, float y, float w, float h) {
        this.state.x = x;
        this.state.y = y;
        this.state.w = w;
        this.state.h = h;
        if (this.state.background != null)
            this.state.background.dimensions(w, h);
    }
    
    /**
     * Add support for backgrounds that can be animated.
     */
    public void background(AnimationFrame... frames) {
        this.state.background = new Animation("Background");
        
        for (AnimationFrame frame : frames) {
            if (this.state.w != 0 && this.state.h != 0) {
                frame.bounds.width = this.state.w;
                frame.bounds.height = this.state.h;
            }
            this.state.background.addFrame(frame);
        }
    }
    
    public void priority(int treePriority) {
        this.state.priority = treePriority;
        if (this.parent != null) {
            this.parent.reprioritize();
        }
    }
    
    public int priority() {
        return this.state.priority;
    }

    private void reprioritize() {
        System.out.println(this.state.name + ": Reprioritizing children");
        Collections.sort(this.children);
        for (Element element : this.children) {
            System.out.println(element);
        }
    }

    /**
     * Or the case that the background doesn't animate.
     * @throws IOException 
     */
    public void background(String pngFile) throws IOException {
        this.background(new AnimationFrame(pngFile, 999999));
    }
    
    /**
     * Set the background color of the element.
     */
    public void backgroundColor(float r, float g, float b) {
    }

    /**
     * A draggable element will move with the mouse on drag
     */
    public void draggable(boolean drag) {
        this.state.draggable = drag;
    }
    
    /**
     * Get draggable state.
     */
    public boolean draggable() {
        return this.state.draggable;
    }

    
    /**
     * Handle the drawing of the base element.
     */
    public void draw() {
        if (hidden())
            return;
        
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

    public boolean checkEvents(float mx, float my, float mdx, float mdy, 
                               boolean click) {
        if (disabled())
            return false;
        
        // Check the children first
        for (Element child : children) {
            if (child.checkEvents(mx, my, mdx, mdy, click)) {
                state.lastClickState = click;
                System.out.println("Preventing propagation of events");
                return true;
            }
        }
        
        boolean inBounds = boundsCheck(mx, my);

        // Dragging causes a bit of oddities.
        // So, we check if we are in bounds of the current element, if not
        // Then handle this stuff.
        if (!inBounds && state.lastEntered) {            
            state.lastEntered = false;
            for (ElementEvent event : this.events.mouseExit) {
                if (event.event(this))
                    break;
            }            
        // If we are in bounds then handle the base cases.
        // These events always fire
        } else if (inBounds) {      
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
        }
                
        // Check drag states
        // We want to run these even if our mouse goes out of bounds
        // because the refresh on the mouse will likely be hire then the
        // sync rate of the game.
        if (draggable()) {
            if (inBounds && !state.dragging && click && !state.lastClickState) {
                state.dragging = true;
                for (ElementEvent event : this.events.dragStart) {
                    if (event.event(this))
                        break;
                }
            } else if (click && state.dragging) {
                state.x += mdx;
                state.y += mdy;                
                for (ElementEvent event : this.events.dragging) {
                    if (event.event(this))
                        break;
                }
            } else if (state.dragging && !click && state.lastClickState) {
                state.dragging = false;
                for (ElementEvent event : this.events.dragEnd) {
                    if (event.event(this))
                        break;
                }
            }            
        }
        
        state.lastClickState = click;
        
        // Now, all of our base cases are covered. If we were originally
        // not in bounds, then ditch out.
        if (!inBounds) {
            return false;
        }

        // Ensure state propagates
        boolean mouseDown = click && !state.lastClickState;
        boolean mouseUp = !click && state.lastClickState;
        
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

    public boolean boundsCheck(float mx, float my) {
        // Check the current node
        float x = realX(), y = realY();
        float t = y + h(), b = y;
        float l = x, r = x + w();
        
        // If we're not in the bounding box, then just fail
        return mx > l && mx < r && my > b && my < t;
    }
    
    public String toString() {
        return "<" + this.state.name + ": (" + this.x() + "|" 
                   + this.realX() + ", " + this.y() + "|" + this.realY()
                   + "), Priority: " + this.priority() + ">";
    }

    public boolean touching(Element other) {
        float x1 = realX(), y1 = realY();
        float w1 = w(), h1 = h();

        float x2 = other.realX(), y2 = other.realY();
        float w2 = other.w(), h2 = other.h();
        
        // Create bounding box
        float xtl1 = x1, ytl1 = y1 + h1;
        float xbl1 = x1, ybl1 = y1;
        float xtr1 = x1 + w1, ytr1 = y1 + h1;
        float xbr1 = x1 + w1, ybr1 = y1;

        // Create bounding box
        float xtl2 = x2, ytl2 = y2 + h2;
        float xbl2 = x2, ybl2 = y2;
        float xtr2 = x2 + w2, ytr2 = y2 + h2;
        float xbr2 = x2 + w2, ybr2 = y2;

        // Check if TL is touching
        if (xtl1 >= xtl2 && xtl1 <= xtr2 &&
            ytl1 >= ybl2 && ytl1 <= ytl2)
            return true;
        
        // Check if TR is touching
        if (xtr1 <= xtr2 && xtr1 >= xtl2 &&
            ytr1 <= ytr2 && ytr1 >= ybr2)
            return true;

        // Check if BL is touching
        if (xbl1 >= xbl2 && xbl1 <= xbr2 &&
            ybl1 >= ybl2 && ybl1 <= ytl2)
            return true;
        
        // Check if BR is touching
        if (xbr1 <= xbr2 && xbr1 >= xbl2 &&
            ybr1 <= ybr2 && ybr1 >= ytr2)
            return true;

        // Check if TL is touching
        if (xtl1 >= xtl2 && xtl1 <= xtr2 &&
            ytl1 >= ybl2 && ytl1 <= ytl2)
            return true;
        
        // Check if TR is touching
        if (xtr1 <= xtr2 && xtr1 >= xtl2 &&
            ytr1 <= ytr2 && ytr1 >= ybr2)
            return true;

        // Check if BL is touching
        if (xbl1 >= xbl2 && xbl1 <= xbr2 &&
            ybl1 >= ybl2 && ybl1 <= ytl2)
            return true;
        
        // Check if BR is touching (width bounds)
        if (xbr1 >= xbl2 && xbl1 <= xbl2 &&
            ybr1 <= ybr2 && ybr1 >= ytr2)
            return true;

        // Check if TL is touching (width bounds)
        if (xtr1 >= xtl2 && xtl1 <= xtl2 &&
            ytl1 >= ybl2 && ytl1 <= ytl2)
            return true;
        
        // Check if TR is touching (width bounds)
        if (xtr1 >= xtr2 && xtl1 <= xtl2 &&
            ytr1 <= ytr2 && ytr1 >= ybr2)
            return true;

        // Check if BL is touching (width bounds)
        if (xbl1 <= xbl2 && xbr1 >= xbr2 &&
            ybl1 >= ybl2 && ybl1 <= ytl2)
            return true;        
        
        return false;
    }                  
    
}