package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Boss;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Log;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public class Fire extends Projectile {

    private FireInteractionHandler handler;

    /**
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, 5);
        handler = new FireInteractionHandler();
        setSprites(Sprite.extractSprites("zelda/explosion", 7, 1.2f,1.2f, this,32,32), false, true, 4);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isConsumed()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class FireInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell other, boolean isCellInteraction) {
            if (other.getType() == ICRogueBehavior.ICRogueCellType.HOLE || other.getType() == ICRogueBehavior.ICRogueCellType.WALL){
                consume();
            }
        }
        @Override
        public void interactWith(Turret other, boolean isCellInteraction) {
            if(isCellInteraction){
                other.takeDamage(damage);
                consume();
            }
        }
        @Override
        public void interactWith(Log other, boolean isCellInteraction) {
            if(isCellInteraction){
                other.takeDamage(damage);
                consume();
            }
        }
        @Override
        public void interactWith(Boss other, boolean isCellInteraction) {
            other.takeDamage(damage);
            consume();
        }

        @Override
        public void interactWith(Arrow other, boolean isCellInteraction) {
            other.consume();
            consume();
        }

    }
}
