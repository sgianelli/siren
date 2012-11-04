package edu.siren.renderer;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

public class Gui {

    public Gui() {
        ortho(0, 640, 0, 480, -1, 1);
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

    public void updateShaders() {
        for (Shader shader : shaders) {
            shader.update("projection", projection);
        }
    }

    public void bindToShader(Shader shader) {
        shaders.add(shader);
        shader.use();
        shader.update("projection", projection);
        shader.release();
    }

    public Matrix4f projection = new Matrix4f();
    public ArrayList<Shader> shaders = new ArrayList<Shader>();
}
