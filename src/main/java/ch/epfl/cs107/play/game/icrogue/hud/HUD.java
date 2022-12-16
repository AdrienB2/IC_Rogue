package ch.epfl.cs107.play.game.icrogue.hud;


import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;


public class HUD extends Entity {
    private PlayerHpDisplay hpDisplay;

    /**
     * Default Entity constructor
     */
    public HUD(ICRogue game) {
        super(DiscreteCoordinates.ORIGIN.toVector());
        hpDisplay = new PlayerHpDisplay();
    }

    public void updateHUD(int playerHP){
        hpDisplay.updateHpBar(playerHP);
    }

    @Override
    public void draw(Canvas canvas) {
        hpDisplay.draw(canvas);
    }
}
