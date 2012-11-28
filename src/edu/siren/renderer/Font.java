package edu.siren.renderer;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import edu.siren.core.geom.Rectangle;

/**
 * A Font is a 32-bit RGBA PNG font-sprite sheet. Any sprite sheet can be
 * used provided it is monospaced and the grid-size is defined. For example
 * a typical 256-character sprite sheet might have a spacing of 24px and be
 * 16x16.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Font {
    public Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private IndexVertexBuffer[] ivbsCache = null;
    public TexturePNG texture;
    private String lastPrinted = null;
    public int squareW = 0;
    public int squareH = 0;
    private float[] rgba = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

    /**
     * Constructs a new Font object from a given 
     *
     * @param pngFile The PNG file to load the sprite sheet from
     * @param square The square window to move across the PNG
     * @throws IOException Throws if pngFile is not found
     */
    public Font(String pngFile, int square) throws IOException {
        this(pngFile, square, square);
    }

    public Font(String pngFile, int sw, int sh) throws IOException {
        texture = new TexturePNG(pngFile, GL13.GL_TEXTURE0);
        if (sw == -1 || sh == -1) {
            squareW = texture.width / 16;
            squareH = texture.height / 16;
        } else {
            squareW = sw;
            squareH = sh;
        }
    }

    public Font(String pngFile) throws IOException {
        this(pngFile, -1, -1);
    }

    /**
     * Constructs an s-texture offset into the PNG
     *
     * @param x The x or row offset
     * @return The S component of ST for the texture sprite sheet.
     */
    private float s(float x) {
        float rect = texture.width / squareW;
        return (x / rect);
    }

    /**
     * Constructs a t-texture offset into the PNG
     *
     * @param y The y or col offset
     * @return The T component of ST for the texture sprite sheet.
     */
    private float t(float y) {
        float rect = texture.height / squareH;
        return (y / rect);
    }

    /**
     * Prints a message to the screen. Each IndexVertexBuffer generated
     * from the string is drawn. This is a recursive call to the `print`
     * that constructs actual IndexVertexBuffer calls.
     *
     * @param what What to print
     * @param size The scaling of the font (1..n) where n is the smallest
     * @param x1 The x-position on the screen
     * @param y1 The y-position on the screen
     */
    public void print(String what, float size, float x1, float y1) {
        if (what != lastPrinted || bounds.x != x1 || bounds.y != y1 || ivbsCache == null) {
            ivbsCache = print(what, size, x1, y1, null, 0);
            bounds.x = x1;
            bounds.y = y1;
            lastPrinted = what;
        }
        
        for (IndexVertexBuffer ivb : ivbsCache) {
            ivb.draw();
        }
    }

    /**
     * Constructs an IndexVertexBuffer suitable for drawing to the screen.
     * This method is the recursive call for constructing IndexVertexBuffer
     * objects for each newline in the designated string `what`.
     *
     * @param what What to print
     * @param size The scaling of the font (1..n) where n is the smallest
     * @param x1 The x-position on the screen
     * @param y1 The y-position on the screen
     * @param ivbs The IndexVertexBuffer array
     * @param ivbi The current offset into the IndexVertexBuffer
     * @return An array of IndexVertexBuffers
     */
    public IndexVertexBuffer[] print(String what, float size, float x1, 
            float y1, IndexVertexBuffer[] ivbs, int ivbi) 
    {
        // This will define the scaling size of the character.
        float rectH = squareH / size;
        float rectW  = squareW / size;

        // Quick regex replacements for common escape sequences.
        what = what.replaceAll("\t", "    ");

        // This is our recursive basis case for splitting the string
        // into newlines so we can render each string with an offset.
        String[] lines = what.split("\n");
        if (lines.length > 1) {
            float offset = 0;
            ivbs = new IndexVertexBuffer[lines.length];
            for (int i = 0; i < lines.length; i++) {
                ivbs = print(lines[i], size, x1, y1 - offset , ivbs, i);
                offset += rectH;
            }
            return ivbs;
        }

        // Construct an ivbs if we are the base case
        if (ivbs == null) {
            ivbi = 0;
            ivbs = new IndexVertexBuffer[1];
        }

        // Construct a STATIC buffer for the current scene
        // We precompute the sizes of the vertex and index arrays
        IndexVertexBuffer ivb = new IndexVertexBuffer(BufferType.STATIC);
        Vertex[] vertices = new Vertex[what.length() * 4];
        byte[] indices = new byte[what.length() * 6];

        // Go over the characters and generate square vertices for each
        // character. The k-offset allows us to build a large index array
        float width = 0.0f, height = 0;
        for (int i = 0, j = 0, l = 0, k = 0; i < what.length(); i++, k += 4) {
            Rectangle charrect = new Rectangle(x1 + i * rectW, y1, rectW, rectH);
            width += charrect.width;
            height = charrect.height;
            // Figure out what column and row we are drawing from
            // the sprite sheet.
            int h = (what.charAt(i));
            int c = h / 16;
            int r = h % 16;
            
            // Corner 1
            float x, y;
            Vertex v0 = new Vertex();
            y = charrect.bottom();
            x = charrect.left();
            v0.xyz(x, y, 0);
            v0.rgb(rgba);
            v0.st(s(r), t(c));
            vertices[j++] = v0;
            
            // Corner 2
            Vertex v1 = new Vertex();
            y = charrect.top();
            x = charrect.left();
            v1.xyz(x, y, 0);
            v1.rgb(rgba);
            v1.st(s(r), t(c + 1));
            vertices[j++] = v1;

            // Corner 3
            Vertex v2 = new Vertex();
            y = charrect.top();
            x = charrect.right();
            v2.xyz(x, y, 0);
            v2.rgb(rgba);
            v2.st(s(r + 1), t(c + 1));
            vertices[j++] = v2;

            // Corner 4
            Vertex v3 = new Vertex();
            y = charrect.bottom();
            x = charrect.right();
            v3.xyz(x, y, 0);
            v3.rgb(rgba);
            v3.st(s(r + 1), t(c));
            vertices[j++] = v3;

            // Also note that our Y-indices are flipped.
            indices[l++] = ((byte) (0 + k));
            indices[l++] = ((byte) (1 + k));
            indices[l++] = ((byte) (2 + k));
            indices[l++] = ((byte) (2 + k));
            indices[l++] = ((byte) (3 + k));
            indices[l++] = ((byte) (0 + k));
        }
        
        if (width > bounds.width) {
            bounds.width = width;
        }
        
        bounds.height += height;
        bounds.y -= height;
        
        // Construct the IndexVertexBuffer values
        ivb.put(vertices);
        ivb.put(indices);
        ivb.put(texture);

        // Set the current position in the array
        // And return the newly generated IVB
        ivbs[ivbi] = ivb;
        return ivbs;
    }
    
    public void invalidate() {
        ivbsCache = null;
        bounds = new Rectangle(0, 0, 0, 0);
    }

    public FontSequence printFrames(boolean successive, FontFrame... frames) {
        return new FontSequence(this, successive, frames);
    }

    public float width() {
        return bounds.width;
    }

    public float lineHeight() {
        if (ivbsCache != null) {
            return bounds.height / (float) (ivbsCache.length);
        }
        
        return 0.0f;
    }

    public void color(float r, float g, float b, float a) {
        rgba = new float[]{r, g, b, a};
        invalidate();
    }
}
