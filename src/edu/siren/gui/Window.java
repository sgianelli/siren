package edu.siren.gui;

public class Window extends Element {
    public Window(String name) {
        this.state.name = "Window <" + name + ">";
        this.xywh(0, 0, 640, 480);
    }
    
    public void close() {
        this.state.clearChildren = true;
    }

    public void removeAll() {
        this.state.clearChildren = true;
    }
}
