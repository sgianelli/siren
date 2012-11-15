package edu.siren.core.sprite;

public class SheetEntry {
    String identifier;
    int width, height, x, y;
    
    /**
     * A sheet entry acts an index to the sprite sheet.
     * This is a simple POD which is interpreted by the SpriteSheet class.
     * The (s, t) coordinates are generated from the values in the class.
     * 
     * @param identifier The name of the sprite
     * @param width The width of the sprite
     * @param height The height of the sprite
     * @param x The x-offset into the sprite sheet
     * @param y The y-offset into the sprite sheet.
     */
    public SheetEntry(String identifier, int width, int height, int x, int y) {
        this.identifier = identifier;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    @Override
	public String toString() {
        return "Sprite: <" + identifier + ", " + width + ", " + height + ", " + x + ", " + y + ">";
    }
}
