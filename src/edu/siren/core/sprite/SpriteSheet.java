package edu.siren.core.sprite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL13;

import edu.siren.renderer.TexturePNG;

public class SpriteSheet {
    public Map<String, SheetEntry> entries;
    public TexturePNG texture;
    public static final Pattern pattern = Pattern.compile("(^\\.\\w+-([\\w-]+)-\\w+\\s\\{\\s+[\\w:]+\\s(\\d+)[;\\s\\w]+[\\w:]+\\s(\\d+)[;\\s\\w-]+:\\s-(\\d+)[\\w\\s]+-(\\d+)[\\w;]+\\s+\\})+", Pattern.MULTILINE | Pattern.DOTALL);

    
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

    public static SpriteSheet fromCSS(String spritesheet, String cssfile) 
            throws IOException 
    {
        FileInputStream stream = new FileInputStream(new File(cssfile));
        FileChannel fc = stream.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                fc.size());
        stream.close();
        String content = Charset.defaultCharset().decode(bb).toString();
        
        // Find matches
        Matcher matcher = pattern.matcher(content);
        ArrayList<SheetEntry> entries = new ArrayList<SheetEntry>();
        while (matcher.find()) {
            String spriteName = matcher.group(2);
            int width = Integer.parseInt(matcher.group(3));
            int height = Integer.parseInt(matcher.group(4));
            int x = Integer.parseInt(matcher.group(5));
            int y = Integer.parseInt(matcher.group(6));
            entries.add(new SheetEntry(spriteName, width, height, x, y));
        }
        
        SpriteSheet sheet = new SpriteSheet();
        sheet.add(spritesheet, entries);
        
        return sheet;
        
    }

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
