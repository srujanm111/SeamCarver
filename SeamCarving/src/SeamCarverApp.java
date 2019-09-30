import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SeamCarverApp extends JFrame implements ActionListener {

    SeamCarver sc;
    boolean imageLoaded;

    JTextField filename;


    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            // remove seams or add seams
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
