package edu.siren.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.game.Player;
import edu.siren.game.entity.Entity;
import edu.siren.renderer.BufferType;
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
    public Camera camera = new Camera(512.0f / 448.0f);
    public Shader shader;
    public ArrayList<Entity> entities = new ArrayList<Entity>();

    /**
     * Constructs a new world of a given width and height. Note that this
     * does not limit the size of the world, just defines an initial size.
     */
    public World(int width, int height) {
        try {
            layers = new TreeSet<Layer>();
            Layer layer = new Layer(BufferType.STATIC);
            layer.addTile(new Tile("res/tests/img/grass.png", -width/2, -height/2, width,
                    height));
            layers.add(layer);
            Random random = new Random();
            
            Keyboard.create();
            Mouse.create();
            
            for (int i = 0; i < 500; i++) {
                int x = random.nextInt(width) - random.nextInt(width*2);
                int y = random.nextInt(height) - random.nextInt(height*2);
                Tile tile = new Tile("res/tests/img/weeds.png", x, y,
                        13.0f, 7.0f, 1, 1);
                layer.addTile(tile);
            }

            for (int i = 0; i < 100; i++) {
                int x = random.nextInt(width) - random.nextInt(width*2);
                int y = random.nextInt(height) - random.nextInt(height*2);
                Tile tile = new Tile("res/tests/img/tree.png", x, y,
                        64.0f, 100.0f, 1, 1);
                layer.addTile(tile);
            }

            shader = new Shader("res/tests/glsl/basic.vert",
                    "res/tests/glsl/basic.frag");
            camera.position.m33 = 100.0f;
            camera.position.m30 = 0.0f;
            camera.bindToShader(shader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LWJGLException e) {
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
    
    /**
     * Adds an explicit player and binds the camera
     * so the player can control the camera interactions.
     */
    public void addEntity(Player player) {
        player.bindCamera(this.camera);
        player.setWorld(this);
        entities.add(player);
    }

}
