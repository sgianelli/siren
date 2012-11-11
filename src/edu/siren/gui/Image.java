package edu.siren.gui;

import java.io.IOException;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import edu.siren.core.geom.Rectangle;
import edu.siren.renderer.Font;
import edu.siren.renderer.IndexVertexBuffer;

public class Image extends Element {
    class ImageState {
        public String titleText = "";
        public double hoverDt = 0.0;
        public double titleTime = 2000.0;
        public double hoverTime = 0.0;
        public float titleX = 0.0f, titleY = 0.0f;
        public IndexVertexBuffer titleBuffer;
        public Font font;
        public int lastX = 0;
        public int lastY = 0;
    };
 
    
    ImageState imageState;
    
    public Image(String pngFile) throws IOException {
        this(pngFile, null);
    }        
    
    /**
     * Creates a new image from `png` that uses `titleText` as the
     * hover text when the image has been hovered for 2 seconds.
     * @throws IOException 
     */
    public Image(String pngFile, String title) throws IOException {
        this.imageState = new ImageState();
        this.state.name = "Image";
        this.imageState.font = new Font("res/tests/fonts/nostalgia.png", 24);
        titleText(title);
        this.background(pngFile);
        this.imageState.hoverDt = 0.0f;
        
        Rectangle bbox = this.state.background.getRect();
        this.dimensions(bbox.width, bbox.height);
        
        this.onMouseHover(new ElementEvent() {
            public boolean event(Element element) {
                if (imageState.titleText == null)
                    return false;
                
                if (imageState.hoverDt == 0 ||
                    imageState.lastX != Mouse.getX() ||
                    imageState.lastY != Mouse.getY())
                {
                    imageState.hoverDt = getTime();
                    imageState.lastX = Mouse.getX();
                    imageState.lastY = Mouse.getY();
                }
                                
                double dt = getTime() - imageState.hoverDt;
                imageState.hoverTime = dt;

                if (imageState.hoverTime > imageState.titleTime) {
                    
                    // Most browsers act like this--they retain the original
                    // position of the mouse for the overlay
                    if (imageState.titleX == 0.0f && 
                            imageState.titleY == 0.0f) 
                    {
                        imageState.titleX = Mouse.getX();
                        imageState.titleY = Mouse.getY();
                    }
                    
                    // Draw the text
                    imageState.font.print(imageState.titleText, 2, 
                            imageState.titleX, imageState.titleY);
                }
                return false;
            }
        });
        
        this.onMouseExit(new ElementEvent() {
            public boolean event(Element element) {
                imageState.hoverDt = 0.0f;
                imageState.titleX = 0.0f;
                imageState.titleY = 0.0f;
                return false;
            }
        });
                
    }
    
    public void titleText(String title) {
        this.imageState.titleText = title;
    }
    
    public String titleText() {
        return this.imageState.titleText;
    }

    public static Image CreateNoException(String string) {
        try {
            Image image = new Image(string);
            return image;
        } catch (Exception e) {
            System.err.println("Failed to create image with: " + string);
        }
        
        return null;
    }
}
