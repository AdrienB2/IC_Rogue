package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bombe extends ICRogueActor implements Interactor {
    private static final int EXPLOSION_DAMAGE = 2;// dégâts causés par l'explosion
    private static final int EXPLOSION_COOLDOWN = 3; // temps d'attente avant l'explosion (en secondes)
    private static final int EXPLOSION_DURATION = 1; // durée de l'explosion (en secondes)
    private BombeInteractionHandler handler; // Gestionnaire d'interactions
    private boolean damageGiven = false; // indique si les dégâts ont déjà été donnés pour ne pas donner plusieurs fois des dégâts au joueur
    private float explosionTimer = 0; // temps écoulé depuis la création de la bombe
    private float spriteTimer = 1f; // temps entre chaque changement de sprite
    private float spriteTimeCounter = 0; // temps écoulé depuis le dernier changement de sprite
    private float timeSinceExplosion = 0;

    private boolean isExploding = false; // indique si la bombe est en train d'exploser
    private Sprite[] sprites; // sprite de la bombe
    private int currentSprite = 0; // sprite actuel de la bombe
    private Sprite[] explosionSprite; // sprite de l'explosion
    private Animation explosionAnimation; // animation de l'explosion

    public Bombe(Area area, DiscreteCoordinates position) {
        super(area, Orientation.UP, position);
        ICRogue.playSE(6); // joue le son de la bombe
        sprites = Sprite.extractSprites("zelda/bomb", 2, 1, 1, this, 16, 16);
        explosionSprite = Sprite.extractSprites("zelda/explosion", 7, 1.2f, 1.2f, this, 32, 32);
        explosionTimer = EXPLOSION_COOLDOWN;
        //inverse l'ordre des sprites pour que l'animation soit dans le bon sens
        Sprite[] temp = new Sprite[explosionSprite.length];
        for (int i = 0; i < explosionSprite.length; i++) {
            temp[i] = explosionSprite[explosionSprite.length - 1 - i];
        }
        explosionSprite = temp;
        explosionAnimation = new Animation(1, explosionSprite, false);
        handler = new BombeInteractionHandler();
    }

    @Override
    public void update(float deltaTime) {
        //Si le temps d'attente est écoulé, la bombe explose
        explosionTimer -= deltaTime;
        if (explosionTimer <= 0 && !isExploding) {
            isExploding = true;
        }
        //Si la bombe est en train d'exploser, on compte le temps écoulé depuis l'explosion et on supprime la bombe après l'explosion
        if(isExploding){
            timeSinceExplosion += deltaTime;
            if (timeSinceExplosion >= EXPLOSION_DURATION) {
                getOwnerArea().unregisterActor(this);
            }
        }
        if (!isExploding) { // si la bombe n'est pas en train d'exploser, on change le sprite
            updateSprite(deltaTime);
        } else { // sinon on joue l'animation d'explosion
            explosionAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isExploding) { // si la bombe n'est pas en train d'exploser, on dessine le sprite
            sprites[currentSprite].draw(canvas);
        }
        else{ // sinon on dessine l'animation d'explosion
            explosionAnimation.draw(canvas);
        }
    }

    /**
     * Change le sprite de la bombe en fonction du temps écoulé
     * Le temps entre chaque changement de sprite est défini par spriteTimer et est de plus en plus court au fur et à mesure que la bombe est proche de l'explosion
     * @param dt (float): delta time
     */
    private void updateSprite(float dt) {
        spriteTimeCounter += dt;
        if (spriteTimeCounter >= spriteTimer) {
            currentSprite = (currentSprite == 0) ? 1 : 0; // choisi le sprite à afficher
            spriteTimer -= (EXPLOSION_COOLDOWN - spriteTimeCounter) * 1000 / (EXPLOSION_COOLDOWN * 50); // réduit le temps entre chaque changement de sprite
            if (spriteTimer < 0.1f) { // on ne veut pas que le temps entre chaque changement de sprite soit inférieur à 0.1f
                spriteTimer = 0.1f;
            }
            spriteTimeCounter = 0;
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return isExploding;
    }

    @Override
    public boolean wantsViewInteraction() {
        return isExploding;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {}


    private class BombeInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(ICRoguePlayer other, boolean isCellInteraction) {
            if(!damageGiven){
                other.takeDamages(EXPLOSION_DAMAGE);
                damageGiven = true;
            }
        }
    }
}
