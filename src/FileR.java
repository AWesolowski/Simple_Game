import java.io.*;
import java.util.Properties;


public class FileR {
    private int scores ;
    private int lvlscore ;
    private int boost ;
    private int miss ;
    private float points ;
    public float wsp ;

    public int geTsco(){return scores;}
    public int geTlvlsco(){return lvlscore;}
    public int getboo(){return boost;}
    public int geTmi(){return miss;}
    public float geTpoi(){return points;}
    public float geTws(){return wsp;}
    /**
     * konstruktor zawierjący parser potrzebny do pobrania informacji z pliku parametrycznego innego niż np. .txt
     */

    public FileR(){
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}