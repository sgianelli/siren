package edu.siren.core.tile;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import edu.siren.core.geom.Rectangle;
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
        createIndexVertexBuffer(1.0f, 1.0f);
    }

    /**
     * Constructs a new tile object at (x, y) and precomputed (w, h) from the
     * specified PNG which acts as the texture.
     */
    public Tile(String filename, float x, float y) throws IOException {
        load(filename, x, y);
    }
    
    public void load(String filename, float x, float y) throws IOException {
        TexturePNG cached = cache.get(filename);
        if (cached == null) {
            cached = new TexturePNG(filename, GL13.GL_TEXTURE0);
            cache.put(filename, cached);
        }
        this.texture = cached;
        int width = this.texture.width;
        int height = this.texture.height;
        bounds = new Rectangle(x, y, width, height);
        createIndexVertexBuffer(1.0f, 1.0f);
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
        createIndexVertexBuffer(1, 1);
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
    
    public void createIndexVertexBuffer(float xbl, float ybl,
                                        float xtl, float ytl,
                                        float xtr, float ytr,
                                        float xbr, float ybr)
    {
        float x, y;
        
        // Corner 1
        Vertex v0 = new Vertex();
        y = bounds.bottom();
        x = bounds.left();
        v0.xyz(x, y, 0);
        v0.rgb(0, 0, 0);
        v0.st(xbl, ybl);
        
        // Corner 2
        Vertex v1 = new Vertex();
        y = bounds.top();
        x = bounds.left();
        v1.xyz(x, y, 0);
        v1.rgb(0, 0, 0);
        v1.st(xtl, ytl);

        // Corner 3
        Vertex v2 = new Vertex();
        y = bounds.top();
        x = bounds.right();
        v2.xyz(x, y, 0);
        v2.rgb(0, 0, 0);
        v2.st(xtr, ytr);

        // Corner 4
        Vertex v3 = new Vertex();
        y = bounds.bottom();
        x = bounds.right();
        v3.xyz(x, y, 0);
        v3.rgb(0, 0, 0);
        v3.st(xbr, ybr);

        // Fill the index vertex buffer
        ivb = new IndexVertexBuffer(BufferType.STATIC);
        ivb.put(v0, v1, v2, v3);
        byte[] indices = { 0, 1, 2, 2, 3, 0 };
        ivb.put(indices);

        if (texture != null)
            ivb.put(texture);
    }

    /**
     * Create the index vertex buffer object with (s, t) tile texture mapping.
     */
    public void createIndexVertexBuffer(float s, float t) {
        createIndexVertexBuffer(0, 0,   // bl
                                0, t,   // tl
                                s, t,   // tr
                                s, 0);  // bl
    }

    /**
     * Creates an inverted IVB suitable for textures that are
     * inverted and you want them corrected.
     */
    public void createInvertIndexVertexBuffer(float s, float t) {
        createIndexVertexBuffer(0, t,  // bl
                                0, 0,  // tl
                                s, 0,  // tr
                                s, t); // br
    }

}
