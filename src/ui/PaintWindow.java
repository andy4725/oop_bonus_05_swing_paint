package ui;

import command.ChangeColorCommand;
import command.CommandManager;
import command.DeleteShapeCommand;
import model.Drawing;
import model.Shape;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

public class PaintWindow extends JFrame {
    private final Drawing drawing = new Drawing();
    private final CommandManager commandManager = new CommandManager();
    private final DrawingCanvas canvas = new DrawingCanvas(drawing, commandManager);

    public PaintWindow() {
        super("Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createToolbar(), BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        installUndoRedoShortcuts();
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JComboBox<DrawingCanvas.Tool> toolSelector = new JComboBox<>(DrawingCanvas.Tool.values());
        toolSelector.addActionListener(event ->
                canvas.setActiveTool((DrawingCanvas.Tool)
                        toolSelector.getSelectedItem()));
        toolBar.add(toolSelector);

        JButton colorButton = new JButton("Choose color");
        colorButton.addActionListener(event -> chooseDrawingColor());
        toolBar.add(colorButton);

        JButton deleteButton = new JButton("Delete selected");
        deleteButton.addActionListener(event -> deleteSelectedShape());
        toolBar.add(deleteButton);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(event -> {
            commandManager.undo();
            canvas.repaint();
        });
        toolBar.add(undoButton);

        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(event -> {
            commandManager.redo();
            canvas.repaint();
        });
        toolBar.add(redoButton);

        return toolBar;
    }

    private void chooseDrawingColor() {
        Color selectedColor = JColorChooser.showDialog(this, "Choose drawing color", Color.BLACK);
        if(selectedColor == null) return;

        canvas.setCurrentColor(selectedColor);
        Shape selectedShape = canvas.getSelectedShape();

        if(selectedShape != null) {
            commandManager.executeCommand(new ChangeColorCommand(selectedShape, selectedColor));
            canvas.repaint();
        }
    }

    private void deleteSelectedShape() {
        Shape selectedShape = canvas.getSelectedShape();

        if (selectedShape != null) {
            commandManager.executeCommand(new DeleteShapeCommand(drawing, selectedShape));
            canvas.deleteSelectedShape(); // If DrawingCanvas provides this method
            canvas.repaint();
        }
    }

    private void installUndoRedoShortcuts() {
        String undoActionKey = "undo";
        String redoActionKey = "redo";

        getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("control Z"), undoActionKey);

        getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("control Y"), redoActionKey);

        getRootPane().getActionMap().put(undoActionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                commandManager.undo();
                canvas.repaint();
            }
        });

        getRootPane().getActionMap().put(redoActionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                commandManager.redo();
                canvas.repaint();
            }
        });
    }
}
