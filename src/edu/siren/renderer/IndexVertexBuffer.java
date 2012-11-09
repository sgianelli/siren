package edu.siren.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * An IndexVertexBuffer is a drawable surface that defines a stride-array
 * of vertices, color, and texture coordinates. Each vertex can have
 * multiple textures associated with it provided the GL textures are used.
 * Structure: [VVVVCCCCTT]
 *
 * @see edu.siren.renderer.Vertex
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class IndexVertexBuffer implements Drawable {
    private boolean hasVertices = false;    
    private boolean hasIndicies = false;
    private boolean valid = false;
    private boolean bound = false;
    private int indexCount = 0;
    
    public FloatBuffer vertices = null;
    public ByteBuffer indices = null;
    public int type = -1;
    public int vaoid = -1;
    public int vboid = -1;
    public int fboid = -1;
    public int fbotid = -1;
    public int textureIDs[] = null;

    /**
     * Each IndexVertexBuffer is either defined as a STATIC DRAW surface
     * or a DYNAMIC DRAW. If a static draw surface is constructed, then the
     * renderer will draw the scene to a framebuffer and use the generated
     * texture for rendering successive frames. This means it is probably
     * best practice to define layers in terms of the interactive-uses.
     * An interactive layer should sit on-top of a static layer so it doesn't
     * force the renderer to reconstruct a static draw surface.
     *
     * @param bufferType The type of surface to generate.
     * @TODO(justinvh) Draw GL15.GL_STATIC_DRAW surfaces to the framebuffer.
     */
    public IndexVertexBuffer(BufferType bufferType) {
        hasVertices = hasIndicies = valid = bound = false;
        vaoid = vboid = -1;
        type = bufferType == BufferType.STATIC ? GL15.GL_STATIC_DRAW
                : GL15.GL_DYNAMIC_DRAW;
    }

    /**
     * Puts and finalizes the IndexVertexBuffer Vertex array. This should
     * be called once all the vertices have been constructed as a FloatBuffer
     * is constructed on call.
     *
     * @param vs A variable argument (or array) of Vertex objects.
     */
    public void put(Vertex... vs) {
        vertices = BufferUtils.createFloatBuffer(vs.length * Vertex.Size.total);
        for (Vertex v : vs)
            vertices.put(v.elements());
        vertices.flip();
        hasVertices = true;
        valid = hasVertices && hasIndicies;
        if (valid && !bound) generateVAOVBO();
    }

    /**
     * Puts and finalizes the IndexVertexBuffer indices array. Like the
     * put(Vertex) call, this should be called once all indices are finalized
     * as a ByteBuffer is constructed on call.
     *
     * @param bytes A variable argument (or byte[]) of indices.
     */
    public void put(byte[] bytes) {
        indices = BufferUtils.createByteBuffer(bytes.length);
        indices.put(bytes);
        indices.flip();
        indexCount = bytes.length;
        hasIndicies = true;
        valid = hasVertices && hasIndicies;
        if (valid && !bound) generateVAOVBO();
    }

    /**
     * Puts and finalizes the Texture array. Like the put(Vertex) and
     * put(Byte[]) calls, this should be called once all texture are finalized
     * as an int[] array is constructed on call.
     *
     * @param textures A variable argument (or Texture array) of Texture objects.
     */
    public void put(Texture... textures) {
        int j = 0;
        textureIDs = new int[textures.length * 2];
        for (Texture texture : textures) {
            textureIDs[j++] = texture.getTextureBinding();
            textureIDs[j++] = texture.getTextureID();
        }
    }
       
    /**
     * Generates the vertex-array and vertex-buffer objects. This function
     * expects that the vertex objects are in a stride that is defined in
     * the Vertex class (@see Vertex).
     */
    private void generateVAOVBO() {
        // Generate a VAO
        vaoid = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoid);

        // Generate the VBO
        vboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Define the position, color, and texture offsets
        GL20.glVertexAttribPointer(2, Vertex.Size.position, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.position);

        // Define the position, color, and texture offsets
        GL20.glVertexAttribPointer(1, Vertex.Size.color, GL11.GL_FLOAT, false,
                Vertex.Byte.stride, Vertex.Offsets.color);

        // Define the position, color, and texture offsets
        GL20.glVertexAttribPointer(0, Vertex.Size.texture, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.texture);

        // Disable the array buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        // Generate a VBO
        vboid = GL15.glGenBuffers();

        // Bind the VBO and indices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        // Try to generate FBO
        // generateFBO();

        // Buffer is now ready to draw
        bound = true;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    public void draw() {
        // Try to load and bind the textures
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); 
        if (textureIDs != null) {
            for (int i = 0; i < textureIDs.length;) {
                GL13.glActiveTexture(textureIDs[i++]);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDs[i++]);
            }
        }   
        
        // Active the VAO
        GL30.glBindVertexArray(vaoid);

        // Enable the position, color, and texture attributes
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Active the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);

        // Draw the triangles
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount,
                GL11.GL_UNSIGNED_BYTE, 0);

        // Unbind everything
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);        
    }
}
