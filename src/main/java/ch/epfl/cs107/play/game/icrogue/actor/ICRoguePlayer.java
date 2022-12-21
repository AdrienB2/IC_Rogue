package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor{
    private int hp;
    private final int maxHP = 10;
    private boolean canUseFire;
    protected boolean isChangingRoom;
    private Sprite[][] sprites;
    private Animation[] animations;


    public ICRoguePlayerInteractionHandler handler;
    private String destinationRoom;
    private DiscreteCoordinates destinationCoordinates;
    private final static int MOVE_DURATION = 6;

    private ArrayList<Integer> keys = new ArrayList<>();

    private final float INVULNERABILITY_DURATION = 0.5f;
    private float invulnerabilityTimer = 0;
    private boolean isInvulnerable = false;

    private final float audioDuration = 0.2f;
    private float audioTimer = 0;

    /**
     * @param owner (Area): Owner area
     * @param orientation (Orientation): Initial orientation of the entity
     * @param coordinates (DiscreteCoordinates): Initial position of the entity
     * @param spriteName (String): Name of the sprite
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        this.hp = 10;
        sprites = Sprite.extractSprites(spriteName, 4, 1, 2,this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        animations = Animation.createAnimations(MOVE_DURATION/2, sprites);
        handler = new ICRoguePlayerInteractionHandler();
        canUseFire = false;
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        if (isInvulnerable){
            invulnerabilityTimer += deltaTime;
            if (invulnerabilityTimer >= INVULNERABILITY_DURATION){
                isInvulnerable = false;
                invulnerabilityTimer = 0;
            }
        }
        isChangingRoom = false;

        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        if(isDisplacementOccurs()) {
            animations[getOrientation().ordinal()].update(deltaTime); // joue l'animation de déplacement si le joueur se déplace
            if(audioTimer >= audioDuration){
                audioTimer = 0;
                ICRogue.playSE(5);
            }
            audioTimer += deltaTime;
        }
        else {
            animations[getOrientation().ordinal()].reset(); // reset l'animation de déplacement si le joueur ne se déplace pas
            audioTimer = 0;
        }
        if(keyboard.get(Keyboard.X).isPressed() && canUseFire){ // Si le joueur appuie sur X et qu'il peut utiliser le feu on crée un projectile de feu
            getOwnerArea().registerActor(new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()));
            ICRogue.playSE(4);
        }
        super.update(deltaTime);
    }
    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }
    public void resetHP(){
        hp = maxHP;
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Indique si le joueur est en train de changer de salle
     * @return (boolean) : True si le joueur change de salle, false sinon
     */
    public boolean isChangingRoom() {
        return isChangingRoom;
    }

    /**
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        ((ICRogueRoom)area).setVisited();
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Inflige un certain nombre de points de dommage au joueur
     * @param damage (float) : nombre de points de dommage infligé
     */
    public void takeDamages(float damage){
        if (!isInvulnerable){
            ICRogue.playSE(9);
            this.hp -= damage;
            this.isInvulnerable = true;
        }
    }

    /**
     * Getter du nombre de points de vie
     * @return (float) : nombre de points de vie restant au joueur
     */
    public float getHp(){
            return hp;
    }
    @Override
    public void draw(Canvas canvas) {
        animations[getOrientation().ordinal()].draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * Permet de redonner de la vie au joueur
     * @param hpGiven (int): nombre de points de vie donnés
     */
    public void heal(int hpGiven){
        hp += hpGiven;
        if(hp>maxHP){
            hp = maxHP;
        }
    }

    /**
     * @return (boolean) : True si le joueur a une clé, false sinon
     */
    public boolean hasKey(){
        return this.keys.size() > 0;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isPressed();
    }

    /**
     * Getter du nom de la salle d'arrivée
     * @return (String) : Nom de la salle dans laquelle le joueur essaie d'aller
     */
    public String getDestinationRoom(){
        return destinationRoom;
    }

    /**
     * Getter des coordonnées dans la salle d'arrivée
     * @return (DiscreteCoordinates) : Coordonnées d'arrivée dans la salle dans laquelle le joueur essaie d'aller
     */
    public DiscreteCoordinates getArrivalCoordinate(){
        return destinationCoordinates;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(Heal other, boolean isCellInteraction) {
            ICRogue.playSE(10);
            other.collect();
            heal(other.getHpGiven());
        }
        @Override
        public void interactWith(Staff other, boolean isCellInteraction) {
            other.collect();
            canUseFire = true;
        }
        @Override
        public void interactWith(Key other, boolean isCellInteraction) {
            other.collect();
            keys.add(other.getId());
        }

        @Override
        public void interactWith(Connector other, boolean isCellInteraction) {
            if(!isCellInteraction){
                int keyUsed = other.unLock(keys);
                if(keyUsed!= -1){
                    keys.remove(keyUsed);
                }
            }
            else if(!isDisplacementOccurs()){
                isChangingRoom = true;
                destinationRoom = other.getDestination();
                destinationCoordinates = other.getArrivalCoordinates();
            }
        }
        @Override
        public void interactWith(Turret other, boolean isCellInteraction) {
            other.takeDamage(1);
        }
    }
}