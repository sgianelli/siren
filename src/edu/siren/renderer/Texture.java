package edu.siren.renderer;

import java.io.IOException;

/**
 * Defines an interface for any texture that is loaded into the engine.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public interface Texture {
    /**
     * The texture must return an OpenGL texture id.
     * @return The OpenGL texture id
     */
    public int getTextureID();

    /**
     * The texture must provide a means of loading a texture from a file.
     *
     * @param filename The file which holds the texture
     * @param unit The GL unit type (GLTEXURE0, GLTEXTURE1, etc.)
     * @return The GL textureID
     * @throws IOException Thrown if the file is not found.
     */
    public int loadTexture(String filename, int unit) throws IOException;

    /**
     * Returns the unit-binding of the texture (GLTEXTURE0, GLTEXTURE1, etc.)
     *
     * @return The GL texture binding
     */
    public int getTextureBinding();
}
