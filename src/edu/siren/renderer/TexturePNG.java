package edu.siren.renderer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
 * Uses the de.matthiasmann.twl.utils.PNGDecoder object for loading a PNG.
 * This class wraps functionality and interfaces with the Texture.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class TexturePNG implements Texture {
    public int textureID;
    public int width, height;
    private int unit;
    public ByteBuffer buf = null;

    /**
     * Trivial constructor.
     */
    public TexturePNG() {
    }
    

    /**
     * Loads a 32-bit RGBA PNG texture.
     *
     * @param filename The PNG to load
     * @param unit The GL unit to define (GLTEXTURE0)
     * @throws IOException Thrown if the file does not exist.
     */
    public TexturePNG(String filename, int unit) throws IOException {
        loadTexture(filename, unit);
    }


    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#getTextureID()
     */
    public int getTextureID() {
        return textureID;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#getTextureBinding()
     */
    public int getTextureBinding() {
        return unit;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#loadTexture(java.lang.String, int)
     */
    public int loadTexture(String filename, int unit) throws IOException {
        this.unit = unit;

        // Load the PNG
        InputStream in = new FileInputStream(filename);
        PNGDecoder decoder = new PNGDecoder(in);

        width = decoder.getWidth();
        height = decoder.getHeight();

        // Allocate the byte buffer
        buf = ByteBuffer.allocateDirect(4 * decoder.getWidth()
                * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        in.close();

        // Create a new texture object in memory and bind it
        textureID = GL11.glGenTextures();
        GL13.glActiveTexture(unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);

        return textureID;
    }
}
