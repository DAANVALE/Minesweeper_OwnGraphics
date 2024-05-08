import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {

    JFrame frame = new JFrame();
    Paint paintPanel;
    int xBlock = -1, yBlock = -1;

    boolean firstClick = true;
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

                firstClick = true;
                paintPanel.setSize(getPanelSize(frame));

                width  = paintPanel.getWidth();
                height = paintPanel.getHeight();
            }
        });

        paintPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){

                    if(paintPanel.didLose() || paintPanel.didWin()){
                        return;
                    }

                    xBlock = e.getX();
                    yBlock = e.getY();
                    paintPanel.onClickBlock(xBlock, yBlock);
                    paintPanel.repaint();

                    if(paintPanel.didLose() && firstClick){
                        JOptionPane.showConfirmDialog(null,"Again", null, JOptionPane.DEFAULT_OPTION);
                        paintPanel.setSize(getPanelSize(frame));
                        paintPanel.onClickBlock(xBlock, yBlock);
                        paintPanel.repaint();
                        return;
                    }

                    firstClick = false;

                    if(paintPanel.didLose()){
                        JOptionPane.showConfirmDialog(null,"LOSE FOR REAL", null, JOptionPane.DEFAULT_OPTION);
                        paintPanel.onLose(xBlock, yBlock);
                    }

                    if(paintPanel.didWin()){
                        JOptionPane.showConfirmDialog(null,"WIN FOR REAL", null, JOptionPane.DEFAULT_OPTION);
                    }
                }

                if(e.getButton() == MouseEvent.BUTTON3){

                    if(paintPanel.didLose()){
                        return;
                    }

                    xBlock = e.getX();
                    yBlock = e.getY();
                    paintPanel.onFlagBlock(xBlock, yBlock);
                    paintPanel.repaint();
                }
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