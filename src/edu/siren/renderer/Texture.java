package edu.siren.renderer;

import java.io.IOException;

public interface Texture {
    public int getTextureID();

    public int loadTexture(String filename, int unit) throws IOException;

    public int getTextureBinding();
}
