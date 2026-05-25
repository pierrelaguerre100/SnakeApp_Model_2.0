import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int boardWidth = 600;
            int boardHeight = 600;

            JFrame frame = new JFrame("Snake");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            MenuWindowPanel menu = new MenuWindowPanel(frame, boardWidth, boardHeight);

            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}