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

    public AnimationFrame(String frameName, TexturePNG texture, int width, 
                          int height, int x, int y, double msec) 
    {
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
        createIndexVertexBuffer(xbl, ybl, xtl, ytl, xtr, ytr, xbr, ybr);                
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
