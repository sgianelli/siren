package edu.siren.core;

import java.util.HashMap;

public class Sprite {
    public HashMap<String, Animation> animations = new HashMap<String, Animation>();
    public Animation active = null;
    public int spriteX = 0, spriteY = 0;

    public void draw() {
        if (active != null) {
            active.draw(spriteX, spriteY);
        }
    }
}
