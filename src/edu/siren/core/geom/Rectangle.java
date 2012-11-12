package edu.siren.core.geom;

/**
 * Defines a simple Rectangle object for tiling, bounds-checking, etc.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Rectangle {
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
    
    public String toString() {
        return "Rect: (" + x + ", " + y + "), (" + width + ", " + height + ")";
    }
}
