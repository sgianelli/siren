package edu.siren.renderer;

import edu.siren.renderer.math.Mat4x4;
import edu.siren.renderer.math.Vec3;

public class Camera {

    public class State {
        public float fov;
        public float aspect_ratio;
        public float theta;
        public float phi;
        public float roll;
    };

    public Camera() {
        state.theta = state.phi = 0.0f;
        state.fov = 45.0f;
        state.aspect_ratio = 480.0f / 640.0f;
        state.roll = 0.0f;
        up = new Vec3(0.0f, 0.0f, -1.0f);
        position = new Vec3(0.0f, 0.0f, 0.0f);
        update_perspective();

        // Initialize basic models
        model = new Mat4x4();
        model.rotatei(0.0f, new Vec3(0.0f, 0.0f, 1.0f));

        // Set our initial position and look somewhere
        position = new Vec3(0.0f, 0.0f, 0.0f);
        look(0.0, 0.0);
    }

    public void move(float x, float y, float z) {
        position = new Vec3(x, y, z);
    }

    public void look(double d, double e) {
        state.theta += e;
        state.phi += d;

        float right_vec_angle = 3.14f / 2.0f;
        direction = new Vec3(Math.cos(state.phi) * Math.sin(state.theta),
                Math.sin(state.phi), Math.cos(state.phi)
                        * Math.cos(state.theta));

        right = new Vec3(Math.sin(state.theta - right_vec_angle), 0.0f,
                Math.cos(state.theta - right_vec_angle));

        up = direction.cross(right);
        Mat4x4 mat = new Mat4x4(1.0f);
        view = mat.rotate(-state.roll * 90.0f, new Vec3(0.0f, 1.0f, 1.0f));

        Vec3 p = position.clone();
        p.addZ(state.roll);

        view.multi(Mat4x4.lookAt(p, p.add(direction), up));
    }

    public void roll(float angle) {
        state.roll -= angle / 90.0f;
        if (state.roll > 0.5f)
            state.roll = 0.5f;
        else if (state.roll < -0.5f)
            state.roll = -0.5f;
    }

    public void fov(float degrees) {
        state.fov = degrees;
    }

    public void aspect_ratio(float ratio) {
        state.aspect_ratio = ratio;
    }

    public void update_perspective() {
        projection = Mat4x4.perspective(state.fov, state.aspect_ratio, 0.1f,
                1000.0f);
    }

    public void move_right() {
        position.subi(right.scale(0.1f));
    }

    public void move_left() {
        position.addi(right.scale(0.1f));
    }

    public void move_forward() {
        position.addi(direction.scale(0.1f));
    }

    public void move_backward() {
        position.subi(direction.scale(0.1f));
    }

    // shit, shit, and more shit
    public State state;
    public Vec3 position;
    public Vec3 direction;
    public Vec3 up;
    public Vec3 right;
    public Mat4x4 model;
    public Mat4x4 view;
    public Mat4x4 projection;
}
