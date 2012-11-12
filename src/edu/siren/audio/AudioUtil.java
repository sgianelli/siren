package edu.siren.audio;

import org.newdawn.easyogg.OggClip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.midi.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioUtil { 
    static public Map<String, Clip> clips = new ConcurrentHashMap<String, Clip>();
    
    static public void playMidi(final String midiFile) 
            throws MidiUnavailableException, InvalidMidiDataException, 
                   IOException 
    {
        Sequencer sequencer = MidiSystem.getSequencer();
        if (sequencer == null)
            throw new MidiUnavailableException();
        sequencer.open();
        FileInputStream is = new FileInputStream(midiFile);
        Sequence mySeq = MidiSystem.getSequence(is);
        sequencer.setSequence(mySeq);
        sequencer.start();
    }

    static public void playBackgroundMidi(final String midiFile) 
    {
        class Inplace extends Thread {
            public void run() {
                try {
                    playMidi(midiFile);
                } catch (Exception e) {
                }
            }
        }        
        Inplace t = new Inplace();
        t.start();
    }
    
    static public void playWav(String filename) {
        try {
            Clip clip = clips.get(filename);
            if (clip == null) {
                AudioInputStream stream;
                stream = AudioSystem.getAudioInputStream(new File(filename));
                clip = AudioSystem.getClip();
                clip.open(stream);
                clips.put(filename, clip);
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    static public void playMusic(final String filename) throws IOException {
        OggClip clip = new OggClip(filename);
        clip.play();
    }
        
    static public Thread playBackgroundMusic(final String ogg, boolean play) 
    {
        class Inplace extends Thread {
            public void run() {
                try {
                    OggClip clip = new OggClip(ogg);
                    Thread clipThread = clip.play();
                    while (!this.isInterrupted());
                    System.out.println("Interrupted!");
                    clip.setGain(0.0f);
                    clip.stop();
                    clipThread.interrupt();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }        
        Inplace t = new Inplace();
        if (play) {
            t.start();
        }
        return t;
    }
    
    static public void playBackgroundWav(final String wavFile) 
    {
        class Inplace extends Thread {
            public void run() {
                try {
                    playWav(wavFile);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }        
        Inplace t = new Inplace();
        t.start();
    }

    public static Thread playBackgroundMusic(String string) {
        return playBackgroundMusic(string, true);
    }
}
