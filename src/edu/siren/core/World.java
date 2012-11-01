package edu.siren.core;

import java.util.Set;
import java.util.TreeSet;

public class World {
    Set<Layer> layers;

    World() {
        layers = new TreeSet<Layer>();
    }

    boolean addLayer(Layer layer) {
        return layers.add(layer);
    }

}
