package edu.siren.core.geom;

import java.io.Serializable;

/**
 * Defines a simple Rectangle object for tiling, bounds-checking, etc.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Rectangle implements Serializable {
    private static final long serialVersionUID = -4593746585805825787L;
    
    public float x, y;
    public float width, height;

    /**
     * Creates a new rectangle
     */
    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /**
     * @return The left bounds
     */
    public float left() {
        return x;
    }

    /**
     * @return The right bounds
     */
    public float right() {
        return x + width;
    }

    /**
     * @return The top bounds
     */
    public float top() {
        return y;
    }

    /**
     * @return The bottom bounds
     */
    public float bottom() {
        return y + height;
    }

    /**
     * Extends this rectangle with another; e.g. merges
     *
     * @param other The other rectangle to merge with this rectangle.
     */
    public void extend(Rectangle other) {
        x = x > other.x ? other.x : x;
        y = y > other.y ? other.y : y;
        width = width < other.width ? other.width : width;
        height = height < other.height ? other.height : height;
    }
    
    @Override
	public String toString() {
        return "Rect: (" + x + ", " + y + "), (" + width + ", " + height + ")";
    }

    public boolean contains(float mx, float my) {        
        // If we're not in the bounding box, then just fail
        return mx > left() && mx < right() && my < bottom() && my > top();
    }

    public boolean contains(Rectangle rect) {
        return contains(rect.x, rect.y);
    }
    
    public boolean touching(Rectangle other) {                
        boolean ibottom = bottom() < other.top();
        boolean itop = top() > other.bottom();
        boolean ileft = left() > other.right();
        boolean iright = right() < other.left();
        
        return !(ibottom || itop || ileft || iright);
    }
    
    public boolean touchingAny(Rectangle other) {
        float x1 = x, y1 = y;
        float w1 = width, h1 = height;

        float x2 = other.x, y2 = other.y;
        float w2 = other.width, h2 = other.height;
        
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
    
    public Rectangle clone() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle scaled(int scalar) {
        Rectangle scaledRect = this.clone();
        scaledRect.width += scalar;
        scaledRect.height += scalar;
        scaledRect.x -= scalar;
        scaledRect.y -= scalar;
        return scaledRect;        
    }

    public Point asPoint() {
        return new Point((int)x, (int)y);
    }    
}
