package command;

import model.Drawing;
import model.RectangleShape;
import model.Shape;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Model-only tests for undo/redo behavior.
 */
class CommandManagerTest {
    private RectangleShape rectangle() {
        return new RectangleShape(10, 20, 100, 50, Color.BLACK, 2.0f);
    }

    @Test
    void addCommandCanBeUndoneAndRedone() {
        Drawing drawing = new Drawing();
        CommandManager manager = new CommandManager();
        Shape shape = rectangle();

        manager.executeCommand(new AddShapeCommand(drawing, shape));
        assertEquals(1, drawing.size());

        manager.undo();
        assertEquals(0, drawing.size());

        manager.redo();
        assertEquals(1, drawing.size());
        assertSame(shape, drawing.get(0));
    }

    @Test
    void deleteCommandCanBeUndoneAndRestoresOrder() {
        Drawing drawing = new Drawing();
        CommandManager manager = new CommandManager();
        Shape first = rectangle();
        Shape second = rectangle();
        drawing.add(first);
        drawing.add(second);

        manager.executeCommand(new DeleteShapeCommand(drawing, first));
        assertEquals(1, drawing.size());
        assertSame(second, drawing.get(0));

        manager.undo();
        assertEquals(2, drawing.size());
        assertSame(first, drawing.get(0));
        assertSame(second, drawing.get(1));
    }

    @Test
    void moveCommandCanBeUndoneAndRedone() {
        Drawing drawing = new Drawing();
        CommandManager manager = new CommandManager();
        RectangleShape shape = rectangle();
        drawing.add(shape);

        manager.executeCommand(new MoveShapeCommand(shape, 15, -5));
        assertTrue(shape.contains(new java.awt.Point(30, 35)));

        manager.undo();
        assertTrue(shape.contains(new java.awt.Point(20, 35)));

        manager.redo();
        assertTrue(shape.contains(new java.awt.Point(30, 35)));
    }

    @Test
    void colorCommandCanBeUndoneAndRedone() {
        Drawing drawing = new Drawing();
        CommandManager manager = new CommandManager();
        RectangleShape shape = rectangle();
        drawing.add(shape);

        manager.executeCommand(new ChangeColorCommand(shape, Color.RED));
        assertEquals(Color.RED, shape.getColor());

        manager.undo();
        assertEquals(Color.BLACK, shape.getColor());

        manager.redo();
        assertEquals(Color.RED, shape.getColor());
    }

    @Test
    void executingNewCommandClearsRedoHistory() {
        Drawing drawing = new Drawing();
        CommandManager manager = new CommandManager();
        RectangleShape shape = rectangle();
        drawing.add(shape);

        manager.executeCommand(new MoveShapeCommand(shape, 10, 0));
        manager.undo();
        assertTrue(manager.canRedo());

        manager.executeCommand(new MoveShapeCommand(shape, 0, 10));
        assertFalse(manager.canRedo());
    }

    @Test
    void undoAndRedoOnEmptyStacksAreSafe() {
        CommandManager manager = new CommandManager();

        assertDoesNotThrow(manager::undo);
        assertDoesNotThrow(manager::redo);
        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());
    }
}