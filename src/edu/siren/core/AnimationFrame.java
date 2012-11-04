package edu.siren.core;

import java.io.IOException;

public class AnimationFrame extends Tile {
    public double frameTime;
    private float lastX = -1, lastY = -1;

    public AnimationFrame(String sprite, double time) throws IOException {
        super(sprite, 0, 0);
        this.frameTime = time;
    }

    public void draw(float x, float y) {
        if (lastX != x || lastY != y) {
            bounds = new Rectangle(x, y, bounds.width, bounds.height);
            createIndexVertexBuffer(1, 1);
            lastX = x;
            lastY = y;
        }
        draw();
    }
}
