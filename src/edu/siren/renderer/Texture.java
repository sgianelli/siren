package edu.siren.renderer;

import java.io.IOException;

public interface Texture {
    public abstract int getTextureID();

    public abstract int loadTexture(String filename, int unit)
            throws IOException;

    public abstract int getTextureBinding();
}
