package edu.siren.core;

import java.io.IOException;

public class AnimationFrame extends Tile {
    public double frameTime;

    public AnimationFrame(String sprite, double time) throws IOException {
        super(sprite, 0, 0);
        this.frameTime = time;
    }

    public void draw(float x, float y) {
        bounds = new Rectangle(x, y, bounds.width, bounds.height);
        createIndexVertexBuffer(1, 1);
        draw();
    }
}
