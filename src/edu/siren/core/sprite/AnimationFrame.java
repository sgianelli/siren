package edu.siren.core.sprite;

import java.io.IOException;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Tile;

/**
 * An AnimationFrame is a single frame with an associated sprite and time.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class AnimationFrame extends Tile {
    public double frameTime;
    private float lastX = -1, lastY = -1;

    /**
     * @param sprite The sprite for this frame
     * @param time The time this frame will run for
     * @throws IOException If the sprite is not found
     */
    public AnimationFrame(String sprite, double time) throws IOException {
        super(sprite, 0, 0);
        this.frameTime = time;
    }

    /**
     * Draw the sprite at a given X, Y
     */
    public void draw(float x, float y) {
        if (lastX != x || lastY != y) {
            invalidateCache(x, y);
            lastX = x;
            lastY = y;
        }
        draw();
    }
    
    public void invalidateCache(float x, float y) {
        bounds = new Rectangle(x, y, bounds.width, bounds.height);
        createIndexVertexBuffer(1, 1);
    }

    public void dimensions(float w, float h) {
        bounds.width = w;
        bounds.height = h;
        invalidateCache(bounds.x, bounds.y);
    }

    public void height(float h) {
        bounds.height = h;
        invalidateCache(bounds.x, bounds.y);
    }

    public void width(float w) {
        bounds.height = w;
        invalidateCache(bounds.x, bounds.y);
    }
}
