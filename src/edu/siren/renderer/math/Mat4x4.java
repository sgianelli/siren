package edu.siren.renderer.math;

public final class Mat4x4 {
    public double m[][] = new double[4][4];

    public Mat4x4(float f) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = f;
            }
        }
    }

    public Mat4x4() {
    }

    public void rotatei(float angle, Vec3 v) {
        double a = Math.toRadians(angle);
        double c = Math.cos(a);
        double s = Math.sin(a);

        Vec3 axisvec = Vec3.normalize(v);
        double[] axis = axisvec.v;
        double[] temp = axisvec.scale(1.0f - c).v;
        double[][] r = new double[4][4];

        r[0][0] = c + temp[0] * axis[0];
        r[0][1] = 0 + temp[0] * axis[1] + s * axis[2];
        r[0][2] = 0 + temp[0] * axis[2] - s * axis[1];

        r[1][0] = 0 + temp[1] * axis[0] - s * axis[2];
        r[1][1] = c + temp[1] * axis[1];
        r[1][2] = 0 + temp[1] * axis[2] + s * axis[0];

        r[2][0] = 0 + temp[2] * axis[0] + s * axis[1];
        r[2][1] = 0 + temp[2] * axis[1] - s * axis[0];
        r[2][2] = c + temp[2] * axis[2];

        set(0, scale(0, r[0][0]).add(scale(1, r[0][1]).add(scale(2, r[0][2]))));
        set(1, scale(0, r[1][0]).add(scale(1, r[1][1]).add(scale(2, r[1][2]))));
        set(2, scale(0, r[2][0]).add(scale(1, r[2][1]).add(scale(2, r[2][2]))));
        set(3, m[3]);
    }

    public Mat4x4 rotate(float angle, Vec3 v) {
        Mat4x4 other = this.clone();
        other.rotatei(angle, v);
        return other;
    }

    public void set(int row, double[] data) {
        m[row] = data;
    }

    public void set(int row, Mat4x4 add) {
        m[row] = add.m[row];
    }

    public void addi(int row, double value) {
        m[row][0] += value;
        m[row][1] += value;
        m[row][2] += value;
    }

    public Mat4x4 add(int row, double value) {
        Mat4x4 other = this.clone();
        other.addi(row, value);
        return other;
    }

    public void addi(Mat4x4 o) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] += o.m[i][j];
            }
        }
    }

    public Mat4x4 add(Mat4x4 m) {
        Mat4x4 result = this.clone();
        result.addi(m);
        return result;
    }

    public void scalei(int row, double d) {
        m[row][0] *= d;
        m[row][1] *= d;
        m[row][2] *= d;
    }

    public Mat4x4 scale(int i, double d) {
        Mat4x4 result = this.clone();
        result.scalei(i, d);
        return result;
    }

    public Mat4x4 clone() {
        Mat4x4 other = new Mat4x4();
        other.m = this.m.clone();
        return other;
    }

    public void multi(Mat4x4 other) {
        Mat4x4 out = new Mat4x4();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    out.m[x][y] = m[x][z] * other.m[z][y];
                }
            }
        }
        m = out.m;
    }

    public static Mat4x4 lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        Vec3 f = Vec3.normalize(center.sub(eye));
        Vec3 u = Vec3.normalize(up);
        Vec3 s = Vec3.normalize(f.cross(u));

        u = s.cross(f);

        Mat4x4 result = new Mat4x4(1.0f);
        result.m[0][0] = s.v[0];
        result.m[1][0] = s.v[1];
        result.m[2][0] = s.v[2];
        result.m[0][1] = u.v[0];
        result.m[1][1] = u.v[1];
        result.m[2][1] = u.v[2];
        result.m[0][2] = -f.v[0];
        result.m[1][2] = -f.v[1];
        result.m[2][2] = -f.v[2];
        result.m[3][0] = -s.dot(eye);
        result.m[3][1] = -u.dot(eye);
        result.m[3][2] = f.dot(eye);

        return result;
    }

    public static Mat4x4 perspective(float fovy, float aspect, float znear,
            float zfar) {
        double range = Math.tan(Math.toRadians(fovy / 2.0)) * znear;
        double left = -range * aspect;
        double right = range * aspect;
        double bottom = -range;
        double top = range;

        Mat4x4 result = new Mat4x4();
        result.m[0][0] = (2.0 * znear) / (right - left);
        result.m[1][1] = (2.0 * znear) / (top - bottom);
        result.m[2][2] = -(zfar + znear) / (zfar - znear);
        result.m[2][3] = -1.0f;
        result.m[3][2] = -(2.0f * zfar * znear) / (zfar - znear);

        return result;
    }
}
