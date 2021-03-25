import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Options extends JPanel
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

    private int volume = 0;
    private boolean mute_state = false;

    private final JButton return_button;
    private final JButton volume_up_button;
    private final JButton volume_down_button;
    private final JButton mute_button;
    private final JLabel panel_name;
    private final Image background_image;
    private Image return_button_image;
    private Image volume_up_button_image;
    private Image volume_down_button_image;
    private Image mute_button_image;


    public Options(int x, int y)
    {
        width = x;
        height = y;

        button_width = (int) (width * 0.2);
        button_height = (int) (height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Options_Background.gif");

        setLayout(null);
        addComponentListener(new main_window());

        return_button = new JButton();
        return_button.addActionListener(options_listener);
        this.add(return_button);
        setComponentZOrder(return_button, 0);

        volume_up_button = new JButton();
        volume_up_button.addActionListener(options_listener);
        this.add(volume_up_button);
        setComponentZOrder(volume_up_button, 0);

        volume_down_button = new JButton();
        volume_down_button.addActionListener(options_listener);
        this.add(volume_down_button);
        setComponentZOrder(volume_down_button, 0);

        mute_button = new JButton();
        mute_button.addActionListener(options_listener);
        this.add(mute_button);
        setComponentZOrder(mute_button, 0);

        panel_name = new JLabel("Options", SwingConstants.CENTER);
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


    public int getVolume()
    {
        return volume;
    }


    public void setVolume(int given_volume)
    {
        volume = given_volume;
    }


    public boolean getMute_state()
    {
        return mute_state;
    }


    public void setMute_state(boolean given_pause_state)
    {
        mute_state = given_pause_state;
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

        volume_up_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "volume_up.gif");
        volume_up_button_image = volume_up_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        volume_up_button.setIcon(new ImageIcon(volume_up_button_image));
        volume_up_button.setSize(button_width, button_height);
        volume_up_button.setLocation((x - button_width)/2, (int) (y * 0.6));

        volume_down_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "volume_down.gif");
        volume_down_button_image = volume_down_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        volume_down_button.setIcon(new ImageIcon(volume_down_button_image));
        volume_down_button.setSize(button_width, button_height);
        volume_down_button.setLocation((x - button_width)/2, (int) (y * 0.4));

        mute_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "pause.gif");
        mute_button_image = mute_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        mute_button.setIcon(new ImageIcon(mute_button_image));
        mute_button.setSize(button_width, button_height);
        mute_button.setLocation((x - button_width)/2, (int) (y * 0.2));

        font_size = Math.min((int) (height * 0.069686), (int) (width * 0.045));
        panel_name.setFont(new Font("Serif", Font.PLAIN, font_size));
        panel_name.setSize((int) (width * 0.2125),(int) (height * 0.08711));
        panel_name.setLocation((width - ((int) (width * 0.2125)))/2, (int) (height * 0.0348));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener options_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == return_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = -1;
            }
            else if (e.getSource() == volume_up_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                volume = 1;
            }
            else if (e.getSource() == volume_down_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                volume = -1;
            }
            else if (e.getSource() == mute_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                mute_state = true;
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
