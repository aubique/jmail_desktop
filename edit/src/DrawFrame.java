import javax.swing.*;

class DrawFrame extends JFrame {

    public DrawFrame(String title, JPanel panelToDraw) {
        super(title);
        setContentPane(panelToDraw);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
