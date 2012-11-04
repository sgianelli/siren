package edu.siren.core;

import java.util.ArrayList;

import org.lwjgl.Sys;

public class Animation {
    public String name;
    public ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();
    private double dt = 0;
    private int frame = 0;
    private AnimationFrame currentFrame;

    public Animation(String n) {
        name = n;
    }

    public void addFrame(AnimationFrame animationFrame) {
        frames.add(animationFrame);
        frame = 0;
        currentFrame = frames.get(frame);
    }

    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public void draw(float x, float y) {
        if (dt == 0) {
            dt = getTime();
        } else {
            dt -= getTime();
            if (dt > currentFrame.frameTime) {
                frame = (frame + 1) % frames.size();
                currentFrame = frames.get(frame);
                dt = getTime();
            }
        }
        currentFrame.draw(x, y);
    }
}
