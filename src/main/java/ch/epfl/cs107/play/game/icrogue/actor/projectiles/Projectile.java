package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor {

    private static int DEFAULT_DAMAGE = 1;
    private static int DEFAULT_MOVE_DURATION = 10;
    private int moveDuration;
    protected int damage;
    private boolean isConsumed;
    protected Sprite[] sprites;
    private Animation animation;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int damage, int moveDuration) {
        super(area, orientation, position);
        this.damage = damage;
        this.moveDuration = moveDuration;
        this.isConsumed = false;
    }

    /**
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position (DisceteCoordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position){
        this(area, orientation, position, DEFAULT_DAMAGE, DEFAULT_MOVE_DURATION);
    }


    @Override
    public boolean wantsCellInteraction() {
        return !isConsumed();
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }


    @Override
    public boolean isCellInteractable() {
        return !isConsumed();
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Setter du sprite du projectile si le projectile n'a pas d'animations
     * @param sprite Sprite du projectile
     */
    public void setSprite(Sprite sprite){
        this.sprites = new Sprite[]{sprite};
        this.animation = new Animation(3, this.sprites, true);
    }

    /**
     * Setter des sprites du projectile si le projectile a des animations
     * @param sprites (Sprite[]) : sprites du projectile
     * @param repeatAnimation (boolean) : indique si l'animation doit être répétée
     * @param inverseSprites (boolean) : indique si les sprites doivent être inversés
     * @param frameDuration (int) : durée d'une frame
     */
    public void setSprites(Sprite[] sprites, boolean repeatAnimation, boolean inverseSprites, int frameDuration){
        this.sprites = sprites;
        if(inverseSprites) {
            for (int i = 0; i < this.sprites.length / 2; i++) {
                Sprite temp = this.sprites[i];
                this.sprites[i] = this.sprites[this.sprites.length - i - 1];
                this.sprites[this.sprites.length - i - 1] = temp;
            }
        }

        this.animation = new Animation(frameDuration, this.sprites, repeatAnimation);
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
        move(moveDuration);
        animation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if(!isConsumed){
            animation.draw(canvas);
        }
    }
}
