package command;

import java.awt.Color;
import model.Shape;

public class ChangeColorCommand implements Command {
    private final Shape shape;
    private final Color newColor;
    private Color oldColor;

    public ChangeColorCommand(Shape shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        oldColor = shape.getColor();
        shape.setColor(newColor);
    }

    @Override
    public void undo() {
        shape.setColor(oldColor);
    }

    @Override 
    public String getName() {
        return "Change color";
    }
}
