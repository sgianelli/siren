package edu.siren.game;

import org.lwjgl.input.Keyboard;

import edu.siren.renderer.Camera;

public class Player extends Actor {
    Camera camera = null;

    public Player(String config) {
        super(config, null);
    }
    
    @Override
    public void draw() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            moveUp();
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveDown();
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            moveLeft();
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            moveRight();
        }
        
        sprite.spriteX = this.x;
        sprite.spriteY = this.y;
        
        // Basic setup
        camera.setPosition(-this.x, -this.y);
        
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
