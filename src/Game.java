import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;


public class Game extends JPanel
{
    private int width;
    private int height;

    private final String filepath_background = "Images/";
    private final String filepath_sounds = "Sounds/";


    private float object_size;
    private int sizer;


    private final Sounds click = new Sounds();

    private JLabel bbtime;
    private JLabel score;
    private JLabel miss;
    private JLabel levels;
    private Image img1;
    private Image img2;
    private ImageIcon image;

    private MouseAdapter mouse_adapter;


    public Game(int x, int y)
    {
        width = x;
        height = y;
        object_size = 400;
        sizer = (int)(object_size * (float)width);


        this.setLayout(null);
        this.addComponentListener(new main_window());


    }






    private void reresize()
    {

    }


    public void paintComponent(Graphics g)
    {

    }


    private class main_window implements ComponentListener
    {

        @Override
        public void componentResized(ComponentEvent e)
        {
            width = getWidth();
            height = getHeight();

            reresize();

            repaint();
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

}
