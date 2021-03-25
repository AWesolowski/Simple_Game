import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class Menu extends JPanel
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

    private final JButton start_button;
    private final JButton highscores_button;
    private final JButton options_button;
    private final JButton exit_button;
    private final JLabel panel_name;

    private Image background_image;
    private Image start_button_image;
    private Image highscores_button_image;
    private Image options_button_image;
    private Image exit_button_image;


    public Menu(int x, int y)
    {
        width = x;
        height = y;

        button_width = (int)(width * 0.2);
        button_height = (int)(height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Menu_Background.gif");

        this.setLayout(null);
        this.addComponentListener(new main_window());

        start_button = new JButton();
        start_button.addActionListener(menu_listener);
        this.add(start_button);
        setComponentZOrder(start_button, 0);

        highscores_button = new JButton();
        highscores_button.addActionListener(menu_listener);
        this.add(highscores_button);
        setComponentZOrder(highscores_button, 0);

        options_button = new JButton();
        options_button.addActionListener(menu_listener);
        this.add(options_button);
        setComponentZOrder(options_button, 0);

        exit_button = new JButton();
        exit_button.addActionListener(menu_listener);
        this.add(exit_button);
        setComponentZOrder(exit_button, 0);

        panel_name = new JLabel("Menu Start", SwingConstants.CENTER);
        panel_name.setForeground(Color.LIGHT_GRAY);
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

        start_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "start.gif");
        start_button_image = start_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        start_button.setIcon(new ImageIcon(start_button_image));
        start_button.setSize(button_width, button_height);
        start_button.setLocation((x - button_width)/2, (int) (y * 0.2));

        highscores_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "highscores.gif");
        highscores_button_image = highscores_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        highscores_button.setIcon(new ImageIcon(highscores_button_image));
        highscores_button.setSize(button_width, button_height);
        highscores_button.setLocation((x - button_width)/2, (int) (y * 0.4));

        options_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "options.gif");
        options_button_image = options_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        options_button.setIcon(new ImageIcon(options_button_image));
        options_button.setSize(button_width, button_height);
        options_button.setLocation((x - button_width)/2, (int) (y * 0.6));

        exit_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "exit.gif");
        exit_button_image = exit_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        exit_button.setIcon(new ImageIcon(exit_button_image));
        exit_button.setSize(button_width, button_height);
        exit_button.setLocation((x - button_width)/2, (int) (y * 0.8));

        font_size = Math.min((int) (height * 0.069686), (int) (width * 0.045));
        panel_name.setSize((int) (width * 0.2125),(int) (height * 0.08711));
        panel_name.setLocation((width - ((int) (width * 0.2125)))/2, (int) (height * 0.0348));
        panel_name.setFont(new Font("Serif", Font.PLAIN, font_size));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener menu_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == start_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 1;
            }
            else if (e.getSource() == highscores_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 2;
            }
            else if (e.getSource() == options_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 3;
            }
            else if (e.getSource() == exit_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                System.exit(0);
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
