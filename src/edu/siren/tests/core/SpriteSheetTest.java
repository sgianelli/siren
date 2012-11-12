package edu.siren.tests.core;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;
import edu.siren.core.sprite.AnimationSequence;
import edu.siren.core.sprite.Sprite;
import edu.siren.core.sprite.SheetEntry;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class SpriteSheetTest {
    
    public static void main(String[] args) throws IOException, LWJGLException {
        // Initialize the screen for testing
        Screen screen = new Screen("Sprite Tests", 640, 480);
        
        // Explicitly create a 2D shader and bind it to the Perspective2D
        // instance for a 640x480 orthographic projection
        Perspective2D perspective = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert", 
                                   "res/tests/glsl/2d-perspective.frag");
        perspective.bindToShader(shader);
        
        SpriteSheet csssheet = SpriteSheet.fromCSS
                ("res/game/gui/sprite-sheet.png",
                "res/game/gui/sprite-sheet.css");

        SpriteSheet linksheet = SpriteSheet.fromCSS
                ("res/game/sprites/characters/link.png",
                "res/game/sprites/characters/link.css");
        

        // Load a new sprite sheet (1st argument)
        SpriteSheet sheet = new SpriteSheet(
                // The sprite sheet to use
                "res/game/gui/sprite-sheet.png",
                
                // These are the corresponding sprite-textures in the sheet
                //              SPRITE NAME      W   H    X    Y
                new SheetEntry("bubble",        71, 46,   0,  87),
                new SheetEntry("large-bubble", 172, 76,   0,   0),
                new SheetEntry("button",        79, 39,  82,  87),
                new SheetEntry("empty-heart",   22, 18, 148, 137),
                new SheetEntry("half-heart",    22, 18,  82, 137),
                new SheetEntry("full-heart",    22, 18, 115, 137));

        // We can extract sprites in two ways. The first way is to extract
        // animations from the sprite sheet. The arguments are variable-length
        // So you can specifiy multiple animations for a sprite. The default
        // animation is whatever animation is first in the arg-list.
        Sprite heart = sheet.createSprite(
                // "heart" is the name of the animation
                new Animation("heart",
                        // Extract "empty-heart" and designate it a 1s frame
                        // Then extract "half-heart" and designate it as 1s
                        // Finally extract "full-heart" and do the same.
                        new AnimationFrame("empty-heart", 1000),
                        new AnimationFrame("half-heart", 1000),
                        new AnimationFrame("full-heart", 1000)));
        
        // This demonstrates using contiguous animation sequences by using
        // a conventional naming to generate animations
        Sprite link = linksheet.createSprite(
                // Creates a new Animation using the AnimationSequence class
                // Basically wraps AnimationSequence to generate a series of
                // sprite references using a prefix and a counter.
                // So, "link-right-" -> "link-right-1", ... "link-right-7"
                // with 100msec frames
                //
                //            FRAME NAME    SPRITE-PREFIX  START  END  MSEC
                new Animation("move-right", "link-right-", 1, 7, 100),
                new Animation("move-left", "link-left-", 1, 7, 100),
                new Animation("move-forward", "link-forward-", 1, 7, 100),
                new Animation("move-backward", "link-backward-", 1, 7, 100));
        
        // Alternatively we can just extract a sprite-texture. A default
        // animation is created and it is given an infinite length time for
        // the animation (a single texture)
        Sprite bubble = csssheet.createSprite("bubble");
        
        // Heart location on the orthographic projection
        // Which in our case is a 2D 640x480
        heart.spriteX = 100;
        heart.spriteY = 100;
        
        // Bubble location on the orthographic projection
        bubble.spriteX = 200;
        bubble.spriteY = 200;
        
        link.spriteX = 50;
        link.spriteY = 50;
        
        // Continue until the screen dies
        // Shader is explicitly used since we are detached from the engine's
        // game state which would control all of this.
        while(screen.nextFrame()) {
            shader.use();
            
            // Draw each sprite
            heart.draw();
            bubble.draw();
            link.draw();
            
            shader.release();
        }
        
        // Always cleanup
        screen.cleanup();
    }

}
