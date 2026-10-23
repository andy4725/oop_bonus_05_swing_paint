package ui;

import command.AddShapeCommand;
import command.CommandManager;
import command.DeleteShapeCommand;
import command.MoveShapeCommand;
import model.Drawing;
import model.EllipseShape;
import model.LineShape;
import model.RectangleShape;
import model.Shape;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Swing panel that displays a drawing and handles mouse-based editing.
public class DrawingCanvas extends JPanel {
    // The tool currently used to interpret mouse gestures.
    public enum Tool {
        SELECT,
        LINE,
        RECTANGLE,
        ELLIPSE
    }

    // Drawing model displayed by this canvas.
    private final Drawing drawing;

    // Manager that records drawing operations for undo and redo.
    private final CommandManager commandManager;

    // Current drawing tool and color.
    private Tool activeTool = Tool.SELECT;
    private Color currentColor = Color.BLACK;

    // Start point for a shape-drawing gesture.
    private Point dragStart;

    // Shape currently selected or being moved.
    private Shape selectedShape;

    // Start point and accumulated movement for a move gesture.
    private Point moveStart;
    private int draggedDx;
    private int draggedDy;

    // Configure the canvas and register mouse handlers.
    public DrawingCanvas(Drawing drawing, CommandManager commandManager) {
        this.drawing = drawing;
        this.commandManager = commandManager;

        // Use a white background and allow the panel to receive focus.
        setBackground(Color.WHITE);
        setFocusable(true);

        // Route mouse events to the canvas's gesture handlers.
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        };

        // Listen for clicks and drag gestures.
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    // Change tools and clear any current selection.
    public void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
        selectedShape = null;
        repaint();
    }

    // Return the active drawing tool.
    public Tool getActiveTool() {
        return activeTool;
    }

    // Set the color used for subsequently created shapes.
    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    // Return the currently selected shape, if any.
    public Shape getSelectedShape() {
        return selectedShape;
    }

    // Delete the selected shape and record the operation.
    public void deleteSelectedShape() {
        if (selectedShape != null) {
            commandManager.executeCommand(new DeleteShapeCommand(drawing, selectedShape));
            selectedShape = null;
            repaint();
        }
    }

    // Paint all shapes in the drawing.
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Use a copy so rendering settings do not affect Swing's graphics object.
        Graphics2D graphics2d = (Graphics2D) graphics.create();
        try {
            graphics2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Draw shapes in model order.
            for (Shape shape : drawing.getShapes()) {
                shape.draw(graphics2d);
            }
        } finally {
            // Release the copied graphics context.
            graphics2d.dispose();
        }
    }

    // Start either a selection/move gesture or a shape-drawing gesture.
    private void handleMousePressed(MouseEvent event) {
        Point point = event.getPoint();

        if (activeTool == Tool.SELECT) {
            // Select the topmost shape under the pointer and prepare to move it.
            selectedShape = drawing.findTopShapeAt(point);
            moveStart = point;
            draggedDx = 0;
            draggedDy = 0;
        } else {
            // Remember where the new shape gesture begins.
            dragStart = point;
        }

        repaint();
    }

    // Move the selected shape as the pointer is dragged.
    private void handleMouseDragged(MouseEvent event) {
        if (activeTool == Tool.SELECT && selectedShape != null && moveStart != null) {
            Point currentPoint = event.getPoint();

            // Apply only the movement since the previous drag event.
            int dx = currentPoint.x - moveStart.x - draggedDx;
            int dy = currentPoint.y - moveStart.y - draggedDy;

            selectedShape.moveBy(dx, dy);
            draggedDx += dx;
            draggedDy += dy;
            repaint();
        }
    }

    // Record the completed movement as one undoable command.
    private void finishMove() {
        if (selectedShape != null && (draggedDx != 0 || draggedDy != 0)) {
            // Restore the original position before executing the command,
            // because the command applies the movement and records its inverse.
            selectedShape.moveBy(-draggedDx, -draggedDy);

            commandManager.executeCommand(new MoveShapeCommand(selectedShape, draggedDx, draggedDy)
            );
        }

        // Clear movement state after the gesture ends.
        moveStart = null;
        draggedDx = 0;
        draggedDy = 0;
    }

    // Finish a move or create the selected type of shape.
    private void handleMouseReleased(MouseEvent event) {
        if (activeTool == Tool.SELECT) {
            finishMove();
            repaint();
            return;
        }

        // Ignore a release without a matching press.
        if (dragStart == null) {
            return;
        }

        Point end = event.getPoint();
        Shape shape = null;

        // Create the requested shape using the drag's start and end points.
        switch (activeTool) {
            case LINE:
                shape = new LineShape(
                        dragStart.x, dragStart.y,
                        end.x, end.y,
                        currentColor, 1.0f
                );
                break;

            case RECTANGLE:
            case ELLIPSE:
                int x = Math.min(dragStart.x, end.x);
                int y = Math.min(dragStart.y, end.y);
                int width = Math.abs(end.x - dragStart.x);
                int height = Math.abs(end.y - dragStart.y);

                if (activeTool == Tool.RECTANGLE) {
                    shape = new RectangleShape(
                            x, y, width, height, currentColor, 1.0f
                    );
                } else {
                    shape = new EllipseShape(
                            x, y, width, height, currentColor, 1.0f
                    );
                }
                break;

            default:
                break;
        }

        // Add the new shape to the drawing and undo history.
        if (shape != null) {
            commandManager.executeCommand(new AddShapeCommand(drawing, shape));
        }

        // Clear the drag state and refresh the canvas.
        dragStart = null;
        repaint();
    }
}