package edu.siren.renderer;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import edu.siren.core.Rectangle;

public class Font {
    private int square;
    public TexturePNG texture;
    private HashMap<String, IndexVertexBuffer> lookup;

    public Font(String image, int square) throws IOException {
        texture = new TexturePNG(image, GL13.GL_TEXTURE0);
        this.square = square;
        lookup = new HashMap<String, IndexVertexBuffer>();
    }

    public float tex(float x, float y) {
        return ((square * x) * texture.width + (square * y))
                / (texture.width * texture.height) / 1.0f;
    }

    public float s(float x) {
        float rect = texture.width / square;
        return (x / rect) + (1.5f / (float) (square)) / rect;
    }

    public float t(float y) {
        float rect = texture.height / square;
        return (y / rect) - (1.0f / square) / rect;
    }

    public void print(String what, int size, float x1, float y1) {
        float rect = square / (float) size;

        what = what.replaceAll("\t", "    ");
        String[] lines = what.split("\n");

        if (lines.length > 1) {
            float offset = 0;
            for (int i = lines.length - 1; i >= 0; i--) {
                print(lines[i], size, x1, y1 + offset);
                offset += rect;
            }
            return;
        }

        byte k = 0;
        y1 += 480 - square;

        IndexVertexBuffer ivb = new IndexVertexBuffer(BufferType.STATIC);
        Vertex[] vertices = new Vertex[what.length() * 4];
        byte[] indices = new byte[what.length() * 6];
        int j = 0, l = 0;

        for (int i = 0; i < what.length(); i++, k += 4) {
            Rectangle bounds = new Rectangle(x1 + i * rect, y1, rect, rect);

            int h = (int) (what.charAt(i));
            int c = h / (texture.width / square);
            int r = h % (texture.width / square);

            // Corner 1
            float x, y;
            Vertex v0 = new Vertex();
            y = bounds.bottom();
            x = bounds.left();
            v0.xyz(x, y, 0);
            v0.rgb(1, 0, 0);
            v0.st(s(r), t(c));
            vertices[j++] = v0;

            // Corner 2
            Vertex v1 = new Vertex();
            y = bounds.top();
            x = bounds.left();
            v1.xyz(x, y, 0);
            v1.rgb(0, 1, 0);
            v1.st(s(r), t(c + 1));
            vertices[j++] = v1;

            // Corner 3
            Vertex v2 = new Vertex();
            y = bounds.top();
            x = bounds.right();
            v2.xyz(x, y, 0);
            v2.rgb(0, 0, 1);
            v2.st(s(r + 1), t(c + 1));
            vertices[j++] = v2;

            // Corner 4
            Vertex v3 = new Vertex();
            y = bounds.bottom();
            x = bounds.right();
            v3.xyz(x, y, 0);
            v3.rgb(1, 1, 1);
            v3.st(s(r + 1), t(c));
            vertices[j++] = v3;

            indices[l++] = ((byte) (0 + k));
            indices[l++] = ((byte) (1 + k));
            indices[l++] = ((byte) (2 + k));
            indices[l++] = ((byte) (2 + k));
            indices[l++] = ((byte) (3 + k));
            indices[l++] = ((byte) (0 + k));
        }
        ivb.put(vertices);
        ivb.put(indices);
        ivb.put(texture);
        ivb.draw();
    }
}
