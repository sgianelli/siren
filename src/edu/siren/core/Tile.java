package edu.siren.core;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import edu.siren.renderer.BufferType;
import edu.siren.renderer.Drawable;
import edu.siren.renderer.IndexVertexBuffer;
import edu.siren.renderer.TexturePNG;
import edu.siren.renderer.Vertex;

/**
 * A tile is a surface (often ground-projected) that provides a number of
 * various interactions and so on. In its most basic form it becomes merged
 * with other tiles when the renderer renders to a static frame buffer.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Tile implements Drawable {
    public TexturePNG texture;
    public Rectangle bounds;
    public IndexVertexBuffer ivb;
    public static final HashMap<String, TexturePNG> cache = new HashMap<String, TexturePNG>();

    /**
     * Trivial constructor.
     */
    public Tile() {
    }

    /**
     * Constructs a new tile object at (x, y) with a (w, h) dimension.
     */
    public Tile(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer(width, height);
    }

    /**
     * Constructs a new tile object at (x, y) and precomputed (w, h) from the
     * specified PNG which acts as the texture.
     */
    public Tile(String filename, float x, float y) throws IOException {
        TexturePNG cached = cache.get(filename);
        if (cached == null) {
            cached = new TexturePNG(filename, GL13.GL_TEXTURE0);
            cache.put(filename, cached);
        }
        this.texture = cached;
        int width = this.texture.width;
        int height = this.texture.height;
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer(width, height);
    }

    /**
     * Constructs a new tile object at (x, y) with (w, h) dimensions using
     * the PNG as the texture.
     */
    public Tile(String filename, float x, float y, float width, float height)
            throws IOException {
        TexturePNG cached = cache.get(filename);
        if (cached == null) {
            cached = new TexturePNG(filename, GL13.GL_TEXTURE0);
            cache.put(filename, cached);
        }
        this.texture = cached;
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer(width, height);
    }

    /**
     * Constructs a new tile object at (x, y) with (w, h) dimensions using
     * the PNG as the texture with (s, t) offsets into the image.
     */
    public Tile(String filename, float x, float y, float width, float height,
            float s, float t) throws IOException {
        TexturePNG cached = cache.get(filename);
        if (cached == null) {
            cached = new TexturePNG(filename, GL13.GL_TEXTURE0);
            cache.put(filename, cached);
        }
        this.texture = cached;
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer(s, t);
    }

    /**
     * Constructs a new tile object at (x, y) with (w, h) dimensions using
     * the texture loaded from TexturePNG as the texture.
     */
    public Tile(TexturePNG texture, float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        this.texture = texture;
        createIndexVertexBuffer(width, height);
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    public void draw() {
        ivb.draw();
    }

    /**
     * Create the index vertex buffer object with (s, t) tile texture mapping.
     */
    protected void createIndexVertexBuffer(float s, float t) {
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
        v1.st(0, t);

        // Corner 3
        Vertex v2 = new Vertex();
        y = bounds.top();
        x = bounds.right();
        v2.xyz(x, y, 0);
        v2.rgb(0, 0, 1);
        v2.st(s, t);

        // Corner 4
        Vertex v3 = new Vertex();
        y = bounds.bottom();
        x = bounds.right();
        v3.xyz(x, y, 0);
        v3.rgb(1, 1, 1);
        v3.st(s, 0);

        // Fill the index vertex buffer
        ivb = new IndexVertexBuffer(BufferType.STATIC);
        ivb.put(v0, v1, v2, v3);
        byte[] indices = { 0, 1, 2, 2, 3, 0 };
        ivb.put(indices);

        if (texture != null)
            ivb.put(texture);
    }

}
