package edu.siren.game.gui;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.siren.audio.AudioUtil;
import edu.siren.gui.*;
import edu.siren.renderer.*;

public class Intro implements Gui{
    final GuiContainer gui = new GuiContainer();
    Screen screen;
    
    public Intro(Screen screen) throws IOException {
        this.screen = screen;
        
        final String beep = "res/game/sound/text-blip.wav";
        Font font = gui.font;
        
        final FontSequence sequence = font.printFrames(
                false,
                new FontFrame("Long ago, in the beautiful\nkingdom of Siren surrounded\nby mountains and forests...", 2500, 1000, beep),
                new FontFrame("Legends told of an omnipotent\nand omniscent Golden Power\nthat resided in a hidden land.", 2500, 1000, beep),
                new FontFrame("Many creatures sought this\nGolden Power and failed.", 2000, 1000, beep),
                new FontFrame("The Gods, aware of their\nmistake separated the world\ninto pieces.", 2300, 1000, beep),
                new FontFrame("As generations carried through\nthe years the creatures forgot\nof their original kingdom of Siren.", 2800, 1000, beep),
                new FontFrame("As the years grew, so did\na darkening and evil force.", 2000, 1000, beep),
                new FontFrame("The Gods weakened by this force\nopened up a world for help.", 2100, 1000, beep),
                new FontFrame("\"Help us!\", they cried.", 1500, 500, beep),
                new FontFrame("\"Help us and fulfill your destiny.\"", 1700, 1000, beep),
                new FontFrame("          SIREN", 500, 5000));
        
        Window window = new Window("Intro");
        
        Image background = new Image("res/game/gui/intro.png");
        {
            window.add(background, -1);
        }
        
        // This is a subwindow for drawing the overlays
        Window introTextWindow = new Window("Intro Text");
        {
            //final Thread audioThread = AudioUtil.playBackgroundMusic("res/tests/sounds/adagio.ogg", false);
            introTextWindow.onDraw(new ElementEvent() {
                public boolean firstDraw = true;
                public boolean event(Element e) {
                    if (firstDraw) {
                        //audioThread.start(); 
                        firstDraw = false;
                    }
                    sequence.draw(150, 260, 2);
                    if (sequence.end()) {
                        gui.disable();
                        //audioThread.interrupt();
                    }
                    return false;
                }
            });
            
            introTextWindow.onMouseUp(new ElementEvent() {
                public boolean event(Element e) {
                    gui.disable();
                    AudioUtil.playWav("res/game/sound/click.wav");
                    
                    //audioThread.interrupt();
                    return false;
                }
            });
                    
            window.add(introTextWindow);
        }
        
        gui.add(window);
    }

    // Intro is a special case Gui that asks for a screen instance
    public void run() {
        if (screen.nextFrame()) {
            gui.draw();
        } else {
            screen.cleanup();
        }
    }
    
    public boolean running() {
        return gui.enabled();
    }
    
    public GuiContainer getContainer() {
        return gui;
    }
}
