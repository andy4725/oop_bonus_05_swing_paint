package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * A drawable line segment that can be selected and moved.
 */
public class LineShape extends Shape {
    // Coordinates of the line's start and end points.
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Creates a line with the given endpoints, color, and stroke width.
     */
    public LineShape(int x1, int y1, int x2, int y2, Color color, float strokeWidth) {
        super(color, strokeWidth);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /** Draws the line between its two endpoints. */
    @Override
    protected void drawShape(Graphics2D graphics) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    /**
     * Returns whether the given point is close enough to the line to count as a hit.
     * The five-pixel tolerance makes this zero-area shape easier to select.
     */
    @Override
    public boolean contains(Point point) {
        return distanceFromPointToSegment(point.x, point.y) <= 5.0;
    }

    /**
     * Calculates the shortest distance from a point to this line segment.
     * The projection is clamped to the segment so points beyond either endpoint
     * are measured against that endpoint.
     */
    private double distanceFromPointToSegment(double px, double py) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        // The squared segment length avoids taking a square root for this check.
        double segmentLengthSquared = dx * dx + dy * dy;

        // A zero-length segment is a single point.
        if (segmentLengthSquared == 0) {
            return Point.distance(px, py, x1, y1);
        }

        // Find the point's projection onto the segment, then limit it to [0, 1].
        double projection = ((px - x1) * dx + (py - y1) * dy) / segmentLengthSquared;
        projection = Math.max(0, Math.min(1, projection));

        // Calculate the closest point on the segment and its distance from the input point.
        double nearestX = x1 + projection * dx;
        double nearestY = y1 + projection * dy;
        return Point.distance(px, py, nearestX, nearestY);
    }

    /** Translates both endpoints by the given horizontal and vertical offsets. */
    @Override
    public void moveBy(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
}