package edu.siren.game;

import org.lwjgl.input.Keyboard;

import edu.siren.renderer.Camera;

public class Player extends Actor {
    public Camera camera = null;
    public int lastMovement = 0;
    public boolean follow = false;

    public Player(String config) {
        super(config, null);
    }
    
    public Player() {
    }
    
    @Override
    public void draw() {
        boolean movement = false;
        
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
