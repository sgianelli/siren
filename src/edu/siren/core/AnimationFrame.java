package edu.siren.core;

import java.io.IOException;

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
            bounds = new Rectangle(x, y, bounds.width, bounds.height);
            createIndexVertexBuffer(1, 1);
            lastX = x;
            lastY = y;
        }
        draw();
    }
}
