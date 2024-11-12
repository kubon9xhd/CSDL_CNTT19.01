import view.StudentFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StudentFrame frame = new StudentFrame();
                frame.setVisible(true);
            }
        });
    }
}