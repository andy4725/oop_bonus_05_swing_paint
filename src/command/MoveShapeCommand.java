package command;

import model.Shape;

public class MoveShapeCommand implements Command {
    private final Shape shape;
    private final int dx;
    private final int dy;

    public MoveShapeCommand(Shape shape, int dx, int dy) {
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;
    }

    @Override 
    public void execute() {
        shape.moveBy(dx, dy);
    }

    @Override 
    public void undo() {
        shape.moveBy(-dx, -dy);
    }

    @Override 
    public String getName() {
        return "Move shape";
    }
}
