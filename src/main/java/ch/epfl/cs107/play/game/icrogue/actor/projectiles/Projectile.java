package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable {

    private static int DEFAULT_DAMAGE = 1;
    private static int DEFAULT_MOVE_DURATION = 10;
    private int frame;
    private int damage;
    private boolean isConsumed;
    protected Sprite sprite;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int damage, int frame) {
        super(area, orientation, position);
        this.damage = damage;
        this.frame = frame;
        this.isConsumed = false;
    }
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position){
        this(area, orientation, position, DEFAULT_DAMAGE, DEFAULT_MOVE_DURATION);
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    @Override
    public void consume() {
        this.isConsumed = true;
    }

    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        move(frame);
        super.update(deltaTime);
    }
}
