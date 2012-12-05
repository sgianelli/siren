package edu.siren.game.gui;

import java.io.IOException;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import edu.siren.audio.AudioUtil;
import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.battle.Dice;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;

public class AcquireUnits implements Gui {
    final GuiContainer gui = new GuiContainer();
    boolean canClose = false;
    
    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    public AcquireUnits(BattleManager manager) throws IOException {   
        final Window window = new Window("Title");
        Image post = new Image("res/game/gui/battle/acquire.png");
        {
            window.add(post);
        }
        
        int diceSize = 4;
        int rollSize = 1;
        
        int unitX = 165;
        for (Player player : manager.blueFull.players) {
            Image unit = new Image("res/game/gui/battle/" + player.name.toLowerCase() + ".png");
            unit.position(unitX, 285);
            window.add(unit);
            diceSize *= 2;
            rollSize *= 2;
            unitX += unit.w() + 10;
        }
        
        Text diceText = new Text("" + diceSize);
        {
            diceText.position(110, 245);
            window.add(diceText);
        }
        
        Text rollText = new Text("" + rollSize);
        {
            rollText.position(120, 197);
            window.add(rollText);
        }
        
        int lossExp = manager.expGained - (manager.expGained / diceSize);
        Text lossText = new Text("" + lossExp);
        {
            lossText.position(200, 145);
            window.add(lossText);
        }
        
        Window roll = new Window("roll");
        {
            final boolean success = Dice.roll(diceSize) <= rollSize;
            roll.position(65, 30);
            roll.dimensions(100, 35);
            roll.onMouseUp(new ElementEvent() {
                public boolean event(Element element) {
                    try {
                        Image image = null;
                        if (success) {
                            image = new Image("res/game/gui/battle/success-roll.png");
                        } else {
                            image = new Image("res/game/gui/battle/fail-roll.png");
                        }
                        image.onMouseUp(new ElementEvent() {
                            public boolean event(Element element) {
                                gui.disable();
                                return false;
                            }
                        });
                        window.add(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            window.add(roll);
        }
        
        Window skip = new Window("skip");
        {
            skip.position(180, 30);
            skip.dimensions(100, 35);
            skip.onMouseUp(new ElementEvent() {
                public boolean event(Element element) {
                    gui.disable();
                    return false;
                }
            });
            window.add(skip);
        }
        
        
        
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
