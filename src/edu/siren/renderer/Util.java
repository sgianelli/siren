package edu.siren.renderer;

/**
 * Defines various static utility methods.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Util {

    /**
     * Shape tests.
     *
     * @author Justin Van Horne <justinvh@gmail.com>
     */
    public static class Shape {

        /**
         * Constructs a new quad tile object for testing.
         *
         * @param texture The texture to bind.
         * @return An IndexVertexBuffer to draw
         */
        public static IndexVertexBuffer createQuad(Texture texture) {
            // Corner 1
            Vertex v0 = new Vertex();
            v0.xyz(-0.5f, 0.5f, 0);
            v0.rgb(1, 0, 0);
            v0.st(0, 0);

            // Corner 2
            Vertex v1 = new Vertex();
            v1.xyz(-0.5f, -0.5f, 0);
            v1.rgb(0, 1, 0);
            v1.st(0, 1);

            // Corner 3
            Vertex v2 = new Vertex();
            v2.xyz(0.5f, -0.5f, 0);
            v2.rgb(0, 0, 1);
            v2.st(1, 1);

            // Corner 4
            Vertex v3 = new Vertex();
            v3.xyz(0.5f, 0.5f, 0);
            v3.rgb(1, 1, 1);
            v3.st(1, 0);

            // Fill the index vertex buffer
            IndexVertexBuffer ivb = new IndexVertexBuffer(BufferType.STATIC);
            ivb.put(v0, v1, v2, v3);
            byte[] indicies = { 0, 1, 2, 2, 3, 0 };
            ivb.put(indicies);
            ivb.put(texture);

            return ivb;
        }
    }

}
