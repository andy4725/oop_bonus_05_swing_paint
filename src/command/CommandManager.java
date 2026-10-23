package command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void executeCommand(Command command) {
        if(command == null)
            throw new IllegalArgumentException("Command cannot be null.");

        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if(!canUndo()) return;

        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo() {
        if(!canRedo()) return;

        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }

    public String getUndoName() {
        return canUndo() ? undoStack.peek().getName() : null;
    }

    public String getRedoName() {
        return canRedo() ? redoStack.peek().getName() : null;
    }
}
