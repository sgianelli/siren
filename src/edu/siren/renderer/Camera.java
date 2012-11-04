package edu.siren.renderer;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

/**
 * The Camera object defines a world viewport. It must be bound to a shader
 * that define the `world` and `projection` uniforms. This allows the camera
 * to control how the displayed objects look. The default projection is 
 * defined by the orthographic perspective given by the aspect-ratio during
 * the Camera's construction
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 *
 */
public class Camera {

    /**
     * Constructs a new Camera with a given aspect ratio.
     *
     * @param aspectRatio The aspect ratio of the camera.
     */
    public Camera(float aspectRatio) {
        position.setIdentity();
        ortho(-2 * aspectRatio, 2 * aspectRatio, -2, 2, -1, 1);
        setZoom(100.0f);
    }

    /**
     * Constructs a new Camera with a given aspect ratio at an (x, y) with
     * a zoom of z.
     *
     * @param aspectRatio The aspect ratio of the camera
     * @param x The x-coordinate of the camera
     * @param y The y-coordinate of the camera
     * @param z The zoom-level of the camera
     */
    public Camera(float aspectRatio, float x, float y, float z) {
        this(aspectRatio);
        setPosition(x, y);
        setZoom(z);
    }

    /**
     * Zooms to `zoom`
     *
     * @param zoom The zoom value to zoom into.
     */
    void setZoom(float zoom) {
        position.m33 = zoom;
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
     * Zooms in one-factor.
     */
    public void zoomIn() {
        if (position.m33 <= 1)
            return;
        position.m33 -= 5.0;
        updateShaders();
    }

    /**
     * Zooms out one-factor.
     */
    public void zoomOut() {
        position.m33 += 5.0f;
        updateShaders();
    }

    /**
     * Moves the camera dx, dy. NOT to dx, dy.
     *
     * @param dx The delta-x movement
     * @param dy The delta-y movement
     * @return Whether a movement was made.
     */
    public boolean move(float dx, float dy) {
        if (dx == 0 && dy == 0)
            return false;

        position.m30 -= dx;
        position.m31 -= dy;
        updateShaders();
        return true;
    }

    /**
     * Sets the position of the camera in 2d-space.
     */
    public void setPosition(float x, float y) {
        position.m30 = x;
        position.m31 = y;
        updateShaders();
    }

    /**
     * Updates all the shaders this camera is bound to.
     */
    public void updateShaders() {
        for (Shader shader : shaders) {
            shader.update("world", position);
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
        shader.update("world", position);
        shader.update("projection", projection);
        shader.release();
    }

    public Matrix4f position = new Matrix4f();
    public Matrix4f projection = new Matrix4f();
    public ArrayList<Shader> shaders = new ArrayList<Shader>();
}
