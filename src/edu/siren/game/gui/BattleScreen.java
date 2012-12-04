package edu.siren.game.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Tile;
import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.Gui;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
import edu.siren.renderer.Font;
import edu.siren.renderer.Screen;

public class BattleScreen implements Gui {

	// Game Components
    final private GuiContainer gui = new GuiContainer();
    public enum Action { NONE, MOVE, ATTACK, DEFEND, SPECIAL, RUN, SKIP_TURN, USE_ITEM, HELP };
    private Action action = Action.NONE;
    private Action nextAction = null;
	private Screen screen;
	private Image banner;
    private Window window;
    private BattleManager battleManager;
    public Image overlayTile;
	
	/**
	 * Constructor to initialize the Menu
	 * @param battleManager 
	 * 
	 * @param screen
	 * @throws IOException 
	 */
	public BattleScreen(BattleManager battleManager) throws IOException {
		// Save the Screen
        Font font = gui.font;
        window = new Window("Battle Screen");
        this.battleManager = battleManager;
	}
	
    @Override
	public void run() {
        // Redraw the Gui
        if (nextAction != null) {
            action = nextAction;
            nextAction = null;
            if (action == Action.SKIP_TURN) {
                clear();
                battleManager.next();
                return;
            }
        }
        gui.draw();
	}

	@Override
	public boolean running() {
		return gui.enabled();
	}

	@Override
	public GuiContainer getContainer() {
		return gui;
	}

    public void showPossibleActions(final Player member) {
        try {
            int x = (int) (member.x + 64);
            int y = (int) (member.y);
            int dy = 16;
            
            gui.elements.clear();
            window.children.clear();
            
            overlayTile = new Image("res/game/gui/attack-tile.png");
            {
                overlayTile.hide();
                overlayTile.xywh(0, 0, 32, 32);
                overlayTile.onDraw(new ElementEvent() { 
                    public boolean event(Element element) {
                        int r = Mouse.getX() / 32;
                        int c = Mouse.getY() / 32;
                        overlayTile.xy(r * 32, c * 32);
                        return false;
                    }
                });
                window.add(overlayTile);
            }
            
            final Image battlebar = new Image("res/game/gui/battle/battle-bar.png");
            {
                battlebar.xy(0, 8);
                window.add(battlebar);
            }
            
            // moves
            final Image attackhover = new Image("res/game/gui/battle/attack-hover.png");
            final Image movehover = new Image("res/game/gui/battle/defend-hover.png");
            final Image specialhover = new Image("res/game/gui/battle/special-hover.png");
            final Image runhover = new Image("res/game/gui/battle/run-hover.png");
            final Image skipturnhover = new Image("res/game/gui/battle/skip-turn-hover.png");
            final Image useitemhover = new Image("res/game/gui/battle/use-item-hover.png");
            final Image helphover = new Image("res/game/gui/battle/help-hover.png");
            
            // attackhover
            {
                attackhover.xy(116, 34);
                attackhover.hide();
                attackhover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        attackhover.show();
                        return false;
                    }
                });
                attackhover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        attackhover.hide();
                        return false;
                    }
                });
                attackhover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        attackhover.show();
                        attackhover.disable();
                        movehover.enable();
                        specialhover.enable();
                        runhover.enable();
                        skipturnhover.enable();
                        useitemhover.enable();
                        helphover.enable();
                        nextAction = Action.ATTACK;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(attackhover);
            }
            
            // movehover
            {
                movehover.xy(attackhover.x() + attackhover.w(), 34);
                movehover.hide();
                movehover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        movehover.show();
                        return false;
                    }
                });
                movehover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        movehover.hide();
                        return false;
                    }
                });
                movehover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        movehover.show();
                        attackhover.enable();
                        movehover.disable();
                        specialhover.enable();
                        runhover.enable();
                        skipturnhover.enable();
                        useitemhover.enable();
                        helphover.enable();
                        nextAction = Action.MOVE;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(movehover);
            }
            
            // specialhover
            {
                specialhover.xy(movehover.x() + movehover.w(), 34);
                specialhover.hide();
                specialhover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        specialhover.show();
                        return false;
                    }
                });
                specialhover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        specialhover.hide();
                        return false;
                    }
                });
                specialhover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        specialhover.show();
                        attackhover.enable();
                        movehover.enable();
                        specialhover.disable();
                        runhover.enable();
                        skipturnhover.enable();
                        useitemhover.enable();
                        helphover.enable();
                        nextAction = Action.SPECIAL;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(specialhover);
            }
            
            // runhover
            {
                runhover.xy(specialhover.x() + specialhover.w(), 34);
                runhover.hide();
                runhover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        runhover.show();
                        return false;
                    }
                });
                runhover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        runhover.hide();
                        return false;
                    }
                });
                runhover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        runhover.show();
                        attackhover.enable();
                        movehover.enable();
                        specialhover.enable();
                        runhover.disable();
                        skipturnhover.enable();
                        useitemhover.enable();
                        helphover.enable();
                        nextAction = Action.RUN;
                        overlayTile.show();
                        
                        return true;
                    }
                });
                window.add(runhover);
            }
            
             // skipturnhover
            {
                skipturnhover.xy(125, 11);
                skipturnhover.hide();
                skipturnhover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        skipturnhover.show();
                        return false;
                    }
                });
                skipturnhover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        skipturnhover.hide();
                        return false;
                    }
                });
                skipturnhover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        skipturnhover.show();
                        attackhover.enable();
                        movehover.enable();
                        specialhover.enable();
                        runhover.enable();
                        skipturnhover.disable();
                        useitemhover.enable();
                        helphover.enable();
                        nextAction = Action.SKIP_TURN;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(skipturnhover);
            }            
            
            // useitemhover
            {
                useitemhover.xy(skipturnhover.x() + skipturnhover.w(), 11);
                useitemhover.hide();
                useitemhover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        useitemhover.show();
                        return false;
                    }
                });
                useitemhover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        useitemhover.hide();
                        return false;
                    }
                });
                useitemhover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        runhover.show();
                        attackhover.enable();
                        movehover.enable();
                        specialhover.enable();
                        runhover.enable();
                        skipturnhover.enable();
                        useitemhover.disable();
                        helphover.enable();
                        nextAction = Action.USE_ITEM;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(useitemhover);
            }            
            
            // helphover
            {
                helphover.xy(useitemhover.x() + useitemhover.w(), 11);
                helphover.hide();
                helphover.onMouseEnter(new ElementEvent() {
                    public boolean event(Element element) {
                        helphover.show();
                        return false;
                    }
                });
                helphover.onMouseExit(new ElementEvent() {
                    public boolean event(Element element) {
                        helphover.hide();
                        return false;
                    }
                });
                helphover.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        runhover.show();
                        attackhover.enable();
                        movehover.enable();
                        specialhover.enable();
                        runhover.enable();
                        skipturnhover.enable();
                        useitemhover.enable();
                        helphover.disable();
                        nextAction = Action.HELP;
                        overlayTile.show();
                        return true;
                    }
                });
                window.add(helphover);
            }  
            
            final Image earth = new Image("res/game/gui/battle/earth.png");
            {
                earth.xy(455, 35);
                window.add(earth);
            }
            
            final Image actions = new Image("res/game/gui/battle/actions.png");
            {
                actions.xy(120, 12);
                window.add(actions);
            }
            
            String playertype = member.name.toLowerCase();
            final Image playericon = new Image("res/game/gui/battle/" + playertype + ".png");
            {
                playericon.xy(0, 28);
                window.add(playericon);
            }
                
            Window actionWindow = new Window("Action Window");
            {
                actionWindow.xy(0, 128);
                actionWindow.priority(100);
                actionWindow.onMouseUp(new ElementEvent() {
                    public boolean event(Element what) {
                        switch (action) {
                        case ATTACK:
                            handleAttack(member, Mouse.getX(), Mouse.getY());
                            break;
                        case MOVE:
                            handleMove(member, Mouse.getX(), Mouse.getY());
                            break;
                        default:
                        }
                        return false;
                    }

                    // Handle attacking a unit or a tile
                    private void handleAttack(Player member, int x, int y) {
                        for (Tile tile : member.possibleMoveOverlay) {
                            if (tile.bounds.contains(x, y)) {
                                battleManager.actionAttack(member, x, y);
                                return;
                            }
                        }
                    }

                    // Check if the click tile is an overlay tile, if so
                    // then issue the player to move to the designated tile
                    private void handleMove(Player member, int x, int y) {
                        for (Tile tile : member.possibleMoveOverlay) {
                            if (tile.bounds.contains(x, y)) {
                                battleManager.actionMove(member, x, y);
                                return;
                            }
                        }
                    }
                });
                window.add(actionWindow);
            }
            
            gui.add(window);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        window.children.clear();
        window.show();
        gui.elements.clear();
        action = Action.NONE;
    }
}