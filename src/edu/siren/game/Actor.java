package edu.siren.game;

import java.io.IOException;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import edu.siren.core.geom.Rectangle;
import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;
import edu.siren.core.tile.Tile;
import edu.siren.game.ai.AI;
import edu.siren.game.entity.Entity;
import edu.siren.game.entity.Interactable;
import edu.siren.renderer.Font;

public abstract class Actor extends Entity implements Interactable {
    public float desiredX = 0;
    public float desiredY = 0;
    public float x = 0;
    public float y = 0;
    
    public int speed = 0;
    public int health = 100;
    public int moves = 2;
    
    public boolean snap;
    public float gridWidth;
    public float gridHeight;
    public boolean drawStatus = false;
    public Font statusFont;
    
    protected Actor() {
        super();
        try { statusFont = new Font("res/tests/fonts/proggy.png"); }
        catch (IOException e) { }
    }

    protected Actor(String config, AI ai) {
        super(config, ai);
        initializeAnimations();
        try { statusFont = new Font("res/tests/fonts/proggy.png"); }
        catch (IOException e) { }
    }

    public void setPosition(float xx, float yy) {
        this.x = xx;
        this.y = yy;
        this.desiredX = xx;
        this.desiredY = yy;
        this.sprite.bounds.x = xx;
        this.sprite.bounds.y = yy;
    }

    @SuppressWarnings("unchecked")
    private boolean initializeAnimations() {
        try {
            speed = json.getInt("speed");
            System.out.println("Player speed: " + speed);
            JSONObject jsonAnims = json.getJSONObject("animations");
            for (String n : (Set<String>) jsonAnims.keySet()) {
                System.out.println(n);
                JSONArray animCollection = jsonAnims.getJSONArray(n);
                Animation animation = new Animation(n);
                for (int i = 0; i < animCollection.length(); i++) {
                    JSONArray sequence = animCollection.getJSONArray(i);
                    if (sequence.length() < 2) {
                        System.err.println("Expected animation sequence");
                        return false;
                    }
                    String texture = sequence.getString(0);
                    int time = sequence.getInt(1);
                    System.out.println("Adding animation: " + texture
                            + ", time: " + time);
                    animation.addFrame(new AnimationFrame(texture, time));
                }
                sprite.animations.put(n, animation);
            }
            sprite.active = sprite.animations.get("idle");
            return true;
        } catch (JSONException e) {
            System.out.println(e);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    abstract public void renderStatus();

    @Override
	public void moveTo(int x, int y) {
        this.desiredX = x;
        this.desiredY = y;
    }

    @Override
	public void draw() {
        boolean movement = false;
        float lastX = this.x;
        float lastY = this.y;
        int lastMovementX = -1;
        int lastMovementY = -1;

        // Go twards X
        if (Math.abs(x - desiredX) > speed) {
            if (x < desiredX) {
                this.x += speed;
                lastMovementX = 3;
            } else if (x > desiredX) {
                this.x -= speed;
                lastMovementX = 4;
            }
        }
        
        if (lastMovementX != -1) {
            movement = true;
        }

        // Go towards Y
        if (Math.abs(y - desiredY) > speed) {
            if (y < desiredY) {
                y += speed;
                lastMovementY = 1;
            } else if (y > desiredY) {
                y -= speed;
                lastMovementY = 2;
            }
        }
        
        if (lastMovementY != -1) {
            movement = true;
        }
        
        Rectangle o = this.getRect(); o.x = this.x; o.y = this.y;
        Rectangle l = o.clone(); l.x -= 2;
        Rectangle r = o.clone(); r.x += 2;
        Rectangle t = o.clone(); t.y -= 2;
        Rectangle b = o.clone(); b.y += 2;

        for (Rectangle bounds : world.solids) {
            if (bounds.x == this.x && bounds.y == this.y)
                continue;
            boolean touched = false;
            if (l.touching(bounds)) {
                touched = true;
                if (lastMovementX == 3) {
                    this.x = lastX;
                    this.desiredX = lastX;
                }
            } else if (r.touching(bounds)) {
                touched = true;
                if (lastMovementX == 4) {
                    this.x = lastX;
                    this.desiredX = lastX;
                }
            }
            
            if (t.touching(bounds)) {
                touched = true;
                if (lastMovementY == 2) {
                    this.y = lastY;
                    this.desiredY = lastY;
                }
            } else if (b.touching(bounds)) {
                touched = true;
                if (lastMovementY == 1) {
                    this.y = lastY;
                    this.desiredY = lastY;
                }
            } 
            
            if (touched) {
                world.solids.remove(bounds);
                world.solids.add(0, bounds);
                break;
            }
        }
        
        if (lastMovementX >= 0) {
            lastMovement = lastMovementX;
        } else if (lastMovementY >= 0) {
            lastMovement = lastMovementY;
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
    
        sprite.spriteX = this.x;
        sprite.spriteY = this.y;

        sprite.draw();
    }

    @Override
    public void interact(Entity other) {
    }
    
    public void snapToGrid(float gridWidth, float gridHeight) {
        this.snap = true;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }
}
