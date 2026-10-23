package command;

import model.Drawing;
import model.Shape;

public class DeleteShapeCommand implements Command {
    private final Drawing drawing;
    private final Shape shape;
    private int originalIndex;

    public DeleteShapeCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
        this.originalIndex = drawing.indexOf(shape);
    }

    @Override
    public void execute() {
        originalIndex = drawing.indexOf(shape);
        if(originalIndex >= 0) {
            drawing.remove(shape);
        }
    }

    @Override
    public void undo() {
        if(originalIndex >= 0 && drawing.indexOf(shape) < 0) {
            drawing.add(originalIndex, shape);
        }
    }

    @Override 
    public String getName() {
        return "Delete shape";
    }
}
