package ch.epfl.cs107.play.game.icrogue.Screens;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Text;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;

/**
 * Ecran de pause
 */
public class PauseScreen extends Area {

    private String text = "PAUSE";
    private int letterIndex = 0;
    private float letterAppearDelay = 0.15f;
    private float count = letterAppearDelay;
    private int xCorrection;

    public PauseScreen(){
        xCorrection = 5-(text.length()/2);
    }

    @Override
    public int getHeight() {
        return 550;
    }

    @Override
    public int getWidth() {
        return 550;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            letterIndex = 0;
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        count += deltaTime;
        if (count >= letterAppearDelay){
            count = 0;
            if(letterIndex < text.length()){
                registerActor(new Text(text.substring(letterIndex, letterIndex+1), new DiscreteCoordinates(xCorrection + letterIndex,5), this, true, 1f, Color.white, 1, 0));
                letterIndex++;
            }
        }
        super.update(deltaTime);
    }

    @Override
    public String getTitle() {
        return "Pause";
    }

    @Override
    public float getCameraScaleFactor() {
        return 10;
    }
}
