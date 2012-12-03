package edu.siren.world.creator;

import java.awt.*;
import java.io.IOException;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
public class WorldCanvas extends Canvas
{
	
    public String title;
    public int width, height;
    public int sync = 0;
    public boolean fullscreen = false;
    static WCanvas WC;
    public static int WINDOW_WIDTH = 512;
    public static int WINDOW_HEIGHT = 448;
    
    public WorldCanvas() throws LWJGLException, IOException
    {

        this.width = 512;
        this.height = 448;
        


        setVisible(true);
        setSize(width, height);
        reload();
    }
    

  /**
   * Reloads the screen with OpenGL 3.2 core
   *
   * @throws LWJGLException An exception that is thrown if LWJGL bails
   */
  public void reload() throws LWJGLException {
      // Describe the format
      PixelFormat pixelFormat = new PixelFormat();
      ContextAttribs cntxt = new ContextAttribs(3, 2).withProfileCore(true);
      cntxt.withForwardCompatible(true);
      cntxt.withProfileCore(true);

      // Execution Context
      Display.setDisplayMode(new DisplayMode(width, height));
//      Display.setTitle(title);
      	//Display.setParent(WC);
    //  Parent.setDisplayable(true);
     
      Display.create(pixelFormat, cntxt);

      // Do our basic GL setup
      //GL11.glClearColor(0.25f, 0.5f, 0.25f, 1.0f);
      backgroundColor(0.25f, 0.5f, 0.25f, 1.0f);
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
      sync = 30;
      
      Keyboard.create();
      Mouse.create();
  }

  /**
   * Checks the LWJGL Display singleton if it is still open.
   *
   * @return true if the Display is still open.
   */
  public boolean isOpened() {
      return !Display.isCloseRequested();
  }

  /**
   * Attempts to sync the display if an FPS is defined.
   *
   * @see edu.sirent.renderer.Screen#sync
   */
  public void syncFPS() {
      if (sync > 0) {
          Display.sync(sync);
      }
  }

  public void backgroundColor(float r, float g, float b, float a) {
  	GL11.glClearColor(r, g, b, a);	
  }
  
  /**
   * Issues a glClear call for various buffer bits. 
   */
  public void clear() {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  /**
   * Updates the screen with whatever that has been drawn
   */
  public void update() {
      syncFPS();
      Display.update();
  }

  /**
   * Destroys the screen and runs any other cleanup routines.
   */
  public void cleanup() {
      
  	
  	// Destroy the Display
  	Display.destroy();

  	// End the Program
  	System.exit(0);
  }

  public boolean nextFrame() {
      update();
      clear();
      return isOpened();
  }
  
  public void endFrame() {
      update();
  }


public void setCanvas(Canvas canvas) throws LWJGLException {
	Display.setParent(canvas);
	
}
}

class WCanvas extends Canvas
{
  public void paint(Graphics g)
  {

  }
}