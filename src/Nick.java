import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class Nick extends JPanel
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

    private String players_nick = null;

    private JButton ok_button;
    private JButton return_button;
    private JLabel panel_name;
    private JTextField nickname_field;
    private Image background_image;
    private Image ok_button_image;
    private Image return_button_image;


    public Nick(int x, int y)
    {
        width = x;
        height = y;

        button_width = (int) (width * 0.2);
        button_height = (int) (height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Nick_Background.gif");

        setLayout(null);
        addComponentListener(new Nick.main_window());

        return_button = new JButton();
        return_button.addActionListener(nick_listener);
        this.add(return_button);
        setComponentZOrder(return_button, 0);

        ok_button = new JButton();
        ok_button.addActionListener(nick_listener);
        this.add(ok_button);
        setComponentZOrder(ok_button, 0);

        panel_name = new JLabel("Enter your nickname", SwingConstants.CENTER);
        panel_name.setForeground(Color.PINK);
        this.add(panel_name);
        setComponentZOrder(panel_name, 0);

        nickname_field = new JTextField();
        add(nickname_field);
        setComponentZOrder(nickname_field, 0);
    }


    public int getState()
    {
        return state;
    }


    public void setState(int given_state)
    {
        state = given_state;
    }


    public String getPlayers_nick()
    {
        return players_nick;
    }


    private void reresize(int x, int y)
    {
        button_width = (int) (x * 0.2);
        button_height = (int) (y * 0.1);

        return_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "return.gif");
        return_button_image = return_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        return_button.setIcon(new ImageIcon(return_button_image));
        return_button.setSize(button_width, button_height);
        return_button.setLocation((int) ((width - button_width)*0.75), (int) (height * 0.6));

        ok_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "ok.gif");
        ok_button_image = ok_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        ok_button.setIcon(new ImageIcon(ok_button_image));
        ok_button.setSize(button_width, button_height);
        ok_button.setLocation((width - button_width)/4, (int) (height * 0.6));

        font_size = Math.min((int) (height * 0.069686), (int) (width * 0.045));
        panel_name.setFont(new Font("Serif", Font.PLAIN, font_size));
        panel_name.setSize((int) (width * 0.2125 * 2),(int) (height * 0.08711));
        panel_name.setLocation((width - ((int) (width * 0.2125 * 2)))/2, (int) (height * 0.0348));

        nickname_field.setFont(new Font("Serif", Font.PLAIN, font_size));
        nickname_field.setSize(button_width * 3,(int) (button_height * 1.5));
        nickname_field.setLocation((x - button_width * 3)/2, (int) (y * 0.3));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener nick_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == return_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = -1;
            }
            else if (e.getSource() == ok_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                players_nick = nickname_field.getText();

                if (!players_nick.equals("") && !players_nick.equals("Please enter your nick"))
                {
                    state = 1;
                }
                else
                {
                    nickname_field.setText("Please enter your nick");
                    state = 0;
                }
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
