package ch.epfl.cs107.play.game;

import ch.epfl.cs107.play.game.icrogue.ICRogue;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


/**
 * Recupère les sons depuis la ressource sounds et les assignes a un URL
 */
public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];


    public Sound() {
        soundURL[0] = getClass().getResource("/sounds/music.wav");
        soundURL[1] = getClass().getResource("/sounds/FinalBattle.wav");
        soundURL[2] = getClass().getResource("/sounds/gameover.wav");
        soundURL[3] = getClass().getResource("/sounds/win.wav");
        soundURL[4] = getClass().getResource("/sounds/burning.wav");
        soundURL[5] = getClass().getResource("/sounds/boots.wav");
        soundURL[6] = getClass().getResource("/sounds/bomb.wav");
        soundURL[7] = getClass().getResource("/sounds/dialogNext.wav");
        soundURL[8] = getClass().getResource("/sounds/hitmonster.wav");
        soundURL[9] = getClass().getResource("/sounds/receivedamage.wav");
        soundURL[10] = getClass().getResource("/sounds/heart.wav");
    }

    /**
     * @param i Les paramètre i est la valeur à laquelle les sons est assigné
     */
    public void setFile(int i) {
        try {
           try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
               clip = AudioSystem.getClip();
               clip.open(ais);
           }

        } catch (Exception e) {

        }
    }


    /**
     * Méthode qui lance le son (play)
     */
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Méthode qui permet de recommencer le son une fois fini (Loop)
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Méthode qui permet de stopper le son
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Méthode qui permet de fermer le son
     */
    public void close() {
        clip.close();
    }
}