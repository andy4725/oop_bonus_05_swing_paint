package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class RectangleShape extends Shape {
    // Stores the rectangle's top-left x-coordinate.
    private int x;

    // Stores the rectangle's top-left y-coordinate.
    private int y;

    // Stores the rectangle's width.
    private int width;

    // Stores the rectangle's height.
    private int height;

    // Creates a rectangle with the given position, size, color, and stroke width.
    public RectangleShape(int x, int y, int width, int height, Color color, float strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Draws the rectangle outline using the supplied graphics context.
    @Override
    protected void drawShape(Graphics2D graphics) {
        graphics.drawRect(x, y, width, height);
    }

    // Returns whether the point falls within a slightly expanded hit area.
    @Override
    public boolean contains(Point point) {
        Rectangle hitArea = new Rectangle(
            x - 4,
            y - 4,
            Math.max(1, width + 8),
            Math.max(1, height + 8));

        return hitArea.contains(point);
    }

    // Moves the rectangle by the given horizontal and vertical offsets.
    @Override 
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}