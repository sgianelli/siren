package edu.siren.game;

import org.lwjgl.input.Keyboard;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.tile.Tile;
import edu.siren.renderer.Camera;

public class Player extends Actor {
    public Camera camera = null;
    public int lastMovement = 1;
    public boolean follow = false;
    public boolean controllable = true;

    public Player(String config) {
        super(config, null);
    }
    
    public Player() {
    }
    
    public void think() {
    }
    
    @Override
    public void draw() {
    	
    	
        think();
        boolean movement = false;
        
        int lastX = this.x;
        int lastY = this.y;
        
        if (controllable && !world.isPaused()) {        
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
    
            for (Rectangle bounds : world.solids) {
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
        this.x += 0.1f * speed;
    }

    public void moveLeft() {
        this.x -= 0.1f * speed;        
    }

    public void moveDown() {
        this.y -= 0.1f * speed;        
    }

    public void moveUp() {
        this.y += 0.1f * speed;        
    }

    public void bindCamera(Camera camera) {
        this.camera = camera;
    }
}
