package edu.siren.core;

public class Rectangle {
    float x, y, width, height;

    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public float left() {
        return x;
    }

    public float right() {
        return x + width;
    }

    public float top() {
        return y;
    }

    public float bottom() {
        return y + height;
    }

    void extend(Rectangle other) {
        x = x > other.x ? other.x : x;
        y = y > other.y ? other.y : y;
        width = width < other.width ? other.width : width;
        height = height < other.height ? other.height : height;
    }
}
