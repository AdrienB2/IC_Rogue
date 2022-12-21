package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Enemy extends ICRogueActor {
    private boolean isDead; // indique si l'ennemi est mort
    private Sprite[][] sprites;
    private Animation[] animations;
    private int currentAnim; // représente l'index de l'animation courante
    protected int MOVE_DURATION = 6;
    protected int hp = 1; // représente les points de vie de l'ennemi (par défaut 1)

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
     * Indique si l'ennemi est mort
     * @return (boolean) True si l'ennemi est mort false sinon
     */
    public boolean isDead(){
        return isDead;
    }

    /**
     * Tue l'ennemi
     */
    protected void kill(){
        isDead = true;
    }


    /**
     * Permet d'infliger des dégâts à l'ennemi
     * @param damage (int) : dégâts infligés à l'ennemi
     */
    public void takeDamage(int damage){
        ICRogue.playSE(8);
        hp -= damage;

        if(hp <= 0){
            kill();
        }
    }

    /**
     * Setter du sprite de l'ennemi si l'ennemi n'a pas d'animations
     * @param sprite Sprite de l'ennemi
     */
    protected void setSprite(Sprite sprite){
        this.sprites = new Sprite[][] {{sprite}};
        animations = new Animation[1];
        animations[0] = new Animation(MOVE_DURATION/2, this.sprites[0]);
        currentAnim = 0;
    }

    /**
     * Setter du sprite de l'ennemi si l'ennemi possède des animations
     * @param sprites (Sprite[][]) : tableau de sprites de l'ennemi
     */
    protected void setSprites(Sprite[][] sprites){
        this.sprites = sprites;
        animations = Animation.createAnimations(MOVE_DURATION/2, this.sprites, true);
    }

    /**
     * Setter pour la depth du sprite de l'ennemi
     * @param depth (int) : profondeur de l'ennemi
     */
    protected void setSpritesDepth(float depth){
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(depth);
            }
        }
    }
    @Override
    public void draw(Canvas canvas) {
        animations[currentAnim].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        currentAnim = this.getOrientation().ordinal();
        if(this.isDisplacementOccurs()){
            animations[currentAnim].update(deltaTime);
        }
        else {
            animations[currentAnim].switchPause();
        }
        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
