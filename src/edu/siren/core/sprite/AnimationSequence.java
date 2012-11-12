package edu.siren.core.sprite;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationSequence {
    public ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();

    public AnimationSequence(String prefix, int from, int to, int msec) 
            throws IOException {
        
        for (int i = from; i <= to; i++) {
            frames.add(new AnimationFrame(prefix + i, msec));
        }            
    }

}
