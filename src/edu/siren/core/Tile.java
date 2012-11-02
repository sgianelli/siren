package edu.siren.core;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import edu.siren.renderer.BufferType;
import edu.siren.renderer.IndexVertexBuffer;
import edu.siren.renderer.Texture;
import edu.siren.renderer.TexturePNG;
import edu.siren.renderer.Vertex;

public class Tile {
    Texture texture;
    Rectangle bounds;
    IndexVertexBuffer ivb;
    private static final HashMap<String, Texture> cache = new HashMap<String, Texture>();

    public Tile(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer();
    }

    public Tile(String filename, float x, float y, float width, float height)
            throws IOException {
        Texture cached = cache.get(filename);
        if (cached == null) {
            cached = new TexturePNG(filename, GL13.GL_TEXTURE0);
            cache.put(filename, cached);
        }
        this.texture = cached;
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer();
    }

    public Tile(Texture texture, float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        this.texture = texture;
        createIndexVertexBuffer();
    }

    public void draw() {
        ivb.draw();
    }

    private void createIndexVertexBuffer() {
        float x, y;

        // Corner 1
        Vertex v0 = new Vertex();
        y = bounds.bottom();
        x = bounds.left();
        v0.xyz(x, y, 0);
        v0.rgb(1, 0, 0);
        v0.st(0, 0);

        // Corner 2
        Vertex v1 = new Vertex();
        y = bounds.top();
        x = bounds.left();
        v1.xyz(x, y, 0);
        v1.rgb(0, 1, 0);
        v1.st(0, bounds.height);

        // Corner 3
        Vertex v2 = new Vertex();
        y = bounds.top();
        x = bounds.right();
        v2.xyz(x, y, 0);
        v2.rgb(0, 0, 1);
        v2.st(bounds.width, bounds.height);

        // Corner 4
        Vertex v3 = new Vertex();
        y = bounds.bottom();
        x = bounds.right();
        v3.xyz(x, y, 0);
        v3.rgb(1, 1, 1);
        v3.st(bounds.width, 0);

        // Fill the index vertex buffer
        ivb = new IndexVertexBuffer(BufferType.STATIC);
        ivb.put(v0, v1, v2, v3);
        byte[] indices = { 0, 1, 2, 2, 3, 0 };
        ivb.put(indices);

        if (texture != null)
            ivb.put(texture);
    }

    boolean outside(Rectangle bounds) {

        // TODO Auto-generated method stub
        return false;
    }

}
