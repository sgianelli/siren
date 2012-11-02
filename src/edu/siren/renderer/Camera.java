package edu.siren.renderer;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

public class Camera {

    public Camera() {
        position.setIdentity();
    }

    public void zoomIn() {
        if (position.m33 <= 1)
            return;
        position.m33 -= 0.5;
        updateShaders();
    }

    public void zoomOut() {
        if (position.m33 >= 100.0)
            return;
        position.m33 += 0.5f;
        updateShaders();
    }

    public void move(float dx, float dy) {
        if (dx == 0 && dy == 0)
            return;

        position.m30 -= dx;
        position.m31 -= dy;
        updateShaders();
    }

    public void updateShaders() {
        for (Shader shader : shaders) {
            shader.update("world", position);
        }
    }

    public void bindToShader(Shader shader) {
        shaders.add(shader);
        shader.use();
        shader.update("world", position);
        shader.release();
        System.out.println("\n" + position);
    }

    public Matrix4f position = new Matrix4f();
    public ArrayList<Shader> shaders = new ArrayList<Shader>();
}
