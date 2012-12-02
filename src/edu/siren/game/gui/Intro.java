package edu.siren.game.gui;

import java.io.IOException;

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
                new FontFrame("Long ago there was a great kingdom.\nIt was a place of beauty sorrounded\nby lush forests and vast mountains.\nCreatures of many lands lived within the\nKingdom. It was a peaceful and unifying\nland of great power and resilence.", 3500, 2000, beep),
                new FontFrame("However, like the kingdoms before it, a\ndark empire was marching its way to\nconquer the peace and create chaos.\nThe king, aware of the evil, called for\nhis people to spread throughout the land\nin hope to spread weak the evil's force.", 3500, 2000, beep),
                new FontFrame("However, the evil did not spread, but\ninstead grew in numbers and strength.\nThe empire eventually conquered all.\nThese dark times told of slavery in\nthe form of games. Societies grew\nin time and slavery became the norm.", 3500, 2000, beep),
                new FontFrame("The Gods, silent through this all,\ngrew tired and restless of the evil\nand decided to send a hero to help.\n\"Help them,\" the gods told to the hero\n\"Help them conquer the evil empire,\"\n\"Help them conquer and reclaim SIREN\".", 3500, 2000, beep));
        
        Window window = new Window("Intro");
        
        Image background = new Image("res/game/gui/intro.png");
        {
            background.xywh(0, 0, 512, 448);
            window.add(background, -1);
        }
        
        final Text prompt = new Text("click to start", 2);
        {
            prompt.position(180, 220);
            prompt.positioning(Element.Position.ABSOLUTE);
            prompt.onDraw(new ElementEvent() {
                public double dt = Element.getTime();
                @Override
				public boolean event(Element element) {                   
                    if (element.disabled())
                        return false;
                    if ((Element.getTime() - dt) > 500.0f) {
                        element.toggle();
                        dt = Element.getTime();
                    }
                    return false;
                }
            });
            prompt.disable();
            prompt.hide();
        }
            
        window.add(prompt, 10);
        
        // This is a subwindow for drawing the overlays
        Window introTextWindow = new Window("Intro Text");
        {
            final Thread audioThread = AudioUtil.playBackgroundMusic("res/tests/sounds/adagio.ogg", false);
            introTextWindow.onDraw(new ElementEvent() {
                public boolean firstDraw = true;
                @Override
				public boolean event(Element e) {
                    if (firstDraw) {
                        audioThread.start(); 
                        firstDraw = false;
                    }
                    sequence.draw(10, 245, 2);
                    if (sequence.end()) {
                        prompt.enable();
                    }
                    return false;
                }
            });
            
            introTextWindow.onMouseUp(new ElementEvent() {
                @Override
				public boolean event(Element e) {
                    gui.disable();
                    AudioUtil.playWav("res/game/sound/click.wav");
                    audioThread.interrupt();
                    return false;
                }
            });
                    
            window.add(introTextWindow);
        }
        
        gui.add(window);
    }

    // Intro is a special case Gui that asks for a screen instance
    @Override
	public void run() {
        if (screen.nextFrame()) {
            gui.draw();
        } else {
            screen.cleanup();
        }
    }
    
    @Override
	public boolean running() {
        return gui.enabled();
    }
    
    @Override
	public GuiContainer getContainer() {
        return gui;
    }
}
