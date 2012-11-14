package edu.siren.renderer;

/**
 * A Vertex object defines XYZW, RGBA, and ST coordinates.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Vertex {
    public float[] vxyzw = new float[] { 0f, 0f, 0f, 1f };
    public float[] vrgba = new float[] { 1f, 1f, 1f, 1f };
    public float[] vst = new float[] { 0f, 0f };
    
    /**
     * Sets everything.
     */
    public void xyzrgbst(float x, float y, float z,
                         float r, float g, float b,
                         float s, float t)
    {
        xyzw(x, y, z, 1.0f);
        rgba(r, g, b, 1.0f);
        st(s, t);
    }

    /**
     * Set the XYZW position of this vertex
     */
    public void xyzw(float x, float y, float z, float w) {
        vxyzw[0] = x;
        vxyzw[1] = y;
        vxyzw[2] = z;
        vxyzw[3] = 1.0f;
    }

    /**
     * Sets the XYZ position of this vertex; assigns 1.0 to w
     */
    public void xyz(float x, float y, float z) {
        vxyzw[0] = x;
        vxyzw[1] = y;
        vxyzw[2] = z;
        vxyzw[3] = 1.0f;
    }

    /**
     * Sets the RGBA color of this vertex.
     */
    public void rgba(float r, float g, float b, float a) {
        vrgba[0] = r;
        vrgba[1] = g;
        vrgba[2] = b;
        vrgba[3] = a;
    }

    /**
     * Sets the RGB color of this vertex; assigns 1.0 to a
     */
    public void rgb(float r, float g, float b) {
        vrgba[0] = r;
        vrgba[1] = g;
        vrgba[2] = b;
        vrgba[3] = 1.0f;
    }

    /**
     * Sets the ST texture coordinates of this vertex.
     */
    public void st(float s, float t) {
        vst = new float[] { s, t };
    }

    /**
     * Sizing for each vertex element.
     */
    public final class Size {
        static final int position = 4;
        static final int color = 4;
        static final int texture = 2;
        static final int total = position + color + texture;
    }

    /**
     * Byte allocations for each vertex element.
     */
    public final class Byte {
        static final int element = 4;
        static final int position = Size.position * element;
        static final int color = Size.color * element;
        static final int texture = Size.texture * element;
        static final int stride = position + color + texture;
    }

    /**
     * Stride/offset position for each vertex element.
     */
    public final class Offsets {
        static final int position = 0;
        static final int color = position + Byte.position;
        static final int texture = color + Byte.color;
    }

    /**
     * Constructs and returns a buffer suitable for a VAO
     * @return The float array of vertices.
     */
    public float[] elements() {
        int i = 0, s = 0;
        float[] elements = new float[Size.total];

        s += Size.position;
        for (int p = 0; i < s; i++, p++)
            elements[i] = this.vxyzw[p];

        s += Size.color;
        for (int p = 0; i < s; i++, p++)
            elements[i] = this.vrgba[p];

        s += Size.texture;
        for (int p = 0; i < s; i++, p++)
            elements[i] = this.vst[p];

        return elements;
    }
}
