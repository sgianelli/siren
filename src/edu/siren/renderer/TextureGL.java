package edu.siren.renderer;

import java.io.IOException;
import java.io.Serializable;

/**
 * An explicitly defined Texture object suitable for external resources
 * that are not class-defined, i.e. procedural textures.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class TextureGL implements Texture, Serializable {
    public int textureID, textureBinding;

    /**
     * Constructs a new TextureGL object.
     *
     * @param textureID The textureID to store
     * @param textureBinding The textureBinding (GLTEXTURE0, etc.)
     */
    public TextureGL(int textureID, int textureBinding) {
        this.textureID = textureID;
        this.textureBinding = textureBinding;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#getTextureID()
     */
    @Override
	public int getTextureID() {
        return textureID;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#loadTexture(java.lang.String, int)
     */
    @Override
	public int loadTexture(String filename, int unit) throws IOException {
        return 0;
    }

    /* (non-Javadoc)
     * @see edu.siren.renderer.Texture#getTextureBinding()
     */
    @Override
	public int getTextureBinding() {
        return textureBinding;
    }

}
