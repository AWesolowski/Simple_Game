import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class Difficulty extends JPanel
{
    private int width;
    private int height;

    private int button_width;
    private int button_height;
    private int font_size;

    private final String filepath_background = "Images/";
    private final String filepath_buttons = "Images/Buttons/";
    private final String filepath_sounds = "Sounds/";

    private int state = 0;

    private final Sounds click = new Sounds();

    private JButton return_button;
    private JButton Baby_Mode_button;
    private JButton Easy_button;
    private JButton Medium_button;
    private JButton Hard_button;
    private JButton Suicide_button;
    private JLabel panel_name;
    private Image background_image;
    private Image return_button_image;
    private Image Baby_Mode_button_image;
    private Image Easy_button_image;
    private Image Medium_button_image;
    private Image Hard_button_image;
    private Image Suicide_button_image;


    public Difficulty(int x, int y)
    {
        width = x;
        height = y;

        button_width = (int) (width * 0.2);
        button_height = (int) (height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Menu_Background.gif");

        setLayout(null);
        addComponentListener(new Difficulty.main_window());

        return_button = new JButton();
        return_button.addActionListener(difficulty_listener);
        this.add(return_button);
        setComponentZOrder(return_button, 0);

        Baby_Mode_button = new JButton();
        Baby_Mode_button.addActionListener(difficulty_listener);
        this.add(Baby_Mode_button);
        setComponentZOrder(Baby_Mode_button, 0);

        Easy_button = new JButton();
        Easy_button.addActionListener(difficulty_listener);
        this.add(Easy_button);
        setComponentZOrder(Easy_button, 0);

        Medium_button = new JButton();
        Medium_button.addActionListener(difficulty_listener);
        this.add(Medium_button);
        setComponentZOrder(Medium_button, 0);

        Hard_button = new JButton();
        Hard_button.addActionListener(difficulty_listener);
        this.add(Hard_button);
        setComponentZOrder(Hard_button, 0);

        Suicide_button = new JButton();
        Suicide_button.addActionListener(difficulty_listener);
        this.add(Suicide_button);
        setComponentZOrder(Suicide_button, 0);

        panel_name = new JLabel("Choose your difficulty", SwingConstants.CENTER);
        panel_name.setForeground(Color.PINK);
        this.add(panel_name);
        setComponentZOrder(panel_name, 0);

    }


    public int getState()
    {
        return state;
    }


    public void setState(int given_state)
    {
        state = given_state;
    }


    private void reresize(int x, int y)
    {
        button_width = (int) (x * 0.2);
        button_height = (int) (y * 0.1);

        return_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "return.gif");
        return_button_image = return_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        return_button.setIcon(new ImageIcon(return_button_image));
        return_button.setSize(button_width, button_height);
        return_button.setLocation((x - button_width)/2, (int) (y * 0.8));

        Baby_Mode_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "baby.gif");
        Baby_Mode_button_image = Baby_Mode_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        Baby_Mode_button.setIcon(new ImageIcon(Baby_Mode_button_image));
        Baby_Mode_button.setSize(button_width, button_height);
        Baby_Mode_button.setLocation((x - button_width) / 4, (int) (y * 0.6));

        Easy_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "easy.gif");
        Easy_button_image = Easy_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        Easy_button.setIcon(new ImageIcon(Easy_button_image));
        Easy_button.setSize(button_width, button_height);
        Easy_button.setLocation((int) ((x - button_width) * 0.75), (int) (y * 0.6));

        Medium_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "medium.gif");
        Medium_button_image = Medium_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        Medium_button.setIcon(new ImageIcon(Medium_button_image));
        Medium_button.setSize(button_width, button_height);
        Medium_button.setLocation((x - button_width)/2, (int) (y * 0.4));

        Hard_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "hard.gif");
        Hard_button_image = Hard_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        Hard_button.setIcon(new ImageIcon(Hard_button_image));
        Hard_button.setSize(button_width, button_height);
        Hard_button.setLocation((x - button_width) / 4, (int) (y * 0.2));

        Suicide_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "suicide.gif");
        Suicide_button_image = Suicide_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        Suicide_button.setIcon(new ImageIcon(Suicide_button_image));
        Suicide_button.setSize(button_width, button_height);
        Suicide_button.setLocation((int) ((x - button_width) * 0.75), (int) (y * 0.2));

        font_size = Math.min((int) (height * 0.069686), (int) (width * 0.045));
        panel_name.setFont(new Font("Serif", Font.PLAIN, font_size));
        panel_name.setSize((int) (width * 0.2125 * 2),(int) (height * 0.08711));
        panel_name.setLocation((width - ((int) (width * 0.2125 * 2)))/2, (int) (height * 0.0348));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener difficulty_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == return_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = -1;
            }
            else if (e.getSource() == Baby_Mode_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 1;
            }
            else if (e.getSource() == Easy_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 2;
            }
            else if (e.getSource() == Medium_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 3;
            }
            else if (e.getSource() == Hard_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 4;
            }
            else if (e.getSource() == Suicide_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 5;
            }

        }
    };


    private class main_window implements ComponentListener
    {

        @Override
        public void componentResized(ComponentEvent e)
        {
            width = getWidth();
            height = getHeight();

            reresize(width, height);

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
