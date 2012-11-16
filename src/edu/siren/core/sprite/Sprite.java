package edu.siren.core.sprite;

import java.util.HashMap;

import edu.siren.core.geom.Rectangle;
import edu.siren.renderer.Drawable;

/**
 * A sprite is a simple drawable surface that is composed of a collection
 * of animations.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Sprite implements Drawable {
    public HashMap<String, Animation> animations = new HashMap<String, Animation>();
    public Animation active = null;
    public int spriteX = 0, spriteY = 0;
    public final Rectangle rectangleZero = new Rectangle(0, 0, 0, 0);
    public Rectangle bounds = new Rectangle(0, 0, 0, 0);

    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    @Override
	public void draw() {
        if (active != null) {
            active.draw(this, spriteX, spriteY);
        }
    }

    public Rectangle getRect() {
        if (active == null) {
            return rectangleZero;
        } else {
            return bounds;
        }
    }

    public void animation(String animationName) {
        if (active == null || !active.name.equals(animationName)) {
            active = animations.get(animationName);
        }
    }
}
