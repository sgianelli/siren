package edu.siren.core.tile;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.siren.core.geom.Rectangle;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Drawable;

/**
 * Abstracts away a priority drawing by defining the concept of a Layer.
 * Layers can be stacked and added as children.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Layer implements Comparable<Layer>, Drawable {
    protected int priority;
    protected int depth;
    protected BufferType type;
    protected Layer parent;
    protected Rectangle bounds;
    protected ArrayList<Drawable> tiles;
    protected ArrayList<TriggerTile> triggerTiles;
    protected ArrayList<Layer> children;
    protected boolean valid = false;

    /**
     * Construct a new basic layer.
     */
    public Layer(BufferType type) {
        children = new ArrayList<Layer>();
        bounds = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);
        parent = null;
        depth = 0;
        priority = 0;
        this.type = type;
        this.tiles = new ArrayList<Drawable>();
        this.triggerTiles = new ArrayList<TriggerTile>();
    }

    public Layer() {
        this(BufferType.STATIC);
    }

    /**
     * Add a collection of tiles to the layer.
     * @param tiles Adds an array of Tiles to the layer.
     */
    public void addTile(Tile... tiles) {
        for (Tile tile : tiles) {
            TriggerTile triggerable = null;
            if (tile instanceof TriggerTile) {
                triggerable = (TriggerTile) tile;
            }
            
            bounds.extend(tile.bounds);
            
            if (triggerable != null) {
                this.triggerTiles.add(triggerable);
            } else {
                this.tiles.add(tile);
            }
        }        
    }

    // TODO(vanhornejb): Optimize it into a single pass
    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    public void draw() {
        for (Drawable tile : tiles) {
            tile.draw();
        }
        
        for (TriggerTile tile : triggerTiles) {
            tile.draw();
        }
    }

    /**
     * Adds a new Layer as a child-layer. Drawn after drawables.
     *
     * @param layer The layer to add
     * @return Whether this children was added or exists in the Layer set.
     */
    public boolean addLayer(Layer layer) {
        bounds.extend(layer.bounds);
        layer.parent = this;
        layer.depth = depth + 1;
        return children.add(layer);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
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
    
    /**
     * Adds a Drawable surface to the Layer.
     *
     * @param drawables Any drawable surface.
     */
    public void addIndexVertexBuffer(Drawable[] drawables) {
        for (Drawable drawable : drawables) {
            tiles.add(drawable);
        }
    }
}
