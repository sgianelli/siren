package edu.siren.renderer;

import java.io.IOException;

public class TextureGL implements Texture {
    public int textureID, textureBinding;

    public TextureGL(int textureID, int textureBinding) {
        this.textureID = textureID;
        this.textureBinding = textureBinding;
    }

    @Override
    public int getTextureID() {
        return textureID;
    }

    @Override
    public int loadTexture(String filename, int unit) throws IOException {
        return 0;
    }

    @Override
    public int getTextureBinding() {
        return textureBinding;
    }

}
