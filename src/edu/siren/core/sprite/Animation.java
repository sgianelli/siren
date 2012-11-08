package edu.siren.core.sprite;

import java.util.ArrayList;

import org.lwjgl.Sys;

import edu.siren.core.geom.Rectangle;

/**
 * An Animation is a collection of AnimationFrames that provides the
 * necessary interpolation between frames for animation sequences.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Animation {
    public String name;
    public ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();
    private double dt = 0;
    private int frame = 0;
    private AnimationFrame currentFrame;

    /**
     * Constructs an Animation object
     *
     * @param name The name of the animation, i.e. "idle"
     */
    public Animation(String name) {
        this.name = name;
    }

    /**
     * Adds a new frame to the animation.
     *
     * @param animationFrame The frame to add to the end of the sequence.
     */
    public void addFrame(AnimationFrame animationFrame) {
        frames.add(animationFrame);
        frame = 0;
        currentFrame = frames.get(frame);
    }

    /**
     * @return Current resolution timer.
     */
    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Draws the underlying sprite at a given X,Y in the world.
     */
    public void draw(float x, float y) {
        if (dt == 0) {
            dt = getTime();
        } else if ((getTime() - dt) > currentFrame.frameTime) {
            frame = (frame + 1) % frames.size();
            currentFrame = frames.get(frame);
            dt = getTime();
        }
        currentFrame.draw(x, y);
    }

    public Rectangle getRect() {
        if (currentFrame == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        
        return currentFrame.bounds;
    }
}
