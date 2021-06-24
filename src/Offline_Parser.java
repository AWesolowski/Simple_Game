import java.io.*;
import java.util.Properties;


public class Offline_Parser {
    private int scores;
    private int lvlscore;
    private int boost;
    private int miss;
    private float points;
    private float wsp;
    private int numberOfLevels;
    private double zmianaStopniaTrudnosci;
    private double poczatkowaSzerokoscObiektu;

    public int geTsco(){return scores;}
    public int geTlvlsco(){return lvlscore;}
    public int getboo(){return boost;}
    public int geTmi(){return miss;}
    public float geTpoi(){return points;}
    public float geTws(){return wsp;}
    public int getNumberOfLevels(){return numberOfLevels;}
    public double getZmianaStopniaTrudnosci(){return zmianaStopniaTrudnosci;}
    public double getPoczatkowaszerokoscObiektu(){return poczatkowaSzerokoscObiektu;}


    /**
     * konstruktor zawierjący parser potrzebny do pobrania informacji z pliku parametrycznego innego niż np. .txt
     */
    public Offline_Parser(){
        try (InputStream input = new FileInputStream("Config/Level.props")){
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            scores=Integer.parseInt(prop.getProperty("scores"));
            lvlscore=Integer.parseInt(prop.getProperty("lvlscore"));
            boost=Integer.parseInt(prop.getProperty("boost"));
            miss=Integer.parseInt(prop.getProperty("miss"));
            points=Float.parseFloat(prop.getProperty("points"));
            wsp=Float.parseFloat(prop.getProperty("scores"));
            numberOfLevels = Integer.parseInt(prop.getProperty("number_of_levels"));
            zmianaStopniaTrudnosci = Double.parseDouble(prop.getProperty("zmianaStopniatrudnosci"));
            poczatkowaSzerokoscObiektu = Double.parseDouble(prop.getProperty("poczatkowaszerokoscobiektu"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}