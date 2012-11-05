package edu.siren.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.siren.game.entity.Entity;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Shader;

/**
 * A World object wraps the basics of the engine to create a simple way to
 * start an instance of the engine with predefined attributes for the camera,
 * as well as a collection of base shaders and world terrain data.
 *
 * @author Justin Van Horne
 */
public class World {
    private Set<Layer> layers;
    public Camera camera = new Camera(640.0f / 480.0f);
    public Shader shader;
    public ArrayList<Entity> entities = new ArrayList<Entity>();

    /**
     * Constructs a new world of a given width and height. Note that this
     * does not limit the size of the world, just defines an initial size.
     */
    public World(int width, int height) {
        try {
            layers = new TreeSet<Layer>();
            Layer layer = new Layer();
            layer.addTile(new Tile("res/tests/img/grass.png", 0, 0, width,
                    height));
            layers.add(layer);
            shader = new Shader("res/tests/glsl/basic.vert",
                    "res/tests/glsl/basic.frag");
            camera.position.m33 = 200.0f;
            camera.bindToShader(shader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the layers, followed by the entities, and then the Hud
     */
    public void draw() {
        for (Layer layer : layers) {
            layer.draw();
        }
        for (Entity entity : entities) {
            entity.draw();
        }
    }

    /**
     * Adds a new layer to the world.
     * 
     * @return Returns false if the layer already exists.
     */
    boolean addLayer(Layer layer) {
        return layers.add(layer);
    }

    /**
     * Adds a new entity to the world.
     */
    public void addEntity(Entity entity) {
        entity.setWorld(this);
        entities.add(entity);
    }

}
