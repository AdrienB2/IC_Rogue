package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Heal extends Item {
    private int hpGiven; // Montant de points de vie donnés par l'objet

    /**
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinates): Initial position of the entity in the Area. Not null
     * @param hp (int): Montant de points de vie donnés par l'objet
     */
    public Heal(Area area, Orientation orientation, DiscreteCoordinates position, int hp) {
        super(area, orientation, position);
        hpGiven = hp;
    }

    /**
     * @return (int): Montant de points de vie donnés par l'objet
     */
    public int getHpGiven() {
        return hpGiven;
    }
}
