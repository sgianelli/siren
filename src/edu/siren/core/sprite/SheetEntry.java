package edu.siren.core.sprite;

public class SheetEntry {
    String identifier;
    int width, height, x, y;
    public SheetEntry(String identifier, int width, int height, int x, int y) {
        this.identifier = identifier;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return "Sprite: <" + identifier + ", " + width + ", " + height + ", " + x + ", " + y + ">";
    }
}
