import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class Game extends JPanel implements ActionListener
{
    private int width;
    private int height;

    public int state = 0;
    private boolean paused = false;

    private final Offline_Parser fr = new Offline_Parser();
    private int scores = 0;
    private int lvlscore = 1;
    private int speed_boost = fr.getboo();
    private int lives_left = fr.geTmi(); //Liczba żyć
    public double points = fr.geTpoi();
    public double wsp = fr.geTws();

    private int liczbaPoziomow = fr.getNumberOfLevels();
    private double zmianastopniatrudnosci = fr.getZmianaStopniaTrudnosci();
    private double poczatkowaszerokoscobiektu = fr.getPoczatkowaszerokoscObiektu();

    private final Random generator = new Random();
    private double rand_x = 1;
    private double rand_y = 1;
    private double x = 100, y = 100, velocity_x = 0.7, velocity_y = 0.7;
    private ArrayList<Integer> x_list = new ArrayList<>();
    private ArrayList<Integer> y_list = new ArrayList<>();
    private ArrayList<Double> x_velocity_list = new ArrayList<>();
    private ArrayList<Double> y_velocity_list = new ArrayList<>();

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
    private Rectangle2D rect;

    private JLabel TimeLeft;
    private JLabel score;
    private JLabel lives;
    private JLabel levels;
    private Image enemy_image;
    private Image background_image;

    private MouseAdapter mouse_adapter;
    private KeyAdapter key_adapter;


    public Game(int gx, int gy, int multiplier, boolean connection_status)
    {
        width = gx;
        height = gy;
        pointsMultiplier = multiplier;

        objects_size = poczatkowaszerokoscobiektu/100;
        sizer = (int)(objects_size * (float)width);

        Random r = new Random();
        for (int i = 0 ; i < 3 ; i++)
        {
            rand_x = sizer/2 + r.nextInt(width - sizer);
            rand_y = sizer/2 + r.nextInt(height - sizer);
            x_list.add((int)rand_x);
            y_list.add((int)rand_y);

            x_velocity_list.add(r.nextDouble()/100);
            y_velocity_list.add(r.nextDouble()/100);
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

                if(rect_list.get(0).contains(me.getPoint()) || rect_list.get(1).contains(me.getPoint()) || rect_list.get(2).contains(me.getPoint()))
                {
                    x = generator.nextInt(width - sizer * 2) + sizer;
                    y = generator.nextInt(height - sizer * 2) + sizer;
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
        });

        this.addKeyListener(key_adapter = new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                System.out.println("Key pressed");
            }

            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
                setFocusable(true);
                requestFocusInWindow();
                System.out.println("Key pressed");

                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_ESCAPE)
                {
                    pause();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                System.out.println("Key pressed");
            }
        });
    }


    private void pause()
    {
        if (paused)
        {
            t.addActionListener(this);
        }
        else t.removeActionListener(this);
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
            points = points + 1+1*((zmianastopniatrudnosci)/100) ;
        }
        else if (pointsMultiplier == 4)
        {
            points = points + 1 + (1 * 2 * (((zmianastopniatrudnosci)/100)));
        }
        else if (pointsMultiplier == 5)
        {
            points = points + 1 + (1 * 4 * (((zmianastopniatrudnosci)/100)));
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
            if (lvlscore <= liczbaPoziomow)
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
                if (sizer == 25)
                {
                    //sizer = sizer;
                }
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
        removeKeyListener(key_adapter);
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
            for (int i = 0 ; i < x_list.size() ; i++)
            {
                if (x_list.get(i) <= 0 || x_list.get(i) >= width - sizer)
                {
                    x_velocity_list.set(i, -x_velocity_list.get(i));
                }
                if (y_list.get(i) <= 0 || y_list.get(i) >= height - sizer)
                {
                    y_velocity_list.set(i, -y_velocity_list.get(i));
                }
                x_list.set(i, (int) (x_list.get(i) + rand_x * (1 + wsp * speed_boost) * x_velocity_list.get(i)));
                y_list.set(i, (int) (y_list.get(i) + rand_y * (1 + wsp * speed_boost) * y_velocity_list.get(i)));
                game_time_counter();
                repaint();
            }

//            if (x <= 0 || x >= width - sizer)
//            {
//                velocity_x = -velocity_x;
//            }
//            if (y <= 0 || y >= height - sizer)
//            {
//                velocity_y = -velocity_y;
//            }
//            x += rand_x * (1 + wsp * speed_boost) * velocity_x;
//            y += rand_y * (1 + wsp * speed_boost) * velocity_y;
//            game_time_counter();
//            repaint();
        }
    }


    public void move(int boost)
    {
        Random r = new Random();

        for (int i = 0 ; i < x_list.size() ; i++)
        {
            rand_x = -1 + (1 - -1) * r.nextDouble();
            rand_y = -1 + (1 - -1) * r.nextDouble();
            x_list.set(i, (int) (x_list.get(i) + rand_x * (1 + 0.5 * boost) * x_velocity_list.get(i)));
            y_list.set(i, (int) (y_list.get(i) + rand_y * (1 + 0.5 * boost) * y_velocity_list.get(i)));
            repaint();
        }
//        rand_x = -1 + (1 - -1) * r.nextDouble();
//        rand_y = -1 + (1 - -1) * r.nextDouble();
//        x += rand_x * (1 + 0.5 * boost) * velocity_x;
//        y += rand_y * (1 + 0.5 * boost) * velocity_y;
//        repaint();
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

        Graphics2D g2d = (Graphics2D) g;

        loadImage();
        //g.drawImage(enemy_image,(int)x,(int)y,sizer,sizer,this);

        for (int i = 0 ; i < 3 ; i++)
        {
            g.drawImage(enemy_image,x_list.get(i),y_list.get(i),sizer,sizer,this);
            rect_list.add(new Rectangle2D.Double(x_list.get(i), y_list.get(i), sizer, sizer));
        }
//        rect = new Rectangle2D.Double((int)x, (int)y, sizer, sizer);

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
