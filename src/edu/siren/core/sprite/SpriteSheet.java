package edu.siren.core.sprite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.lwjgl.opengl.GL13;

import edu.siren.renderer.TexturePNG;

/**
 * @author Justin Van Horne
 * A sprite sheet defines a collection of sprites in a single image.
 */
public class SpriteSheet {
    public Map<String, SheetEntry> entries;
    public TexturePNG texture;
    
    /**
     * The CSS pattern is used to extract specific features from a very
     * specific style-sheet structure. This CSS stylesheet is generated
     * by the HTML5 application "Stitches".
     */
    public static final Pattern cssPattern = Pattern.compile(
            "(^\\.\\w+-([\\w-]+)-\\w+\\s\\{" + // Get class with pre/suf-fix
            "\\s+[\\w:]+\\s(\\d+)[;\\s\\w]+" + // Get the width
            "[\\w:]+\\s(\\d+)"               + // Get the height
            "[;\\s\\w-]+:\\s-(\\d+)"         + // Get the x-offset (w/o minus)
            "[\\w\\s]+-(\\d+)"               + // Get the y-offset (w/o minus)
            "[\\w;]+\\s+\\})+",                // Skip the rest of junk
            Pattern.MULTILINE | Pattern.DOTALL);

    
    public SpriteSheet() { }
    
    /**
     * Construct a new sprite sheet with a defined set of entries into
     * the sprite sheet. These entries define the x, y, w, h of the individual
     * sprites within the sheet.
     * 
     * @param spritesheet The sprite sheet to load.
     * @param sprites The various sprites within the sprite sheet
     * @throws IOException Throws if the sprite sheet is not found.
     */
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
    
    /**
     * Create a sprite from the spritesheet from a unique-key "name"
     * 
     * @param name The name of the sprite within the sprite sheet.
     * @return The found Sprite or null if not found.
     */
    public Sprite createSprite(String name) {
        Animation animation = new Animation("default");
        SheetEntry entry = entries.get(name);
        
        // Maybe we should have better error handling here
        if (entry == null) {
            System.err.println("Bad sprite: " + name);
            printAvailableSprites();
            return null;
        }
        
        // Add the new frame
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
        
        // Create a new sprite from a series of animations
        // This might be confusing, but we're actually using the Animation
        // class as a sort-of descriptor to generate a new Animation class
        // from it. This looks confusing here, but makes sense from a usage
        // standpoint (hopefully)
        for (Animation animation : animations) {            
            // Create the new constructed animation
            // Go through each of the frames in the animation being requested
            // and retrieve the frames from the sprite sheet
            Animation constructedAnim = new Animation(animation.name);
            for (AnimationFrame frame : animation.frames) {
                SheetEntry entry = entries.get(frame.frameName);
                
                // If something bad goes wrong, then print the sprites
                if (entry == null) {                    
                    System.err.println("Bad sprite: " + frame.frameName);
                    printAvailableSprites();
                    return null;
                }
                
                // Add a real frame to the constructed animation seqquence
                constructedAnim.addFrame(new AnimationFrame(
                        frame.frameName, texture, entry.width,
                        entry.height, entry.x, entry.y, frame.frameTime));                
            }
            
            // We need a default animation
            if (active == null) {
                active = constructedAnim;
            }
            
            // Add the animation to the new sprite
            sprite.animations.put(animation.name, constructedAnim);
        }
        
        // Set the active animation and return the new sprite
        sprite.active = active;
        return sprite;
    }

    /**
     * Helpful when you have no idea what is available.
     */
    public void printAvailableSprites() {
        Iterator<Entry<String, SheetEntry>> it = entries.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, SheetEntry> pairs = it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove();
        }
    }

    /**
     * Create a new sprite sheet from the HTML5-app CSS
     * 
     * @param spritesheet The image to use as a sprite sheet
     * @param cssfile The CSS that defines the sprites in the sprite sheet
     * @return A new SpriteSheet or null if something goes wrong
     * @throws IOException Thrown if the spritesheet is not found.
     */
    public static SpriteSheet fromCSS(String spritesheet, String cssfile) 
            throws IOException 
    {
        // Read in all the data
        FileInputStream stream = new FileInputStream(new File(cssfile));
        FileChannel fc = stream.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                fc.size());
        stream.close();
        String content = Charset.defaultCharset().decode(bb).toString();
        
        // Find all our CSS matches
        Matcher matcher = cssPattern.matcher(content);
        ArrayList<SheetEntry> entries = new ArrayList<SheetEntry>();
        while (matcher.find()) {
            String spriteName = matcher.group(2);
            int width = Integer.parseInt(matcher.group(3));
            int height = Integer.parseInt(matcher.group(4));
            int x = Integer.parseInt(matcher.group(5));
            int y = Integer.parseInt(matcher.group(6));
            entries.add(new SheetEntry(spriteName, width, height, x, y));
        }
        
        // Create a new sprite sheet from the matches
        SpriteSheet sheet = new SpriteSheet();
        sheet.add(spritesheet, entries);        
        return sheet;
        
    }

    /**
     * Acts as a utility method when the varargs constructor doesn't work.
     */
    public void add(String spritesheet, ArrayList<SheetEntry> sprites) 
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
}
