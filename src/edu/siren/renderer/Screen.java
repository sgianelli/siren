package edu.siren.renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Screen {
    public String title;
    public int width, height;
    public int sync = 0;

    public Screen(String title, int width, int height) throws LWJGLException {
        this.title = title;
        this.width = width;
        this.height = height;
        reload();
    }

    public void reload() throws LWJGLException {
        // Describe the format
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2);
        contextAtrributes.withForwardCompatible(true);
        contextAtrributes.withProfileCore(true);

        // Execution Context
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setTitle(title);
        Display.create(pixelFormat, contextAtrributes);

        // Do our basic GL setup
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.4f, 0.0f, 0.0f, 0f);
        GL11.glViewport(0, 0, width, height);
    }

    public boolean isOpened() {
        return !Display.isCloseRequested();
    }

    public void syncFPS() {
        if (sync > 0) {
            Display.sync(sync);
        }
    }

    public void update() {
        syncFPS();
        Display.update();
    }

    public void cleanup() {
        Display.destroy();
    }
}
