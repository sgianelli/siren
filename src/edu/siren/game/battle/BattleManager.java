package edu.siren.game.battle;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.gui.BattleScreen;
import edu.siren.gui.Gui;

public class BattleManager {
    public Team red, blue;
    public Team active, other;
    private boolean mouseDown = false;
    public BattleScreen battleScreen = null;
    public World world = null;
    public Layer filler = new Layer("fill");
    public int lastTileR = -1, lastTileC = -1;
    public boolean replaceTile = false;
    
    public BattleManager(World world, Team red, Team blue) throws IOException {
        battleScreen = new BattleScreen(this);
        this.world = world;
        filler.priority = 1000;
        world.addLayer(filler);
        this.red = red;
        this.blue = blue;
        this.active = blue;
        next();
    }
    
    /**
     * Switch to the next team
     */
    public Team next() {
        // Reset the number of moves each player can make for the next
        // frame of the game.
        for (Player player : active.players) {
            player.moves = player.maxMoves;
            player.drawPossibleMoveOverlay = false;
        }
        
        other = active;
        active = (active.equals(red) ? blue : red);
        
        for (Player member : active.players) {
            member.moves = member.maxMoves;
            member.drawPossibleMoveOverlay = true;
        }
        return active;
    }
    
    /**
     * Attempt to select a team member at x, y
     */
    public Player select(Team team, int x, int y) {
        for (Player player : team.players) {
            if (player.moves > 0 && player.getRect().contains(x, y))
                return player;
        }
        return null;
    }
    
    /**
     * Move a player to a grid unit
     */
    public boolean actionMove(Player player, int x, int y) {
        if (player.inMovement || player.moves <= 0) return false;
        
        boolean valid = false;
        for (Player member : active.players) {
            if (member.equals(player)) {
                valid = true;
                break;
            }
        }
        
        if (!valid) return false;
        
        // Figure out the row / col selected
        int r = (int) (x / player.gridWidth);
        int c = (int) (y / player.gridHeight);
        int pr = (int) (player.sprite.spriteX / player.gridWidth);
        int pc = (int) (player.sprite.spriteY / player.gridHeight);
        
        for (Rectangle tile : filler.solids) {
            if (tile.contains(x, y)) {
                return false;
            }
        }
        
        for (Player member : other.players) {
            Rectangle rb = member.getRect().clone();
            rb.x = (rb.x / 32) * 32;
            rb.y = (rb.y / 32) * 32;
            rb.width = 32;
            rb.height = 32;
            if (rb.contains(x, y)) {
                return false;
            }
        }
        
        int dr = Math.abs(r - pr);
        int dy = Math.abs(c - pc);
        if ((dr + dy) > player.moves)
            return false;
        player.moves -= (dr + dy);
        player.moveTo(x, y);
        player.inMovement = true;
        player.possibleMoveOverlay.clear();
        return true;
    }
    
    public boolean actionAttack(Player player, int x, int y) {
        if (player.inMovement || player.moves <= 0) return false;
        
        boolean valid = false;
        for (Player member : active.players) {
            if (member.equals(player)) {
                valid = true;
                break;
            }
        }
        
        if (!valid) return false;
        
        // Figure out the row / col selected
        int r = (int) (x / player.gridWidth);
        int c = (int) (y / player.gridHeight);
        int pr = (int) (player.sprite.spriteX / player.gridWidth);
        int pc = (int) (player.sprite.spriteY / player.gridHeight);
        
        for (Rectangle tile : filler.solids) {
            if (tile.contains(x, y)) {
                return false;
            }
        }
        
        int dr = Math.abs(r - pr);
        int dy = Math.abs(c - pc);
        if ((dr + dy) > player.moves)
            return false;
        
        // Attacks a player
        boolean hitPlayer = false;
        for (Player member : other.players) {
            Rectangle rb = member.getRect().clone();
            rb.x = (rb.x / 32) * 32;
            rb.y = (rb.y / 32) * 32;
            rb.width = 32;
            rb.height = 32;
            int rbr = (int) (rb.x / 32);
            int rbc = (int) (rb.y / 32);
            if (rb.contains(x, y)) {
                member.health -= player.attack;
                if (rbr < pr) {
                    if (rbc > pc) {
                        player.moveTo((r + 1) * 32 + 8, (c - 1) * 32 + 8);
                    } else if (rbc < c) {
                        player.moveTo((r + 1) * 32 + 8, (c + 1) * 32 + 8);
                    } else {
                        player.moveTo((r + 1) * 32 + 8, (c) * 32 + 8);
                    }
                } else if (rbr > pr) {
                    if (rbc > pc) {
                        player.moveTo((r - 1) * 32 + 8, (c - 1) * 32 + 8);
                    } else if (rbc < pc) {
                        player.moveTo((r - 1) * 32 + 8, (c + 1) * 32 + 8);
                    } else {
                        player.moveTo((r - 1) * 32 + 8, (c) * 32 + 8);
                    }
                } else {
                    if (rbc > pc) {
                        player.moveTo((r) * 32 + 8, (c - 1) * 32 + 8);
                    } else if (rbc < c) {
                        player.moveTo((r) * 32 + 8, (c + 1) * 32 + 8);
                    }
                }
                hitPlayer = true;
                break;
            }
        }
        
        if (hitPlayer) {
            player.moves -= (dr + dy);
            player.moveTo((r - 1) * 32 + 8, (c) * 32 + 8);
            player.possibleMoveOverlay.clear();
            return true;
        }
        
        // Attacks a tile
        try {
            if (r == lastTileR && c == lastTileC && replaceTile) {
                player.moves -= (dr + dy);
                player.moveTo((r - 1) * 32 + 8, (c) * 32 + 8);
                player.possibleMoveOverlay.clear();
                Tile tile = new Tile("res/game/gui/black.png", r * 32, c * 32, 32, 32, true);
                tile.solid = true;
                filler.addTile(tile);
                replaceTile = false;
                battleScreen.overlayTile.background("res/game/gui/attack-tile.png");
            } else if ((r != lastTileR || c != lastTileC) && replaceTile) {
                replaceTile = false;
                System.out.println("attack tile fail #2");
                battleScreen.overlayTile.background("res/game/gui/attack-tile.png");
            } else if (!replaceTile) {
                lastTileR = r;
                lastTileC = c;
                replaceTile = true;
                System.out.println("attack tile verify");
                battleScreen.overlayTile.background("res/game/gui/attack-tile-verify.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    public void think() {
        // Get the state of the mouse and see if we are clicking anywhere
        float x = Mouse.getX();
        float y = Mouse.getY();
        boolean click = Mouse.isButtonDown(0);
        
        boolean valid = false;
        for (int i = 0; i < active.players.size(); i++) {
            Player member = active.players.get(i);
            if (member.moves > 0) {
                valid = true;
                break;
            }
            if (member.health <= 0) {
                System.out.println("removing player");
                active.players.remove(i--);
            }
        }
        
        if (active.players.size() <= 0) {
            System.out.println("Game is over");
        }
        
        if (!valid) {
            battleScreen.clear();
            next();
            return;
        }
        
        
        battleScreen.run();
        
        if (!click || mouseDown) {
            if (mouseDown && !click)
                mouseDown = false;
            return;
        }
        
        for (Player member : active.players) {
            if (member.getRect().contains(x, y)) {
                showPossibleActions(member);
                break;
            }
        }
    }

    public void showPossibleActions(Player member) {
        battleScreen.showPossibleActions(member);
    }

    
}
