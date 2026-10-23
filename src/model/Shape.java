package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
* Common base class for every drawable object.
*
* The model stores shapes and their drawing properties. It does not know
* anything about Swing components, mouse events, or windows.
*/
public abstract class Shape {
    private Color color;
    private float strokeWidth;

    protected Shape(Color color, float strokeWidth) {
        if(color == null)
            throw new IllegalArgumentException("Color cannot be null.");

        if(strokeWidth <= 0)
            throw new IllegalArgumentException("Stroke width must be positive.");

        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public Color getColor() {
        return color;
    }

    // Used by ChangeColorCommand to apply and restore a shape's color.
    public void setColor(Color color) {
        if(color == null)
            throw new IllegalArgumentException("Color cannot be null.");

        this.color = color;
    }

    public float getStrokeWidth() {
        return getStrokeWidth();
    }

    // Draws the shape using its current color and line width.
    public final void draw(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(strokeWidth));
        drawShape(graphics);
    }

    // Subclasses implement their actual geometry here.
    protected abstract void drawShape(Graphics2D graphics);

    // Returns true if the supplied point is close enough to select this shape.
    public abstract boolean contains(Point point);

    // Moves this shape by the supplied horizontal and vertical distances.
    public abstract void moveBy(int dx, int dy);
}
