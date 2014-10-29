// For week 8
// sestoft@itu.dk * 2014-10-12

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class TestButtonBlinkGui {
    private static boolean showBar = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Random random = new Random();
            final JFrame frame = new JFrame("TestButtonBlinkGui");
            final JPanel panel = new JPanel() {
                public void paint(Graphics g) {
                    super.paint(g);
                    if (showBar) {
                        g.setColor(Color.RED);
                        g.fillRect(0, 0, 10, getHeight());
                    }
                }
            };
            final JButton button = new JButton("Press here");
            frame.add(panel);
            panel.add(button);
            button.addActionListener((e) -> panel.setBackground(new Color(random.nextInt())));
            frame.pack();
            frame.setVisible(true);
        });

        

        while (true) {
            try {
                Thread.sleep(800);
            } // milliseconds
            catch (InterruptedException exn) {}
            showBar = !showBar;
            panel.repaint();
        }
    }
}

