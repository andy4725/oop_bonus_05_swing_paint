package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the shapes in a drawing in painting order. Shapes added later appear
 * on top of shapes added earlier.
 */
public class Drawing {
    private final List<Shape> shapes = new ArrayList<>();

    /** Adds a non-null shape to the top of the drawing. */
    public void add(Shape shape) {
        if(shape == null)
            throw new IllegalArgumentException("Shape cannot be null.");

        shapes.add(shape);
    }

    /** Removes the first matching shape and reports whether it was present. */
    public boolean remove(Shape shape) {
        return shapes.remove(shape);
    }

    /** Returns the shape's index, or {@code -1} if it is not in the drawing. */
    public int indexOf(Shape shape) {
        return shapes.indexOf(shape);
    }

    /** Returns the number of shapes in the drawing. */
    public int size() {
        return shapes.size();
    }

    /** Returns the shape at the given index in painting order. */
    public Shape get(int index) {
        return shapes.get(index);
    }

    /**
     * Returns an unmodifiable view of the shapes in painting order. Changes
     * made to this drawing are reflected in the returned view.
     */
    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    /**
     * Finds the topmost shape containing the point, or {@code null} if none
     * does. Shapes are checked from the end of the painting order backwards.
     */
    public Shape findTopShapeAt(java.awt.Point point) {
        for(int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if(shape.contains(point)) {
                return shape;
            }
        }

        return null;
    }
}
