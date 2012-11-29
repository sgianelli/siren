package edu.siren.core.tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import edu.siren.core.geom.Rectangle;
import edu.siren.core.sprite.Sprite;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Drawable;
import edu.siren.renderer.IndexVertexBuffer;

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
    public ArrayList<Tile> triggerTiles;
    protected ArrayList<Layer> children;
    protected HashMap<String, Tile> tileHashMap = null;
    protected boolean valid = false;
    private List<IndexVertexBuffer> rawivb = new ArrayList<IndexVertexBuffer>();
    private String name = null;
    public World world = null;
    public ArrayList<Rectangle> solids = new ArrayList<Rectangle>();
    private ArrayList<Tile> removeNext = new ArrayList<Tile>();

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
        this.triggerTiles = new ArrayList<Tile>();
    }

    public Layer() {
        this(BufferType.STATIC);
    }

    public Layer(String name) {
        this(BufferType.STATIC);
        this.name = name;        
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
            
            tile.parent = this;
            
            bounds.extend(tile.bounds);
            
            if (triggerable != null) {
                this.triggerTiles.add(triggerable);
            } else {
                this.tiles.add(tile);
            }
            
            // Bind the tile to a layer
            // And cache solid tiles for BBOX tests
            tile.layer = this;
            if (tile.solid) {
                solids.add(tile.bounds);
            }
        }        
    }

    // TODO(vanhornejb): Optimize it into a single pass
    /* (non-Javadoc)
     * @see edu.siren.renderer.Drawable#draw()
     */
    @Override
	public void draw() {
        
        for (Drawable tile : tiles) {
            tile.draw();
        }
                
        for (IndexVertexBuffer ivb : rawivb) {
            ivb.draw();
        }        
        
        for (Tile tile : removeNext) {
            triggerTiles.remove(tile);
            tiles.remove(tile);
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
    @Override
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
     * @param indexVertexBuffers Any drawable surface.
     */
    public void addIndexVertexBuffer(IndexVertexBuffer[] indexVertexBuffers) {
        for (IndexVertexBuffer drawable : indexVertexBuffers) {
            rawivb.add(drawable);
        }
    }

    public void extendHash(HashMap<String, Tile> tileHashMap) {   
        for (Drawable tile : tiles) {
            if (tile instanceof Tile) {
                Tile t = (Tile) tile;
                if (t.id != null) {
                    tileHashMap.put(t.id, t);
                }
            }
        }
    }

    public void addDrawable(Drawable drawable) {
        tiles.add(drawable);
    }
    
    public void remove(Tile what) {
        removeNext.add(what);
    }

    public void checkEvents() {
        for (Tile tile : triggerTiles) {
            tile.checkEvents(world);
        }
    }
}
