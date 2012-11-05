package edu.siren.core;

import java.util.HashMap;

import edu.siren.renderer.Drawable;

/**
 * A sprite is a simple drawable surface.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Sprite implements Drawable {
    public HashMap<String, Animation> animations = new HashMap<String, Animation>();
    public Animation active = null;
    public int spriteX = 0, spriteY = 0;

    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    public void draw() {
        if (active != null) {
            active.draw(spriteX, spriteY);
        }
    }
}
