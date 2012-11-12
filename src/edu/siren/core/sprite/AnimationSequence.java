package edu.siren.core.sprite;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationSequence {
    public ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();

    /**
     * Constructs an animation sequence suitable for a sequence of frames
     * in a sprite sheet. Though AnimationFrame usually throws an IO-exception
     * it won't in this case since it is not a file path.
     * 
     * This class works by assuming some linear contiguous ordering of your
     * frames in the sprite-sheet. So:
     * 
     *  prefix: link-movement-
     *  from: 1
     *  to: 3
     *  msec: 100
     *  
     *  -> link-movement-1, link-movement-2, link-movement-3
     * 
     * @param prefix The frame-prefix
     * @param from The frame start
     * @param to The frame end
     * @param msec The msec to play this frame
     */
    public AnimationSequence(String prefix, int from, int to, int msec) {        
        for (int i = from; i <= to; i++) {
            try {
                frames.add(new AnimationFrame(prefix + i, msec));
            } catch (IOException e) {
            }
        }            
    }

}
