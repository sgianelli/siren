package edu.siren.audio.tests;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import edu.siren.audio.AudioUtil;

public class MidiSoundTest {
    public static void main(String[] args) 
            throws MidiUnavailableException, InvalidMidiDataException, 
                   IOException 
    {
        AudioUtil.playBackgroundMidi("res/tests/sounds/zero.midi");
        System.out.println("Hello, world!");
    }    
}
