
## `README.md`

```markdown
# Simple Vector Paint

A small Swing drawing application demonstrating the Command design pattern.
It supports lines, rectangles, ellipses, selection, moving, deleting, color
changes, and undo/redo.

## Requirements

- Java 11 or newer
- JUnit 5 to run the tests

## Running

Compile the Java sources from the project root:

```sh
javac -d out src/App.java src/model/*.java src/command/*.java src/ui/*.java

java -cp out App
Controls
- Select: click an object to select it and drag to move it
- Line, Rectangle, Ellipse: choose a tool and drag on the canvas
- Choose color: changes the drawing color; if a shape is selected, changes
  that shape's color as an undoable operation
- Delete selected: removes the selected shape
- Ctrl+Z: undo
- Ctrl+Y: redo
Design
Drawing is the model and contains the ordered list of shapes. Shape is the
abstract base class for the concrete vector shapes. Each user action is
represented by a Command with execute() and undo() methods.
CommandManager stores commands in undo and redo stacks.
The model and commands do not depend on Swing. DrawingCanvas displays the
model and converts mouse interactions into commands.
See [UML.md](UML.md) for the class diagram.
Tests
The test suite uses JUnit 5 and tests the command manager and commands against
the model without starting the UI.