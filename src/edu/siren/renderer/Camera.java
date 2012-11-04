package edu.siren.renderer;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

public class Camera {

    public Camera(float ar) {
        position.setIdentity();
        ortho(-2 * ar, 2 * ar, -2, 2, -1, 1);
        position.m33 = 1000.0f;
    }

    public void ortho(float left, float right, float bottom, float top,
            float near, float far) {
        projection.setIdentity();

        projection.m00 = 2.0f / (right - left);
        projection.m11 = 2.0f / (top - bottom);
        projection.m22 = -2.0f / (far - near);
        projection.m30 = -(right + left) / (right - left);
        projection.m31 = -(top + bottom) / (top - bottom);
        projection.m32 = -(far + near) / (far - near);
    }

    public void zoomIn() {
        if (position.m33 <= 1)
            return;
        position.m33 -= 5.0;
        updateShaders();
    }

    public void zoomOut() {
        position.m33 += 5.0f;
        updateShaders();
    }

    public boolean move(float dx, float dy) {
        if (dx == 0 && dy == 0)
            return false;

        position.m30 -= dx;
        position.m31 -= dy;
        updateShaders();
        return true;
    }

    public void updateShaders() {
        for (Shader shader : shaders) {
            shader.update("world", position);
            shader.update("projection", projection);
        }
    }

    public void bindToShader(Shader shader) {
        shaders.add(shader);
        shader.use();
        shader.update("world", position);
        shader.update("projection", projection);
        shader.release();
    }

    public Matrix4f position = new Matrix4f();
    public Matrix4f projection = new Matrix4f();
    public ArrayList<Shader> shaders = new ArrayList<Shader>();
}
