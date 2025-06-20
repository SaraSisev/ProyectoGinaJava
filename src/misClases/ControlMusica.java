
package misClases;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
public class ControlMusica {
    private static Clip clip;
    private static boolean pausada = false;

    public static void iniciarMusica(String ruta) {
        try {
            if (clip == null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ControlMusica.class.getResource(ruta));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else if (pausada) {
                clip.setMicrosecondPosition(0);
                clip.start();
                pausada = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pausarReanudar() {
        if (clip != null) {
            if (pausada) {
                clip.start();
                pausada = false;
            } else {
                clip.stop();
                pausada = true;
            }
        }
    }

    public static boolean estaPausada() {
        return pausada;
    }
}
