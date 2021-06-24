import java.io.*;
import java.util.Properties;


public class Server_Parser {
    private int speed_boost;
    private int collisions_available;
    private double speed_parameter;
    private int number_of_levels;
    private double difficulty_change;
    private double starting_object_size;
    private int number_of_enemies;

    public int getSpeed_boost(){return speed_boost;}
    public int getCollisions_available(){return collisions_available;}
    public double getSpeed_parameter(){return speed_parameter;}
    public int getNumber_of_levels(){return number_of_levels;}
    public double getDifficulty_change(){return difficulty_change;}
    public double getStarting_object_size(){return starting_object_size;}
    public int getNumber_of_enemies(){return number_of_enemies;}


    /**
     * konstruktor zawierjący parser potrzebny do pobrania informacji z pliku parametrycznego innego niż np. .txt
     */
    public Server_Parser(){
        try (InputStream input = new FileInputStream("Config/Level.props")){
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            speed_boost=Integer.parseInt(prop.getProperty("speed_boost"));
            collisions_available=Integer.parseInt(prop.getProperty("collisions_available"));
            speed_parameter=Integer.parseInt(prop.getProperty("speed_parameter"));
            number_of_levels=Integer.parseInt(prop.getProperty("number_of_levels"));
            difficulty_change=Float.parseFloat(prop.getProperty("difficulty_change"));
            starting_object_size=Float.parseFloat(prop.getProperty("starting_object_size"));
            number_of_enemies = Integer.parseInt(prop.getProperty("number_of_enemies"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}