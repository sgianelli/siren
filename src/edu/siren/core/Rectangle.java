package edu.siren.core;

public class Rectangle extends java.awt.Rectangle {
    private static final long serialVersionUID = 5995669794424202731L;

    Rectangle(int x, int y) {
        super(x, y);
    }

    void extend(Rectangle other) {
        x = x > other.x ? other.x : x;
        y = y > other.y ? other.y : y;
        width = width < other.width ? other.width : width;
        height = height < other.height ? other.height : height;
    }
}
