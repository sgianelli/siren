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
    public enum Action { NONE, MOVE, ATTACK };
    private Action action = Action.NONE;
    private Action nextAction = null;
	private Screen screen;
	private Image banner;
    private Window window;
    private BattleManager battleManager;
	
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
            
            final Image overlayTile = new Image("res/game/gui/attack-tile.png");
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
            
            final Text attack = new Text("attack", 2);
            {
                attack.xy(x, y);
                
                attack.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        attack.text("Select a tile / enemy");
                        attack.x(attack.x() + 16);
                        nextAction = Action.ATTACK;
                        overlayTile.show();
                        return true;
                    }
                });
                        
                y += dy;
                window.add(attack);
            }
            
            final Text move = new Text("move", 2);
            {
                move.xy(x, y);
                move.hoverColor(0.75f, 1.0f, 1.0f);
                y += dy;
                move.onMouseUp(new ElementEvent() {
                    public boolean event(Element element) {
                        move.text("Select a tile");
                        move.x(move.x() + 16);
                        nextAction = Action.MOVE;
                        overlayTile.hide();
                        return true;
                    }
                });
                    
                window.add(move);
            }
            
            final Text defend = new Text("defend", 2);
            {
                defend.xy(x, y);
                defend.hoverColor(0.75f, 1.0f, 1.0f);
                y += dy;
                window.add(defend);
            }
            
            final Text special = new Text("special", 2);
            {
                special.xy(x, y);
                special.hoverColor(0.75f, 0.0f, 0.0f);
                y += dy;
                window.add(special);
            }
            
            Window actionWindow = new Window("Action Window");
            {
                actionWindow.onMouseUp(new ElementEvent() {
                    public boolean event(Element what) {
                        switch (action) {
                        case ATTACK:
                            handleAttack(member, Mouse.getX(), Mouse.getY());
                            break;
                        case MOVE:
                            overlayTile.hide();
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
                gui.add(actionWindow);
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