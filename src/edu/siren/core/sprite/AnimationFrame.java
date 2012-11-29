package edu.siren.core.sprite;

import java.io.File;
import java.io.IOException;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Tile;
import edu.siren.renderer.TexturePNG;

/**
 * An AnimationFrame is a single frame with an associated sprite and time.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class AnimationFrame extends Tile {
    public double frameTime;
    private float lastX = -1, lastY = -1;
    private float xbl, ybl, xtl, ytl, xtr, ytr, xbr, ybr;
    public String frameName;

    /**
     * @param sprite The sprite for this frame
     * @param time The time this frame will run for
     * @throws IOException If the sprite is not found
     */
    public AnimationFrame(String sprite, double time) throws IOException {
        this.solid = true;
        File file = new File(sprite);
        if (file.exists()) {
            xbl = xtl = 0;
            ybl = ybr = 0;
            xbr = xtr = 1;
            ytl = ytr = 1;            
            load(sprite, 0, 0);
        }
        this.frameName = sprite;
        this.frameTime = time;
    }

    /**
     * Creates a new AnimationFrame given by a frame name, texture, and so on.
     * This allows you to generate the lookup in the sprite-sheet as well.
     * 
     * @param frameName What to call this frame
     * @param texture The PNG reference to set for this frame
     * @param width The width of the frame
     * @param height The height of the frame
     * @param x The x-offset into the sprite sheet
     * @param y The y-offset into the sprite sheet
     * @param msec The time to play this frame
     */
    public AnimationFrame(String frameName, TexturePNG texture, int width, 
                          int height, int x, int y, double msec) 
    {
        this.solid = true;
        
        // Store the basics
        this.texture = texture;
        this.frameName = frameName;
        this.frameTime = msec;
                
        // Define quick lookups
        float w = texture.width;
        float h = texture.height;
        float xw = width;
        float yh = height;

        bounds = new Rectangle(x, y, xw, yh);

        // Define the texture coordinates
        xbl = x / w;        ybl = y / h;
        xtl = x / w;        ytl = (y + yh) / h;
        xtr = (x + xw) / w; ytr = (y + yh) / h;
        xbr = (x + xw) / w; ybr = y / h;
                
        // Create the new IVB with the given ST
        createIndexVertexBuffer(xbl, ybl, xtl, ytl, xtr, ytr, xbr, ybr);                
    }

    /**
     * Create a single animation frame with a default "indefinite" lifespan.
     * @param animationName The animation name (sprite-sheet reference)
     */
    public AnimationFrame(String animationName) {
        this.frameName = animationName;
        this.frameTime = 999999;
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
    
    /**
     * Invaoidate the cache and create a new IVB at (x, y)
     * @param x The x-coordinate to draw
     * @param y The y-coordinate to draw
     */
    public void invalidateCache(float x, float y) {
        bounds = new Rectangle(x, y, bounds.width, bounds.height);
        createIndexVertexBuffer(xbl, ybl, xtl, ytl, xtr, ytr, xbr, ybr);                
    }

    /**
     * Set the bounding box.
     * 
     * @param w Width of the BBOX
     * @param h Height of the BBOX
     */
    public void dimensions(float w, float h) {
        bounds.width = w;
        bounds.height = h;
        invalidateCache(bounds.x, bounds.y);
    }

    /**
     * Sets the height of the BBOX.
     * 
     * @param h
     */
    public void height(float h) {
        bounds.height = h;
        invalidateCache(bounds.x, bounds.y);
    }

    /**
     * Sets the width of the BBOX
     * @param w
     */
    public void width(float w) {
        bounds.height = w;
        invalidateCache(bounds.x, bounds.y);
    }
}
