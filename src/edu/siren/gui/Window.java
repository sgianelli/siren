package edu.siren.gui;

public class Window extends Element {
    public Window(String name) {
        this.state.name = "Window <" + name + ">";
    }
    
    public void close() {
        this.state.clearChildren = true;
    }
}
