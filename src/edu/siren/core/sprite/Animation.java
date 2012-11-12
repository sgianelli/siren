package edu.siren.core.sprite;

import java.io.IOException;
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
     * Constructs a new animation from a collection of AnimationFrames.
     * This allows you to individualize each animation with specific frames.
     */
    public Animation(String name, AnimationFrame... frames) {
        this.name = name;
        for (AnimationFrame frame : frames) {
            this.frames.add(frame);
        }
    }

    /**
     * Construct a new animation from a linear-sequence of frames as
     * defined by an AnimationSequence. This allows you to create animations
     * that fit a model "name-n" where n is an integer frame number.
     */
    public Animation(String name, AnimationSequence animationSequence) {
        this.name = name;
        for (AnimationFrame frame : animationSequence.frames) {
            this.frames.add(frame);
        }
    }

    /**
     * Wraps the AnimationSequence to create an inline animation expression.
     * 
     * @param name The name of the animation
     * @param prefix The prefix in the sprite-sheet to use.
     * @param from The frame number to start from
     * @param to The frame number to end on (includes the frame)
     * @param msec The time to run the frame
     * @throws IOException Tends to happen.
     */
    public Animation(String name, String prefix, int from, int to, int msec) 
            throws IOException 
    {
        this(name, new AnimationSequence(prefix, from, to, msec));
    }

    /**
     * Creates an inline animation with a single frame.
     * Useful for "idle" like animations.
     * 
     * @param name The name of the animation
     * @param animation The animation itself in the sprite sheet.
     */
    public Animation(String name, String animation) {
        this.name = name;
        this.frames.add(new AnimationFrame(animation));
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

    public void dimensions(float w, float h) {
        for (AnimationFrame frame : frames) {
            frame.dimensions(w, h);
        }
    }

    public void width(float w) {
        for (AnimationFrame frame : frames) {
            frame.width(w);
        }
    }

    public void height(float h) {
        for (AnimationFrame frame : frames) {
            frame.height(h);
        }
    }

}
