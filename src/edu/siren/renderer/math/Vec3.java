package edu.siren.renderer.math;

public class Vec3 {
    double v[] = new double[3];

    public Vec3(double x, double y, double z) {
        v[0] = x;
        v[1] = y;
        v[2] = z;
    }

    public static Vec3 normalize(Vec3 v) {
        return v.normalize();
    }

    public Vec3 normalize() {
        Vec3 out = this.clone();
        out.dividei(out.magnitude());
        return out;
    }

    private void dividei(double d) {
        v[0] /= d;
        v[1] /= d;
        v[2] /= d;
    }

    private double magnitude() {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    public Vec3 scale(double d) {
        Vec3 f = this.clone();
        f.v[0] *= d;
        f.v[1] *= d;
        f.v[2] *= d;
        return f;
    }

    public Vec3 cross(Vec3 y) {
        Vec3 x = this;
        return new Vec3(x.v[1] * y.v[2] - y.v[1] * x.v[2], x.v[2] * y.v[0]
                - y.v[2] * x.v[0], x.v[0] * y.v[1] - y.v[0] * x.v[1]);
    }

    public Vec3 clone() {
        Vec3 vec = new Vec3(v[0], v[1], v[2]);
        return vec;
    }

    public void addZ(float z) {
        v[1] += z;
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(v[0] + other.v[0], v[1] + other.v[1], v[2] + other.v[2]);
    }

    public void subi(Vec3 other) {
        v[0] -= other.v[0];
        v[1] -= other.v[1];
        v[2] -= other.v[2];
    }

    public void addi(Vec3 other) {
        v[0] += other.v[0];
        v[1] += other.v[1];
        v[2] += other.v[2];
    }

    public Vec3 sub(Vec3 other) {
        return new Vec3(v[0] - other.v[0], v[1] - other.v[1], v[2] - other.v[2]);
    }

    public double dot(Vec3 vy) {
        double[] y = vy.v;
        double[] x = v;
        return x[0] * y[0] + x[1] * y[1] + x[2] * y[2];
    }

}
