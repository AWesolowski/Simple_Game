import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.util.Random;


public class Game extends JPanel implements ActionListener
{
    private int liczbaPoziomow = 5;
    private float zmianastopniatrudnosci = 25;
    private float poczatkowaszerokoscobiektu = 8;

    private int width;
    private int height;

    private FileR fr = new FileR();
    private int scores = fr.geTsco();
    public int state = 0;
    private int lvlscore = fr.geTlvlsco();
    private double randd = 1;
    private double randy = 1;
    private int boost = fr.getboo();
    public double points = fr.geTpoi();
    private int miss = fr.geTmi();
    private Random generator = new Random();
    private Rectangle2D rect;
    private double x = 100, y = 100, velx = 0.7, vely = 0.7;
    private javax.swing.Timer t = new javax.swing.Timer(5,this);
    private long start = System.currentTimeMillis();
    private long time;
    private int sizer;
    private int count_lvl = 0;
    //public float wsp = fr.geTws();
    public float wsp = 0.2f;
    public int pointsMultiplier;

    public int lines_numb;
    public Records highscores_tab;
    public boolean file_exist;
    public boolean shorten;
    String g_name;
    private boolean was_changed = false;
    private BufferedWriter bw = null;

    private String filepath = "Config/Highscores.txt";
    private final String filepath_Background = "Images/";
    private final String filepath_Game_Images = "Images/Game/";
    private final String filepath_sounds = "Sounds/";


    private float objects_size;


    private final Sounds click = new Sounds();

    private JLabel TimeLeft;
    private JLabel score;
    private JLabel misses;
    private JLabel levels;
    private Image enemy_image;
    private Image background_image;
    private ImageIcon image;

    private MouseAdapter mouse_adapter;


    public Game(int gx, int gy, int multiplier)
    {
        width = gx;
        height = gy;
        pointsMultiplier = multiplier;

        objects_size = poczatkowaszerokoscobiektu/100;
        sizer = (int)(objects_size * (float)width);

        background_image = Toolkit.getDefaultToolkit().getImage(filepath_Background + "Game_Background.gif");

        this.setLayout(null);
        this.addComponentListener(new main_window());

        score = new JLabel("Score");
        add(score);
        scored();

        misses = new JLabel("Misses left");
        add(misses);
        misses();

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
        addMouseListener(mouse_adapter = new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                super.mouseClicked(me);
                click.playSound(filepath_sounds + "click.wav");

                if(rect.contains(me.getPoint()))
                {
                    x = generator.nextInt(width - sizer * 2) + sizer;
                    y = generator.nextInt(height - sizer * 2) + sizer;
                    scored();
                    repaint();
                }
                else {
                    if (pointsMultiplier != 1)
                    {
                        miss--;
                        System.out.println(miss);
                        if (miss == 0)
                        {
                            EndGame();
                        }
                        misses();
                        repaint();
                    }

                }
            }
        });
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
            boost++;

            boost = boost + 5;

            move(boost);
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


    public void misses(){
        misses.setBounds(15, 20, 300, 30);
        misses.setText("Misses Left: " + miss);
        misses.setForeground(Color.red);
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
        if ((time = System.currentTimeMillis() - start) / 1000F >= 100)
        {
            System.out.println("koniec");
            EndGame();
        }
        else {
            if (x <= 0 || x >= width - sizer)
            {
                velx = -velx;
            }
            if (y <= 0 || y >= height - sizer)
            {
                vely = -vely;
            }
            x += randd * (1 + wsp * boost) * velx;
            y += randy * (1 + wsp * boost) * vely;
            game_time_counter();
            repaint();
        }
    }


    public void move(int boost)
    {
        Random r = new Random();
        randd = -1 + (1 - -1) * r.nextDouble();
        randy = -1 + (1 - -1) * r.nextDouble();
        x += randd * (1 + 0.5 * boost) * velx;
        y += randy * (1 + 0.5 * boost) * vely;
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

        Graphics2D g2d = (Graphics2D) g;

        loadImage();
        g.drawImage(enemy_image,(int)x,(int)y,sizer,sizer,this);

        rect = new Rectangle2D.Double((int)x, (int)y, sizer, sizer);

        BufferedImage bi = new BufferedImage(sizer, sizer, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics().drawImage(enemy_image, (int)x, (int)y, this);


        TexturePaint tp = new TexturePaint(bi, rect);
        g2d.setPaint(tp);
        g2d.fill(rect);

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
