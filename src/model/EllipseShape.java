package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class EllipseShape extends Shape {
    private int x;
    private int y;
    private int width;
    private int height;

    public EllipseShape(int x, int y, int width, int height, Color color, float strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawShape(Graphics2D graphics) {
        graphics.drawOval(x, y, width, height);
    }

    @Override 
    public boolean contains(Point point) {
        // Normalize the point relative to the ellipse center. Points on or
        // inside the ellipse satisfy nx^2 + ny^2 <= 1.
        double radiusX = Math.max(1, Math.abs(width) / 2.0);
        double radiusY = Math.max(1, Math.abs(height) / 2.0);
        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;

        double normalizedX = (point.x - centerX) / radiusX;
        double normalizedY = (point.y - centerY) / radiusY;
        return normalizedX * normalizedX + normalizedY * normalizedY <= 1.0;
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
