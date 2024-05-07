import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class Main {

    JFrame frame = new JFrame();
    Paint paintPanel;

    int width = 1, height = 1;

    public Main() {
        frame.setSize(340,360);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        paintPanel = new Paint(getPanelSize(frame));
        frame.add(paintPanel);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {

                if(getPanelSize(frame).height < 20){
                    return;
                }   

                paintPanel.setSize(getPanelSize(frame));

                width  = paintPanel.getWidth();
                height = paintPanel.getHeight();

            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main();
            }
        });
    }

    public static Dimension getPanelSize(JFrame window){
        return new Dimension(window.getWidth() - window.getInsets().left - window.getInsets().right, window.getHeight() - window.getInsets().top - window.getInsets().bottom);
    }
}