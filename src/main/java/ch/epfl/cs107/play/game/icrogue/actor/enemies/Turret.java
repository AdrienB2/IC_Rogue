package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Turret  extends Enemy{
    private Orientation[] directions; // directions dans lesquelles la tour tire
    private final float COOLDOWN = 2.f; // temps entre deux tirs
    private float counter;// compte le temps écoulé depuis le dernier tir
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, Orientation[] directions) {
        super(area, orientation, position);
        setSprite(new Sprite("icrogue/static_npc", 1.25f, 1.25f, this));
        this.directions = directions;
        this.counter = 0;
    }

    @Override
    public void update(float deltaTime) {
        // on incrémente le compteur et vérifie si le temps écoulé depuis le dernier tir est supérieur ou égale au cooldown
        this.counter+=deltaTime;
        if(this.counter >= COOLDOWN){
            //On crée une flèche dans chaque direction et remet le compteur à 0
            for (Orientation orientation:
                    directions) {
                getOwnerArea().registerActor(new Arrow(getOwnerArea(), orientation, getCurrentMainCellCoordinates()));
                this.counter = 0;
            }
        }
        // On supprime la tour si elle n'a plus de vie
        if(isDead()){
            getOwnerArea().unregisterActor(this);
        }
        super.update(deltaTime);
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
}
