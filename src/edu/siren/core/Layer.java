package edu.siren.core;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.siren.renderer.Drawable;

public class Layer implements Comparable<Layer>, Drawable {
    protected int priority;
    protected int depth;
    protected Layer parent;
    protected Rectangle bounds;
    protected ArrayList<Drawable> tiles;
    protected Set<Layer> children;

    public Layer() {
        children = new TreeSet<Layer>();
        bounds = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);
        parent = null;
        depth = 0;
        priority = 0;
        this.tiles = new ArrayList<Drawable>();
    }

    public void addTile(Tile... tiles) {
        for (Tile tile : tiles) {
            bounds.extend(tile.bounds);
            this.tiles.add(tile);
        }
    }

    // TODO(vanhornejb): Optimize it into a single pass
    public void draw() {
        for (Drawable tile : tiles) {
            tile.draw();
        }
    }

    public boolean addLayer(Layer layer) {
        bounds.extend(layer.bounds);
        layer.parent = this;
        layer.depth = depth + 1;
        return children.add(layer);
    }

    public int compareTo(Layer layer) {
        if (depth > layer.depth) {
            return 1;
        } else if (depth < layer.depth) {
            return -1;
        } else {
            if (priority > layer.priority) {
                return 1;
            } else if (priority < layer.priority) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void addIndexVertexBuffer(Drawable[] drawables) {
        for (Drawable drawable : drawables) {
            tiles.add(drawable);
        }
    }
}
