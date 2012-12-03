package edu.siren.game;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Font;

public class Player extends Actor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2363703736908492621L;
	public Camera camera = null;
    public boolean follow = false;
    public boolean controllable = true;
    public boolean hadMovement = false;
    public ArrayList<Tile> possibleMoveOverlay = new ArrayList<Tile>();
    public int maxMoves = 4;
    public boolean drawPossibleMoveOverlay = false;
    
    public Player(String config) {
        super(config, null);
    }
    
    public Player() {
        super();
    }
    
    public void think() {
        if (ai != null) {
            ai.think();
        }
    }
    
    @Override
    public void draw() {
        think();
        
        if (!inMovement && possibleMoveOverlay.isEmpty()) {
            createMoveOverlay();
        }
        
        // Draw the status of the player, like health and movements
        if (drawStatus) {
            renderStatus();
        }
        
        if (!controllable) {
            if (snap && drawPossibleMoveOverlay) {
                drawMoveOverlay();
            }
            super.draw();
            return;
        }
        
        boolean movement = false;
        
        float lastX = this.x;
        float lastY = this.y;
        
        if (controllable && (!snap || (snap && !hadMovement))) {        
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                moveUp();
                sprite.animation("move-forward");
                lastMovement = 1;
                movement = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                moveDown();
                sprite.animation("move-backward");
                lastMovement = 2;
                movement = true;
            }
            
            int lastMovementY = lastMovement;
            
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                moveLeft();
                sprite.animation("move-left");
                lastMovement = 3;
                movement = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                moveRight();
                sprite.animation("move-right");
                lastMovement = 4;
                movement = true;
            }
            
            int lastMovementX = lastMovement;
            
            Rectangle o = this.getRect(); o.x = this.x; o.y = this.y;
            Rectangle l = o.clone(); l.x -= 2;
            Rectangle r = o.clone(); r.x += 2;
            Rectangle t = o.clone(); t.y -= 2;
            Rectangle b = o.clone(); b.y += 2;
    
            Rectangle selfRect = this.getRect();
            for (Rectangle bounds : world.solids) {
                if (bounds.equals(selfRect))
                    continue;
                boolean touched = false;
                if (l.touching(bounds)) {
                    touched = true;
                    if (lastMovementX == 3) {
                        this.x = lastX;
                    }
                } else if (r.touching(bounds)) {
                    touched = true;
                    if (lastMovementX == 4) {
                        this.x = lastX;
                    }
                }
                
                if (t.touching(bounds)) {
                    touched = true;
                    if (lastMovementY == 2) {
                        this.y = lastY;
                    }
                } else if (b.touching(bounds)) {
                    touched = true;
                    if (lastMovementY == 1) {
                        this.y = lastY;
                    }
                } 
                
                if (touched) {
                    world.solids.remove(bounds);
                    world.solids.add(0, bounds);
                    break;
                }
            }
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_A) ||
            Keyboard.isKeyDown(Keyboard.KEY_W) ||
            Keyboard.isKeyDown(Keyboard.KEY_S) ||
            Keyboard.isKeyDown(Keyboard.KEY_D)) {
            hadMovement = true;
        } else {
            hadMovement = false;
        }
        
        // Draw the tiles that are available to move to
        if ((snap && hadMovement) || possibleMoveOverlay.isEmpty()) {
            createMoveOverlay();
        }
        
        if (snap) {
            drawMoveOverlay();
        }
        
        if (!movement) {
            switch (lastMovement) {
            case 1:
                sprite.animation("idle-forward");
                break;
            case 2:
                sprite.animation("idle-backward");
                break;
            case 3:
                sprite.animation("idle-left");
                break;
            case 4:
                sprite.animation("idle-right");
                break;
            }
        } else {
            preventCollision.clear();
        }
        
        // Only update the camera if necessary
        if (follow && (sprite.spriteX != this.x || sprite.spriteY != this.y)) {
            camera.setPosition(-this.x, -this.y);
        } 
        
        sprite.spriteX = this.x;
        sprite.spriteY = this.y;
        
        sprite.draw();
    }

    public void moveRight() {
        if (snap) {
            this.x += gridWidth;
        } else {
            this.x += 0.1f * speed;
        }
    }

    public void moveLeft() {
        if (snap) {
            this.x -= gridWidth;
        } else {
            this.x -= 0.1f * speed;        
        }
    }

    public void moveDown() {
        if (snap) {
            this.y -= gridHeight;
        } else {
            this.y -= 0.1f * speed;        
        }
    }

    public void moveUp() {
        if (snap) {
            this.y += gridHeight;
        } else {
            this.y += 0.1f * speed;        
        }
    }

    public void bindCamera(Camera camera) {
        this.camera = camera;
    }
    
    /**
     * Centers a player's position about x, y
     */
    public void setPositionCenter(int x, int y) {
        float xx = x - (this.getRect().width / 2.0f);
        float yy = y + (this.getRect().height / 2.0f);
        this.setPosition(xx, yy);
    }

    @Override
    public void renderStatus() {
        statusFont.print("H:" + health + "/100" + "\nM:" + moves,
            3, 
            sprite.spriteX - sprite.getRect().width / 2.0f - 2.0f, 
            sprite.spriteY + sprite.getRect().height + 8.0f);
    }
    
    public void createMoveOverlay() {
        int row = (int) (this.x / (float) this.gridWidth);
        int col = (int) (this.y / (float) this.gridHeight);
        possibleMoveOverlay.clear();
        
        if (moves <= 0)
            return;
        
        try {
            // Go through each row
            for (int k = 0; k <= moves; k++) {
                // Determine the number of columns to print on each side
                for (int j = 0; j <= 2*k; j++) {
                    possibleMoveOverlay.add(new Tile("res/tests/img/yellow.png",
                            ((row - moves) + k) * this.gridHeight,
                            ((col - k) + j) * this.gridWidth,
                            32, 32, true));
                }
                
                for (int j = 0; j <= 2*k; j++) {
                    possibleMoveOverlay.add(new Tile("res/tests/img/yellow.png",
                            ((row + moves) - k) * this.gridHeight,
                            ((col - k) + j) * this.gridWidth,
                            32, 32, true));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void drawMoveOverlay() {
        for (Tile tile : possibleMoveOverlay) {
                tile.draw();
        }
    }
}
