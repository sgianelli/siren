package edu.siren.renderer;

public class Vertex {
    public float[] vxyzw = new float[] { 0f, 0f, 0f, 1f };
    public float[] vrgba = new float[] { 1f, 1f, 1f, 1f };
    public float[] vst = new float[] { 0f, 0f };

    public void xyzw(float x, float y, float z, float w) {
        vxyzw[0] = x;
        vxyzw[1] = y;
        vxyzw[2] = z;
        vxyzw[3] = 1.0f;
    }

    public void xyz(float x, float y, float z) {
        vxyzw[0] = x;
        vxyzw[1] = y;
        vxyzw[2] = z;
        vxyzw[3] = 1.0f;
    }

    public void rgba(float r, float g, float b, float a) {
        vrgba[0] = r;
        vrgba[1] = g;
        vrgba[2] = b;
        vrgba[3] = a;
    }

    public void rgb(float r, float g, float b) {
        vrgba[0] = r;
        vrgba[1] = g;
        vrgba[2] = b;
        vrgba[3] = 1.0f;
    }

    public void st(float s, float t) {
        vst = new float[] { s, t };
    }

    public final class Size {
        static final int position = 4;
        static final int color = 4;
        static final int texture = 2;
        static final int total = position + color + texture;
    }

    public final class Byte {
        static final int element = 4;
        static final int position = Size.position * element;
        static final int color = Size.color * element;
        static final int texture = Size.texture * element;
        static final int stride = position + color + texture;
    }

    public final class Offsets {
        static final int position = 0;
        static final int color = position + Byte.position;
        static final int texture = color + Byte.color;
    }

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