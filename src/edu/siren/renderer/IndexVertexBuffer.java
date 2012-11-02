package edu.siren.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class IndexVertexBuffer {
    private boolean hasVertices, hasIndicies, valid, bound;
    public int vaoid, vboid;
    public int textureIDs[];
    private int vertexCount, indexCount;
    public FloatBuffer vertices;
    public ByteBuffer indices;
    public int type;

    public IndexVertexBuffer(BufferType bufferType) {
        hasVertices = hasIndicies = valid = bound = false;
        vaoid = vboid = -1;
        type = bufferType == BufferType.STATIC ? GL15.GL_STATIC_DRAW
                : GL15.GL_DYNAMIC_DRAW;
    }

    public void put(Vertex... vs) {
        vertices = BufferUtils.createFloatBuffer(vs.length * Vertex.Size.total);
        for (Vertex v : vs)
            vertices.put(v.elements());
        vertices.flip();
        vertexCount = vs.length;
        hasVertices = true;
        valid = hasVertices && hasIndicies;
    }

    public void put(byte[] bytes) {
        indices = BufferUtils.createByteBuffer(bytes.length);
        indices.put(bytes);
        indices.flip();
        indexCount = bytes.length;
        hasIndicies = true;
        valid = hasVertices && hasIndicies;
    }

    public void put(Texture... tx) {
        textureIDs = new int[tx.length * 2];
        for (int i = 0, j = 0; i < tx.length; i++) {
            textureIDs[j++] = tx[i].getTextureBinding();
            textureIDs[j++] = tx[i].getTextureID();
        }
    }

    private void generateVAOVBO() {
        vaoid = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoid);

        vboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(2, Vertex.Size.position, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.position);

        GL20.glVertexAttribPointer(1, Vertex.Size.color, GL11.GL_FLOAT, false,
                Vertex.Byte.stride, Vertex.Offsets.color);

        GL20.glVertexAttribPointer(0, Vertex.Size.texture, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.texture);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        vboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        bound = true;
    }

    public void draw() {
        if (!valid) {
            return;
        } else if (!bound) {
            generateVAOVBO();
        }

        if (textureIDs != null) {
            for (int i = 0; i < textureIDs.length;) {
                GL13.glActiveTexture(textureIDs[i++]);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDs[i++]);
            }
        }

        GL30.glBindVertexArray(vaoid);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount,
                GL11.GL_UNSIGNED_BYTE, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

}
