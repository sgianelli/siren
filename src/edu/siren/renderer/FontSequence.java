package edu.siren.renderer;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.Sys;

import edu.siren.audio.AudioUtil;


public class FontSequence {
    public FontFrame[] frames;
    private FontFrame currentFrame;
    public boolean successive;
    private double dt = 0, dtFrame = 0, dtWait = 0;
    private boolean sucessive, done = false;
    private int frame = 0, frameIndex = 0;
    private Font font;
    
    FontSequence(Font font, boolean successive, FontFrame... fontFrames) {
        this.successive = sucessive;
        this.font = font;
        frames = fontFrames;
        currentFrame = frames[0];
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
    public void draw(int x, int y, int size) {
        if (dt == 0) {
            dt = getTime();
        } else {
            if ((getTime() - dtFrame) > currentFrame.msecTotal) { 
                if (dtWait == 0) {
                    dtWait = getTime();
                }
                if ((getTime() - dtWait) > currentFrame.msecWait) {
                    if (frame + 1 == frames.length) {
                        done = true;
                        return;
                    }
                    frame = (frame + 1) % frames.length;
                    currentFrame = frames[frame];
                    frameIndex = 0;
                    dt = 0;
                    dtWait = 0;
                    dtFrame = 0;
                }
            }
        }
        
        if (dtFrame == 0) {
            dtFrame = getTime();
        } else {
            if (frameIndex >= currentFrame.message.length()) {
                font.print(currentFrame.message, size, x, y);
                return;
            }
            
            if ((getTime() - dtFrame) > currentFrame.msecFrame) {   
                frameIndex++;
                dtFrame = getTime();
                if (currentFrame.wav != null && frameIndex % 2 == 0) {
                    try {
                        AudioUtil.playWav(currentFrame.wav);
                    } catch (LineUnavailableException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
            font.print(currentFrame.message.substring(0, frameIndex), size, x, y);
        }
    }

    public boolean end() {
        return done;
    }

}
