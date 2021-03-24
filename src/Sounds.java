import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sounds
{
    private String filepath = "Sounds/";

    private Map<String, Clip> sounds = new HashMap<>();


    public Sounds()
    {
        loadResources(filepath + "hit.wav");
        loadResources(filepath + "button.wav");
        loadResources(filepath + "click.wav");
    }


    private void loadResources(String soundName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            sounds.put(soundName, clip);
        } catch (Exception ex)
        {
            System.out.println("Error with loading sounds");
            ex.printStackTrace();
        }
    }


    public void playSound(String soundName)
    {
        Clip clip = sounds.get(soundName);
        clip.setFramePosition(0);
        clip.start();
    }
}
