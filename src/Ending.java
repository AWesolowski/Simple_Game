import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Ending  extends JPanel
{
    private int width;
    private int height;

    private int button_width;
    private int button_height;
    private int font_size;

    private final String filepath_background = "Images/";
    private final String filepath_buttons = "Images/Buttons/";
    private final String filepath_sounds = "Sounds/";
    private final String filepath_config = "Config/";

    private int state = 0;

    private final Sounds click = new Sounds();

    private JButton exit_button;
    private JButton go_again_button;
    private JLabel nickname;
    private JLabel nickname2;
    private Image background_image;
    private Image exit_button_image;
    private Image go_again_button_image;


    Ending(int x, int y, double points, String player_nick, Records highscore_table)
    {
        width = x;
        height = y;


        button_width = (int) (width * 0.2);
        button_height = (int) (height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Menu_Background.gif");

        setLayout(null);
        addComponentListener(new Ending.main_window());

        exit_button = new JButton();
        exit_button.addActionListener(ending_listener);
        this.add(exit_button);
        setComponentZOrder(exit_button, 0);

        go_again_button = new JButton();
        go_again_button.addActionListener(ending_listener);
        this.add(go_again_button);
        setComponentZOrder(go_again_button, 0);

        nickname = new JLabel("Great job " + player_nick + "!!", SwingConstants.CENTER);
        nickname.setForeground(Color.lightGray);
        this.add(nickname);
        setComponentZOrder(nickname, 0);

        nickname2 = new JLabel("Your score : " + String.format("%.2f", points), SwingConstants.CENTER);
        nickname2.setForeground(Color.lightGray);
        this.add(nickname2);
        setComponentZOrder(nickname2, 0);

        fill_highscores(points, player_nick, highscore_table);
    }


    public int getState()
    {
        return state;
    }


    public void setState(int given_state)
    {
        state = given_state;
    }


    private void fill_highscores(double points, String player_nick, Records table)
    {
        boolean was_changed = false;

        for (int i = 0 ; i < table.getNumber_of_records() ; i++)
        {
            if (points > table.getPoints(i))
            {
                table.addValues(player_nick, points, i);
                was_changed = true;
                break;
            }
        }

        if (!was_changed)
        {
            table.addValues(player_nick, points);
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath_config + "Highscores.txt"));
            for (int i = 0 ; i < table.getNumber_of_records() ; i++)
            {
                bw.write(table.getNickname(i) + " ");
                bw.write(Double.toString(table.getPoints(i)));
                bw.newLine();
            }

            bw.close();
        }
        catch (IOException ex) {}
    }


    private void reresize(int x, int y)
    {
        button_width = (int) (x * 0.2);
        button_height = (int) (y * 0.1);

        exit_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "exit.gif");
        exit_button_image = exit_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        exit_button.setIcon(new ImageIcon(exit_button_image));
        exit_button.setSize(button_width, button_height);
        exit_button.setLocation((x - button_width)/4, (int) (y * 0.6));

        go_again_button_image = Toolkit.getDefaultToolkit().getImage(filepath_buttons + "go_again.gif");
        go_again_button_image = go_again_button_image.getScaledInstance(button_width, button_height, Image.SCALE_DEFAULT);
        go_again_button.setIcon(new ImageIcon(go_again_button_image));
        go_again_button.setSize(button_width, button_height);
        go_again_button.setLocation((int) ((x - button_width) * 0.75), (int) (y * 0.6));

        if ( (int) (height * 0.069686) <= (int) (width * 0.045)) { font_size = (int) (height * 0.069686); }
        else { font_size = (int) (width * 0.045); }
        nickname.setSize((int) (width * 0.2125 * 4),(int) (height * 0.08711));
        nickname.setLocation((width - ((int) (width * 0.2125 * 4)))/2, (int) (height * 0.0348));
        nickname.setFont(new Font("Serif", Font.PLAIN, font_size));

        nickname2.setSize((int) (width * 0.2125 * 2),(int) (height * 0.08711));
        nickname2.setLocation((width - ((int) (width * 0.2125 * 2)))/2, (int) (height * 0.0348 * 4));
        nickname2.setFont(new Font("Serif", Font.PLAIN, font_size));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener ending_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == go_again_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = 1;
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
