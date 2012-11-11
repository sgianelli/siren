package edu.siren.core.sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL13;

import edu.siren.renderer.TexturePNG;

public class SpriteSheet {
    public Map<String, SheetEntry> entries;
    public TexturePNG texture;
    
    public SpriteSheet() { }
    
    public SpriteSheet(String spritesheet, SheetEntry... sprites) 
            throws IOException 
    {
        add(spritesheet, sprites);
    }
    
    public void add(String spritesheet, SheetEntry... sprites) 
            throws IOException 
    {
        if (entries == null) {
            entries = new HashMap<String, SheetEntry>();
            texture = new TexturePNG(spritesheet, GL13.GL_TEXTURE0);
        }
        
        for (SheetEntry entry : sprites) {
            entries.put(entry.identifier, entry);
        }
    }
    
    public Sprite createSprite(String name) {
        Animation animation = new Animation("default");
        SheetEntry entry = entries.get(name);
        if (entry == null)
            return null;
        animation.addFrame(new AnimationFrame(
                entry.identifier, texture, entry.width, entry.height, 
                entry.x, entry.y, 99999.0));

        Sprite sprite = new Sprite();
        sprite.animations.put("default", animation);
        sprite.active = animation;
        
        return sprite;
    }


    public Sprite createSprite(Animation... animations) {
        Sprite sprite = new Sprite();
        Animation active = null;
        for (Animation animation : animations) {
            Animation constructedAnim = new Animation(animation.name);
            for (AnimationFrame frame : animation.frames) {
                SheetEntry entry = entries.get(frame.frameName);
                if (entry == null) {
                    System.err.println("Bad sprite reference");
                    return null;
                }
                constructedAnim.addFrame(new AnimationFrame(
                        frame.frameName, texture, entry.width,
                        entry.height, entry.x, entry.y, frame.frameTime));                
            }
            if (active == null) {
                active = constructedAnim;
            }
            sprite.animations.put(animation.name, constructedAnim);
        }
        sprite.active = active;
        return sprite;
    }

}
