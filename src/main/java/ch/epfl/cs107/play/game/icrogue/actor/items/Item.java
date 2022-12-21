package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Item extends CollectableAreaEntity {

    private Animation animation; // Animation de l'objet

    /**
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
    public Item(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * Setter du sprite de l'item si l'item possède des animations
     * @param sprites (Sprite[][]) : tableau de sprites de l'ennemi
     */
    public void setSprites(Sprite[] sprites){
        animation = new Animation(3, sprites, true);
    }
    /**
     * Setter du sprite de l'item si l'item n'a pas d'animations
     * @param sprite Sprite de l'item
     */
    public void setSprite(Sprite sprite){
        animation = new Animation(3, new Sprite[]{sprite}, true);
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
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()){
            animation.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
        super.update(deltaTime);
    }
}
