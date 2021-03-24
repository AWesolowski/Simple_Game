import javax.swing.*;
import java.awt.*;


public class Window extends JFrame
{
    private int window_width;
    private int window_height;

    private long timex = 0;
    private boolean mute = false;
    private String s_name;

    private String filepath_sounds = "Sounds/";

    private boolean file_exist = true;
    private boolean shorten = false;

    private String o_nick;
    private int lines_numb;

    private String title = "Shoot the ravens";

    private Menu menu;
    private Highscores highscores;
    private Options options;
    private Music music;

    private Window()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window_width = (int)(screenSize.width / 2);
        window_height = (int)(screenSize.height/2);

        this.setSize(window_width, window_height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle(title);
        this.setVisible(true);
        this.setResizable(true);

        CustomCursor();

        menu = new Menu(window_width, window_height);
        highscores = new Highscores(window_width, window_height);
        options = new Options(window_width, window_height);
        music = new Music();
        music.CustomSoundBackground(filepath_sounds + "background_music.wav");

    }

    public void CustomCursor()
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
        this.add(menu);
        repaint();

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
            //call_nick;
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


    private void call_highscores()
    {
        highscores.setSize(window_width, window_height);
        add(highscores);
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










    public static void main(String[] args)
    {
        Window window = new Window();
        window.call_menu();
    }
}
