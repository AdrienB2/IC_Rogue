package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Enemy extends ICRogueActor {
    private boolean isDead;
    private Sprite sprite;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Enemy(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        isDead = false;
    }

    /**
     * @return (boolean) True si l'ennemi est mort false sinon
     */
    public boolean isDead(){
        return isDead;
    }

    /**
     * Tue l'ennemi
     */
    public void kill(){
        isDead = true;
    }

    /**
     * Setter du sprite de l'ennemi
     * @param sprite Sprite de l'ennemi
     */
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
