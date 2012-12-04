package edu.siren.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

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
    private byte elementOffset = 0;
    private int position, color, tex;
    
    public FloatBuffer vertices = BufferUtils.createFloatBuffer(4 * Vertex.Size.total);
    public ByteBuffer indices = BufferUtils.createByteBuffer(6);
    public ArrayList<Vertex> vertexCache = new ArrayList<Vertex>();
    public ArrayList<Byte> indexCache = new ArrayList<Byte>();
    public int type = -1;
    public int vaoid = -1;
    public int vboid = -1;
    public int eboid = -1;
    public int fboid = -1;
    public int fbotid = -1;
    public int textureIDs[] = null;
    
    public String getID() {
        return null;
    }

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
        type = GL15.GL_DYNAMIC_DRAW;
    }

    /**
     * Puts and finalizes the IndexVertexBuffer Vertex array. This should
     * be called once all the vertices have been constructed as a FloatBuffer
     * is constructed on call.
     *
     * @param vs A variable argument (or array) of Vertex objects.
     */
    public void put(Vertex... vs) {
        for (Vertex v : vs)
            vertexCache.add(v);
        
        if (vertexCache.size() > 4)
            vertices = BufferUtils.createFloatBuffer(vertexCache.size() * Vertex.Size.total);
        
        for (Vertex v : vertexCache) {
            vertices.put(v.elements());
        }
        
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
        for (byte b : bytes) {
            indexCache.add((byte) (elementOffset + b));
        }
        
        elementOffset += bytes.length;
            
        if (indexCache.size() > 6)
            indices = BufferUtils.createByteBuffer(indexCache.size());
        
        for (byte b : indexCache) {
            indices.put(b);
        }
        
        indices.flip();
        indexCount = indexCache.size();
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
        // Just reload the vertices and don't do the rest of the nonsense
        if (vboid != -1) {
            
            GL30.glBindVertexArray(vaoid);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);
            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboid);
            GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indices);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            bound = true;
            return;
        }
        
        // Generate a VAO
        vaoid = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoid);

        // Generate the VBO
        vboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);

        // Define the position, color, and texture offsets
        int program = Shader.getActiveShader();
        
        position = GL20.glGetAttribLocation(program, "position");
        GL20.glEnableVertexAttribArray(position);
        GL20.glVertexAttribPointer(position, Vertex.Size.position, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.position);

        // Define the position, color, and texture offsets
        color = GL20.glGetAttribLocation(program, "color");
        GL20.glEnableVertexAttribArray(color);
        GL20.glVertexAttribPointer(color, Vertex.Size.color, GL11.GL_FLOAT, false,
                Vertex.Byte.stride, Vertex.Offsets.color);

        // Define the position, color, and texture offsets
        tex = GL20.glGetAttribLocation(program, "tex");
        GL20.glEnableVertexAttribArray(tex);
        GL20.glVertexAttribPointer(tex, Vertex.Size.texture, GL11.GL_FLOAT,
                false, Vertex.Byte.stride, Vertex.Offsets.texture);

        // Disable the array buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        // Bind the VBO and indices
        eboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices,
                GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        // Try to generate FBO
        // generateFBO();

        // Buffer is now ready to draw
        bound = true;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    @Override
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
        GL20.glEnableVertexAttribArray(position);
        GL20.glEnableVertexAttribArray(color);
        GL20.glEnableVertexAttribArray(tex);

        // Active the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboid);

        // Draw the triangles
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount,
                GL11.GL_UNSIGNED_BYTE, 0);
        
        // Unbind everything
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(position);
        GL20.glDisableVertexAttribArray(color);
        GL20.glDisableVertexAttribArray(tex);
        GL30.glBindVertexArray(0);        
    }

    public void clear() {
        vertexCache.clear();
        indexCache.clear();
        indexCount = 0;
        elementOffset = 0;
        valid = false;
        bound = false;
        hasIndicies = false;
        hasVertices = false;
    }
}
