import ui.PaintWindow;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Swing components should be created on the event dispatch thread.
        SwingUtilities.invokeLater(() -> {
            PaintWindow window = new PaintWindow();
            window.setVisible(true);
        });
    }
}