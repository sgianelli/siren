package edu.siren.core;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Layer implements Comparable<Layer> {
    protected int priority;
    protected int depth;
    protected Layer parent;
    protected Rectangle bounds;
    protected ArrayList<Tile> tiles;
    protected Set<Layer> children;

    Layer() {
        children = new TreeSet<Layer>();
        bounds = new Rectangle(0, 0);
        parent = null;
        depth = 0;
        priority = 0;
    }

    void addTile(Tile tile) {
        bounds.extend(tile.bounds);
        tiles.add(tile);
    }

    boolean addLayer(Layer layer) {
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
}
