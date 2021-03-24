import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Music
{
    private float vol = - 20.0f;
    private Clip clip;
    private long cliptime;


    public float getVolume()
    {
        return vol;
    }


    public void setVolume(float given_volume)
    {
        vol = given_volume;
    }


    void CustomSoundBackground(String filepath_music) {
        try {
            File musicPath = new File(filepath_music);

            if (musicPath.exists())
            {
                AudioInputStream AudioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(AudioInput);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                volume.setValue(vol);

                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {
                System.out.println("Background music file does not exist");
            }
        } catch (Exception ex)
        {
            System.out.println("Background music filepath is incorrect");
            ex.printStackTrace();
        }
    }


    public void stopMusic()
    {
        clip.stop();
        clip.close();
    }


    public long pauseMusic()
    {
        cliptime = clip.getMicrosecondPosition();
        clip.stop();
        return cliptime;
    }


    public void resumeMusic(long clip_moment)
    {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(vol);
        clip.setMicrosecondPosition(clip_moment);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void muteMusic(long clip_moment)
    {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(volume.getMinimum());
        clip.setMicrosecondPosition(clip_moment);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
