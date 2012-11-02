package edu.siren.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.siren.game.entity.Entity;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Shader;

public class World {
    private Set<Layer> layers;
    public Camera camera = new Camera(640.0f / 480.0f);
    public Shader shader;
    public ArrayList<Entity> entities = new ArrayList<Entity>();

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

    public void draw() {
        for (Layer layer : layers) {
            layer.draw();
        }
        for (Entity entity : entities) {
            entity.draw();
        }
    }

    boolean addLayer(Layer layer) {
        return layers.add(layer);
    }

    public void addEntity(Entity entitiy) {
    }

}
