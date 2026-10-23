package command;

import model.Drawing;
import model.Shape;

public class AddShapeCommand implements Command {
    private final Drawing drawing;
    private final Shape shape;
    private int insertionIndex;

    public AddShapeCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override 
    public void execute() {
        // Remember the location so undo/redo preserves stacking order.
        insertionIndex = drawing.size();
        drawing.add(shape);
    }

    @Override 
    public void undo() {
        insertionIndex = drawing.indexOf(shape);
        drawing.remove(shape);
    }

    @Override 
    public String getName() {
        return "Add shape";
    }
}
