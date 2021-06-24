import javax.swing.*;
import java.awt.*;


public class Window extends JFrame
{
    private int window_width;
    private int window_height;

    private long timex = 0;
    private boolean mute = false;

    private final String filepath_sounds = "Sounds/";
    private final String title = "PANG";

    private Records highscore_table;

    private Menu menu;
    private Nick nick;
    private Highscores highscores;
    private Options options;
    private Difficulty difficulty;
    private Game game;
    private Ending ending;
    private Music music;

    Client client;
    private JLabel connection_status_label;
    private boolean connection_status;

    private Window()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window_width = screenSize.width / 2;
        window_height = screenSize.height/2;

        this.setSize(window_width, window_height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle(title);
        this.setVisible(true);
        this.setResizable(true);

        CustomCursor();

        highscore_table = new Records();

        menu = new Menu(window_width, window_height);
        nick = new Nick(window_width, window_height);
        highscores = new Highscores(window_width, window_height, highscore_table);
        options = new Options(window_width, window_height);
        difficulty = new Difficulty(window_width, window_height);
        music = new Music();
        music.CustomSoundBackground(filepath_sounds + "background_music.wav");

        connection_status_label = new JLabel();
    }

    private void conncet_to_server()
    {
        connection_status_label.setBounds(window_width - 150, 15, 300, 30);
        connection_status_label.setText("Waiting for server");
        connection_status_label.setForeground(Color.red);

        client = new Client();
        connection_status = client.otworzPolaczenie("Adam", 8080);

        if (connection_status)
        {
            connection_status_label.setText("Connected");
            connection_status_label.setForeground(Color.green);
        }
        else connection_status_label.setText("Failed to connect");

    }

    private void CustomCursor()
    {
        Image cursor_img = Toolkit.getDefaultToolkit().getImage("Images/Cursor.png");
        Point points = new Point(15, 15);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor_img, points, "Cursor");
        this.setCursor(cursor);
    }

    private void call_menu()
    {
        int state;

        menu.setSize(window_width, window_height);
        menu.add(connection_status_label);
        this.add(menu);
        repaint();

        conncet_to_server();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (menu.getState() != 0)
            {
                state = menu.getState();
                getContentPane().removeAll();
                break;
            }
        }

        if (state == 1)
        {
            menu.setState(0);
            call_nick();
        }
        else if (state == 2)
        {
            menu.setState(0);
            call_highscores();
        }
        else if (state == 3)
        {
            menu.setState(0);
            call_options();
        }
    }


    private void call_nick()
    {
        nick.setSize(window_width, window_height);
        this.add(nick);
        repaint();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (nick.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
        }

        if (nick.getState() == 1)
        {
            nick.setState(0);
            call_difficulty();
        }
        else if (nick.getState() == -1)
        {
            nick.setState(0);
            call_menu();
        }
    }


    private void call_highscores()
    {
        highscores.setSize(window_width, window_height);
        this.add(highscores);
        repaint();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (highscores.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
        }

        if (highscores.getState() == -1)
        {
            highscores.setState(0);
            call_menu();
        }
    }


    private void call_options()
    {
        options.setSize(window_width, window_height);
        this.add(options);
        repaint();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (options.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
            else
            {
                float volume = music.getVolume();

                if (options.getVolume() == -1)
                {
                    if (volume >= -59)
                    {
                        music.setVolume(volume - 1);
                    }
                    options.setVolume(0);

                    if (!mute)
                    {
                        timex = music.pauseMusic();
                        music.resumeMusic(timex);
                    }
                }
                else if (options.getVolume() == 1)
                {
                    if (volume <= 14)
                    {
                        music.setVolume(volume + 1);
                    }
                    options.setVolume(0);

                    if (!mute)
                    {
                        timex = music.pauseMusic();
                        music.resumeMusic(timex);
                    }
                }
                else if (options.getMute_state())
                {
                    if (!mute)
                    {
                        timex = music.pauseMusic();
                        music.muteMusic(timex);
                        mute = true;
                    }
                    else
                    {
                        timex = music.pauseMusic();
                        music.resumeMusic(timex);
                        mute = false;
                    }
                    options.setMute_state(false);
                }
            }
        }

        if (options.getState() == -1)
        {
            options.setState(0);
            call_menu();
        }
    }


    private void call_difficulty()
    {
        difficulty.setSize(window_width, window_height);
        this.add(difficulty);
        repaint();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (difficulty.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
        }

        int difficulty_lvl = difficulty.getState();
        difficulty.setState(0);

        if (difficulty_lvl == 1)
        {
            game = new Game(window_width, window_height, 1, connection_status);
            call_game();
        }
        else if (difficulty_lvl == 2)
        {
            game = new Game(window_width, window_height, 2, connection_status);
            call_game();
        }
        else if (difficulty_lvl == 3)
        {
            game = new Game(window_width, window_height, 3, connection_status);
            call_game();
        }
        else if (difficulty_lvl == 4)
        {
            game = new Game(window_width, window_height, 4, connection_status);
            call_game();
        }
        else if (difficulty_lvl == 5)
        {
            game = new Game(window_width, window_height, 5, connection_status);
            call_game();
        }
        else if (difficulty_lvl == -1)
        {
            call_nick();
        }
    }


    private void call_game()
    {
        game.setSize(window_width, window_height);
        this.add(game);
        repaint();

        while (true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException e){}
            if (game.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
        }

        if (game.getState() == 1)
        {
            ending = new Ending(window_width, window_height, game.getPoints(), nick.getPlayers_nick(), highscore_table);

            game.setState(0);
            call_ending();
        }
    }


    private void call_ending()
    {
        ending.setSize((int) (window_width * 0.6), (int) (window_height * 0.6));
        add(ending);
        repaint();

        while(true)
        {
            try {Thread.sleep(100);}
            catch (InterruptedException ex){}
            if (ending.getState() != 0)
            {
                getContentPane().removeAll();
                break;
            }
            else continue;
        }

        if (ending.getState() == 1)
        {
            ending.setState(0);
            call_menu();
        }
    }


    public static void main(String[] args)
    {
        Window window = new Window();
        window.call_menu();
    }
}
