import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;


public class Highscores extends JPanel
{
    private int width;
    private int height;

    private int button_width;
    private int button_height;
    private int font_size;

    private final String filepath_config = "Config/";
    private final String filepath_background = "Images/";
    private final String filepath_buttons = "Images/Buttons/";
    private final String filepath_sounds = "Sounds/";

    private int state = 0;

    private final Sounds click = new Sounds();

    private final JButton return_button;
    private final JLabel panel_name;
    private final JLabel scores;
    private final Image background_image;
    private Image return_button_image;


    public Highscores(int x, int y, Records highscore_table)
    {
        width = x;
        height = y;

        button_width = (int) (width * 0.2);
        button_height = (int) (height * 0.1);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_background + "Highscores_Background.gif");

        setLayout(null);
        addComponentListener(new main_window());

        return_button = new JButton();
        return_button.addActionListener(highscores_listener);
        this.add(return_button);
        setComponentZOrder(return_button, 0);

        panel_name = new JLabel("Highscores", SwingConstants.CENTER);
        panel_name.setForeground(Color.CYAN);
        this.add(panel_name);
        setComponentZOrder(panel_name, 0);

        scores = new JLabel("", SwingConstants.CENTER);
        scores.setForeground(Color.PINK);
        setComponentZOrder(scores, 0);

        load_highscore_data_from_file("Highscores.txt", highscore_table);
        show_scores(highscore_table);
    }


    public int getState()
    {
        return state;
    }


    public void setState(int given_state)
    {
        state = given_state;
    }


    private void load_highscore_data_from_file(String filename, Records table)
    {
        if (new File(filepath_config + filename).exists())
        {
            BufferedReader bufferedReader = null;

            try
            {
                bufferedReader = new BufferedReader(new FileReader(filepath_config + filename));
                System.out.println("Highscores file opened");

                String line;
                double points;
                int i = 0;

                while ((line = bufferedReader.readLine()) != null)
                {
                    String[] words = line.split(" ");

                    try {points = Double.parseDouble(words[1]);}
                    catch (NumberFormatException e) {points = 0;}

                    table.addValues(words[0], points);
                    i++;
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Highscores file do not exist");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("Couldn't read the line");
                e.printStackTrace();
            }
            finally
            {
                try {bufferedReader.close();}
                catch (IOException e) {System.out.println("Couldn't close bufferedReader");}
            }
        }
        else
        {
            System.out.println("Highscores file do not exist");
        }
    }


    public void show_scores(Records table)
    {
        String tmp_string;
        int tmp_int = 0;
        int number_of_records = table.getNumber_of_records();

        if (number_of_records == 0)
        {
            tmp_string = "";
        }
        else
        {
            if (number_of_records > 1)
            {
                tmp_string = "<html>";

                for (int i = 0; i < Math.min(number_of_records - 1, 6); i++)
                {
                    tmp_string = tmp_string + (i + 1) + ". " + table.getNickname(i) + " : "
                            + table.getPoints(i) + "<br>";

                    tmp_int = i;
                }

                tmp_int++;

                tmp_string = tmp_string + (tmp_int + 1) + ". " + table.getNickname(tmp_int) + " : "
                        + table.getPoints(tmp_int) + "</html>";
            }
            else
            {
                tmp_string = "1. " + table.getNickname(0) + " : " + table.getPoints(0);
            }
        }

        scores.setText(tmp_string);
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

        font_size = Math.min((int) (height * 0.069686), (int) (width * 0.045));
        panel_name.setFont(new Font("Serif", Font.PLAIN, font_size));
        panel_name.setSize((int) (width * 0.2125 * 2),(int) (height * 0.08711));
        panel_name.setLocation((width - ((int) (width * 0.2125 * 2)))/2, (int) (height * 0.0348));

        scores.setFont(new Font("Serif", Font.PLAIN, font_size));
        scores.setSize((int) (width * 0.2125 * 2),(int) (height * 0.08711 * 8));
        scores.setLocation((width - ((int) (width * 0.2125 * 2)))/2, (int) (height * 0.0348 * 2));
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);
    }


    ActionListener highscores_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == return_button)
            {
                click.playSound(filepath_sounds + "button.wav");
                state = -1;
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
