import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Integer.parseInt;


public class Game extends JPanel implements ActionListener
{
    private int width;
    private int height;

    public int state = 0;
    private boolean paused = true;

    private int scores = 0;
    private int lvlscore = 1;
    public double points = 0;

    private  Offline_Parser fr;
    private int speed_boost;
    private int lives_left;
    public double wsp;
    private int number_of_levels;
    private double difficulty_change;
    private double starting_object_size;
    private int number_of_enemies;

    private final Random generator = new Random();
    private double rand_x;
    private double rand_y;

    private int x1, x2, x3, x4, x5;
    private int y1, y2, y3, y4, y5;
    private double x1_velocity, x2_velocity, x3_velocity, x4_velocity, x5_velocity;
    private double y1_velocity, y2_velocity, y3_velocity, y4_velocity, y5_velocity;

    private double mouse_x;
    private double mouse_y;
    private double screen_width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private double screen_height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private int sizer;
    private double objects_size;

    private int count_lvl = 0;
    public int pointsMultiplier;

    private javax.swing.Timer t = new javax.swing.Timer(5,this);
    private long start = System.currentTimeMillis();

    private final String filepath_Background = "Images/";
    private final String filepath_Game_Images = "Images/Game/";
    private final String filepath_sounds = "Sounds/";

    private final Sounds click = new Sounds();

    private ArrayList<Rectangle2D> rect_list = new ArrayList<>();
    private Rectangle2D rect1, rect2, rect3, rect4, rect5;

    private JLabel TimeLeft;
    private JLabel score;
    private JLabel lives;
    private JLabel levels;
    private Image enemy_image;
    private Image background_image;

    private MouseAdapter mouse_adapter;
    private PointerInfo pointerInfo;
    private Point point;


    public Game(int gx, int gy, int multiplier, boolean connection_status, Client client)
    {
        width = gx;
        height = gy;
        pointsMultiplier = multiplier;

        if (connection_status)
        {
            speed_boost = parseInt(client.wyslijPolecenie("get_speed_boost"));
            lives_left = parseInt(client.wyslijPolecenie("get_collisions_available"));
            wsp = Double.parseDouble(client.wyslijPolecenie("get_speed_parameter"));
            number_of_levels = parseInt(client.wyslijPolecenie("get_number_of_levels"));
            difficulty_change = Double.parseDouble(client.wyslijPolecenie("get_difficulty_change"));
            starting_object_size = Double.parseDouble(client.wyslijPolecenie("get_starting_object_size"));
            number_of_enemies = parseInt(client.wyslijPolecenie("get_starting_number_of_enemies"));
        }
        else
        {
            fr = new Offline_Parser();
            speed_boost = fr.getSpeed_boost();
            lives_left = fr.getCollisions_available();
            wsp = fr.getSpeed_parameter();
            number_of_levels = fr.getNumber_of_levels();
            difficulty_change = fr.getDifficulty_change();
            starting_object_size = fr.getStarting_object_size();
            number_of_enemies = fr.getNumber_of_enemies();
        }

        objects_size = starting_object_size/100;
        sizer = (int)(objects_size * (float)width);

        Random r = new Random();
        x1_velocity = r.nextDouble()/500;
        y1_velocity = r.nextDouble()/500;
        rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
        rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
        x1 = (int) rand_x;
        y1 = (int) rand_y;

        if (number_of_enemies > 1 && number_of_enemies < 3)
        {
            x2_velocity = r.nextDouble()/500;
            y2_velocity = r.nextDouble()/500;
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x2 = (int) rand_x;
            y2 = (int) rand_y;
        }
        else if (number_of_enemies > 2 && number_of_enemies < 4)
        {
            x3_velocity = r.nextDouble()/500;
            y3_velocity = r.nextDouble()/500;
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x3 = (int) rand_x;
            y3 = (int) rand_y;
        }
        else if (number_of_enemies > 3 && number_of_enemies < 5)
        {
            x4_velocity = r.nextDouble()/500;
            y4_velocity = r.nextDouble()/500;
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x4 = (int) rand_x;
            y4 = (int) rand_y;
        }
        else if (number_of_enemies > 4)
        {
            x5_velocity = r.nextDouble()/500;
            y5_velocity = r.nextDouble()/500;
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x5 = (int) rand_x;
            y5 = (int) rand_y;
        }



        background_image = Toolkit.getDefaultToolkit().getImage(filepath_Background + "Game_Background.gif");

        this.setLayout(null);
        this.addComponentListener(new main_window());

        score = new JLabel("Score");
        add(score);
        scored();

        lives = new JLabel("Lives left");
        add(lives);
        collisions();

        levels = new JLabel("Level");
        add(levels);
        lvl();

        TimeLeft = new JLabel("Czas do końca gry:");
        add(TimeLeft);
        game_time_counter();

        /**
         *Tworzenie nowych obiektów po trafieniu lub zliczanie nie trafionych strzałów w zależnosci od tego do czego
         * mamy strzelać figury geometryczne (kwadrat,kółko,trójkąt,prostokąt) czy też obiektów graficznych.
         */
        this.addMouseListener(mouse_adapter = new MouseAdapter()
        {

            public void mouseClicked(MouseEvent me)
            {
                super.mouseClicked(me);
                click.playSound(filepath_sounds + "click.wav");

                if(rect1.contains(me.getPoint()))
                {
                    x1 = generator.nextInt(width - sizer * 2) + sizer;
                    y1 = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else if(number_of_enemies > 1 && number_of_enemies < 3 && rect2.contains(me.getPoint()))
                {
                    x2 = generator.nextInt(width - sizer * 2) + sizer;
                    y2 = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else if(number_of_enemies > 2 && number_of_enemies < 4 && rect3.contains(me.getPoint()))
                {
                    x3 = generator.nextInt(width - sizer * 2) + sizer;
                    y3 = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else if(number_of_enemies > 3 && number_of_enemies < 5 && rect4.contains(me.getPoint()))
                {
                    x4 = generator.nextInt(width - sizer * 2) + sizer;
                    y4 = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else if(number_of_enemies > 4 && rect5.contains(me.getPoint()))
                {
                    x5 = generator.nextInt(width - sizer * 2) + sizer;
                    y5 = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else {
                    if (pointsMultiplier != 1)
                    {
                        lives_left--;
                        System.out.println(lives_left);
                        if (lives_left == 0)
                        {
                            EndGame();
                        }
                        collisions();
                        repaint();
                    }

                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pause();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pause();
            }
        });
    }


    private void pause()
    {
        if (paused)
        {
            t.addActionListener(this);
            paused = false;
        }
        else
        {
            t.removeActionListener(this);
            paused = true;
        }
    }


    public void scored()
    {
        scores++;

        score.setBounds(15, 5, 300, 30);

        if (pointsMultiplier == 2)
        {
            points++;
        }
        else if (pointsMultiplier == 3)
        {
            points = points + 1+1*((difficulty_change)/100) ;
        }
        else if (pointsMultiplier == 4)
        {
            points = points + 1 + (1 * 2 * (((difficulty_change)/100)));
        }
        else if (pointsMultiplier == 5)
        {
            points = points + 1 + (1 * 4 * (((difficulty_change)/100)));
        }

        if (points > 1)
        {
            click.playSound(filepath_sounds + "click.wav");
            speed_boost++;

            speed_boost = speed_boost + 5;

            move(speed_boost);
        }

        if ((scores % 16) == 0 && scores > 1)
        {
            if (lvlscore <= number_of_levels)
            {
                count_lvl++;

                if (pointsMultiplier == 2)
                {
                    sizer = (int) (sizer * 0.8);
                }
                else if (pointsMultiplier == 3)
                {
                    sizer = (int) (sizer * 0.75);
                }
                else if (pointsMultiplier == 4)
                {
                    sizer = (int) (sizer * 0.70);
                }
                else if (pointsMultiplier == 5)
                {
                    sizer = (int) (sizer * 0.5);
                }

                lvlscore++;
                lvl();
                repaint();
                System.out.println(sizer);
                System.out.println("ROBIE");
                System.out.println(lvlscore);
            }
        }

        score.setText("Score: " + (int)points);
        score.setForeground(Color.red);
    }


    public void collisions(){
        lives.setBounds(15, 20, 300, 30);
        lives.setText("Lives Left: " + lives_left);
        lives.setForeground(Color.red);
    }


    public void lvl(){
        levels.setBounds(15,35,300,30);
        levels.setText("Level :" + lvlscore);
        levels.setForeground(Color.red);
    }


    public void game_time_counter(){

        if(pointsMultiplier ==4) {
            TimeLeft.setBounds(15, 50, 300, 30);
            TimeLeft.setText("Time left to play:" + (100 - (System.currentTimeMillis() - start) / 1000));
            TimeLeft.setForeground(Color.red);
        }

    }


    public void EndGame()
    {
        t.removeActionListener(this);
        t.stop();
        removeMouseListener(mouse_adapter);
        state = 1;
    }


    public void actionPerformed(ActionEvent e)
    {
        if ((System.currentTimeMillis() - start) / 1000F >= 100)
        {
            System.out.println("koniec");
            EndGame();
        }
        else {

            if (x1 <= 0 || x1 >= width - sizer)
            {
                x1_velocity = -x1_velocity;
            }
            if (y1 <= 0 || y1 >= height - sizer)
            {
                y1_velocity = -y1_velocity;
            }
            x1 += rand_x * (1 + wsp * speed_boost) * x1_velocity;
            y1 += rand_y * (1 + wsp * speed_boost) * y1_velocity;

            if (number_of_enemies > 1 && number_of_enemies < 3)
            {
                if (x2 <= 0 || x2 >= width - sizer)
                {
                    x2_velocity = -x2_velocity;
                }
                if (y2 <= 0 || y2 >= height - sizer)
                {
                    y2_velocity = -y2_velocity;
                }
                x2 += rand_x * (1 + wsp * speed_boost) * x2_velocity;
                y2 += rand_y * (1 + wsp * speed_boost) * y2_velocity;
            }
            else if (number_of_enemies > 2 && number_of_enemies < 4)
            {
                if (x3 <= 0 || x3 >= width - sizer)
                {
                    x3_velocity = -x3_velocity;
                }
                if (y3 <= 0 || y3 >= height - sizer)
                {
                    y3_velocity = -y3_velocity;
                }
                x3 += rand_x * (1 + wsp * speed_boost) * x3_velocity;
                y3 += rand_y * (1 + wsp * speed_boost) * y3_velocity;
            }
            else if (number_of_enemies > 3 && number_of_enemies < 5)
            {
                if (x4 <= 0 || x4 >= width - sizer)
                {
                    x4_velocity = -x4_velocity;
                }
                if (y4 <= 0 || y4 >= height - sizer)
                {
                    y4_velocity = -y4_velocity;
                }
                x4 += rand_x * (1 + wsp * speed_boost) * x4_velocity;
                y4 += rand_y * (1 + wsp * speed_boost) * y4_velocity;
            }
            else if (number_of_enemies > 4)
            {
                if (x5 <= 0 || x5 >= width - sizer)
                {
                    x5_velocity = -x5_velocity;
                }
                if (y5 <= 0 || y5 >= height - sizer)
                {
                    y5_velocity = -y5_velocity;
                }
                x5 += rand_x * (1 + wsp * speed_boost) * x5_velocity;
                y5 += rand_y * (1 + wsp * speed_boost) * y5_velocity;
            }
        }
    }


    public void move(int boost)
    {
        Random r = new Random();

        rand_x = -1 + (1 - -1) * r.nextDouble();
        rand_y = -1 + (1 - -1) * r.nextDouble();
        x1 += rand_x * (1 + 0.5 * boost) * x1_velocity;
        y1 += rand_y * (1 + 0.5 * boost) * y1_velocity;

        if (number_of_enemies > 1 && number_of_enemies < 3)
        {
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x2 += rand_x * (1 + 0.5 * boost) * x2_velocity;
            y2 += rand_y * (1 + 0.5 * boost) * y2_velocity;
        }
        else if (number_of_enemies > 2 && number_of_enemies < 4)
        {
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x3 += rand_x * (1 + 0.5 * boost) * x3_velocity;
            y3 += rand_y * (1 + 0.5 * boost) * y3_velocity;
        }
        else if (number_of_enemies > 3 && number_of_enemies < 5)
        {
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x4 += rand_x * (1 + 0.5 * boost) * x4_velocity;
            y4 += rand_y * (1 + 0.5 * boost) * y4_velocity;
        }
        else if (number_of_enemies > 4)
        {
            rand_x = sizer*3/4 + r.nextInt(width - sizer*5/4);
            rand_y = sizer*3/4 + r.nextInt(height - sizer*5/4);
            x5 += rand_x * (1 + 0.5 * boost) * x5_velocity;
            y5 += rand_y * (1 + 0.5 * boost) * y5_velocity;
        }

        repaint();
    }


    private void loadImage()
    {
        enemy_image = Toolkit.getDefaultToolkit().getImage(filepath_Game_Images + "Bird.gif");
    }


    public int getState()
    {
        return state;
    }

    public void setState(int given_state)
    {
        state = given_state;
    }

    public double getPoints()
    {
        return points;
    }


    private void reresize()
    {
        sizer = (int) (objects_size * (float) width);

        if (pointsMultiplier == 2)
        {
            sizer = (int) (sizer * Math.pow(0.8, count_lvl));
        }
        else if (pointsMultiplier == 3)
        {
            sizer = (int) (sizer * Math.pow(0.75, count_lvl));
        }
        else if (pointsMultiplier == 4)
        {
            sizer = (int) (sizer * Math.pow(0.70, count_lvl));
        }
        else if (pointsMultiplier == 5)
        {
            sizer = (int) (sizer * Math.pow(0.5, count_lvl));
        }
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setDoubleBuffered(true);
        g.drawImage(background_image, 0, 0, width, height, this);

        loadImage();
        g.drawImage(enemy_image, x1, y1,sizer,sizer,this);
        rect1 = new Rectangle2D.Double(x1, y1, sizer, sizer);

        if (number_of_enemies > 1 && number_of_enemies < 3)
        {
            loadImage();
            g.drawImage(enemy_image, x2, y2,sizer,sizer,this);
            rect2 = new Rectangle2D.Double(x2, y2, sizer, sizer);
        }
        else if (number_of_enemies > 2 && number_of_enemies < 4)
        {
            loadImage();
            g.drawImage(enemy_image, x3, y3,sizer,sizer,this);
            rect3 = new Rectangle2D.Double(x3, y3, sizer, sizer);
        }
        else if (number_of_enemies > 3 && number_of_enemies < 5)
        {
            loadImage();
            g.drawImage(enemy_image, x4, y4,sizer,sizer,this);
            rect4 = new Rectangle2D.Double(x4, y4, sizer, sizer);
        }
        else if (number_of_enemies > 4)
        {
            loadImage();
            g.drawImage(enemy_image, x5, y5,sizer,sizer,this);
            rect5 = new Rectangle2D.Double(x5, y5, sizer, sizer);
        }

        t.start();
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
