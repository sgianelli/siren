package edu.siren.renderer;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

/**
 * A Perspective2D object is similar to a Camera in that it defines an 
 * othrographic perspective, except for the Perspective2D it defines a fixed 
 * 640x480 projection
 * suitable for drawing HUD-like components to the screen.
 *
 * @see edu.siren.renderer.Camera
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Perspective2D {

    /**
     * Constructs a new Gui object with a othographic perspective of
     * 640x480.
     */
    public Perspective2D() {
        ortho(0, 512, 0, 448, -1000, 1000);
    }

    /**
     * Constructs a new Gui object with a defined othographic perspective
     * outside of 640x480
     */
    public Perspective2D(float width, float height) {
        ortho(0, width, 0, height, -1000, 1000);
    }
    
    /**
     * Sets the orthographic projection of the camera.
     *
     * @param left The most-left view
     * @param right The most-right view
     * @param bottom The most-bottom view
     * @param top The most-top view
     * @param near The nearest the camera can see
     * @param far The farthest the camera can see
     */
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

   /**
     * Updates all the shaders this camera is bound to.
     */
    public void updateShaders() {
        for (Shader shader : shaders) {
            shader.update("projection", projection);
        }
    }

    /**
     * Binds the camera to a specific shader.
     *
     * @param shader The Shader object to bind to.
     */
    public void bindToShader(Shader shader) {
        shaders.add(shader);
        shader.use();
        shader.update("projection", projection);
        shader.release();
    }

    public Matrix4f projection = new Matrix4f();
    public ArrayList<Shader> shaders = new ArrayList<Shader>();    
}
