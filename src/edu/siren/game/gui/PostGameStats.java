package edu.siren.game.gui;

import java.io.IOException;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import edu.siren.audio.AudioUtil;
import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;

public class PostGameStats implements Gui {
    Thread successMusic; 
    final GuiContainer gui = new GuiContainer();
    boolean canClose = false;
    
    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    public PostGameStats(BattleManager manager) throws IOException {   
        Window window = new Window("Title");
        Image post = new Image("res/game/gui/battle/post.png");
        {
            window.add(post);
        }
        
        // Text statistics
        int units = manager.red.players.size();
        Text unitText = new Text("100 x " + units + " = " + (100 * units));
        {
            unitText.position(135, 340);
            unitText.hide();
            unitText.onDraw(new ElementEvent() {
                public double dt = -1;
                public boolean ignore = false;
                public boolean event(Element element) {
                    if (dt == -1) dt = getTime();
                    if (ignore) return false;
                    if ((getTime() - dt) > 2000) {
                        AudioUtil.playWav("res/game/sound/post.wav");
                        element.show();
                        ignore = true;
                    }
                    return false;
                }
            });
            window.add(unitText);
        }
        
        int health = 0;
        for (Player member : manager.red.players) {
            health += member.health;
        }
        
        Text healthText = new Text("25 x " + health + " = " + (25 * health));
        {
            healthText.position(135, 285);
            healthText.hide();
            healthText.onDraw(new ElementEvent() {
                public double dt = -1;
                public boolean ignore = false;
                public boolean event(Element element) {
                    if (dt == -1) dt = getTime();
                    if (ignore) return false;
                    if ((getTime() - dt) > 3000) {
                        AudioUtil.playWav("res/game/sound/post.wav");
                        element.show();
                        ignore = true;
                    }
                    return false;
                }
            });
            window.add(healthText);
        }
        
        int killed = manager.blueFull.players.size() - manager.blue.players.size();
        Text killedText = new Text("50 x " + killed + " = " + (50 * killed));
        {
            killedText.position(135, 225);
            killedText.hide();
            killedText.onDraw(new ElementEvent() {
                public double dt = -1;
                public boolean ignore = false;
                public boolean event(Element element) {
                    if (dt == -1) dt = getTime();
                    if (ignore) return false;
                    if ((getTime() - dt) > 4000) {
                        AudioUtil.playWav("res/game/sound/post.wav");
                        element.show();
                        ignore = true;
                    }
                    return false;
                }
            });
            window.add(killedText);
        }
        
        int total = (100 * units) + (25 * health) + (50 * killed);
        manager.expGained = total;
        Text totalText = new Text("" + total);
        {
            totalText.position(135, 170);
            totalText.hide();
            totalText.onDraw(new ElementEvent() {
                public double dt = getTime();
                public boolean ignore = false;
                public boolean event(Element element) {
                    if (dt == -1) dt = getTime();
                    if (ignore) return false;
                    if ((getTime() - dt) > 5000) {
                        AudioUtil.playWav("res/game/sound/post.wav");
                        successMusic = AudioUtil.playBackgroundMusic("res/game/sound/post.ogg");
                        element.show();
                        canClose = true;
                        ignore = true;
                    }
                    return false;
                }
            });
            window.add(totalText);
        }
        
        window.onMouseUp(new ElementEvent() {
            public boolean event(Element element) {
                if (canClose) {
                    successMusic.interrupt();
                    gui.disable();
                }
                return false;
            }
        });
        
        gui.add(window);
    }
    
    @Override
    public boolean running() {
        return gui.enabled();
    }

    // Intro is a special case Gui that asks for a screen instance
    @Override
    public void run() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        gui.draw();
        Display.update();
    }
    
    @Override
    public GuiContainer getContainer() {
        return gui;
    }
}
