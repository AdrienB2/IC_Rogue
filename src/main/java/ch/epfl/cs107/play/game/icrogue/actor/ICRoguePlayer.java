package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
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
    private float hp;
    private boolean canUseFire;
    protected boolean isChangingRoom;
    private TextGraphics message;
    private Sprite[][] sprites;
    private Animation[] animations;

    /// Animation duration in frame number

    public ICRoguePlayerInteractionHandler handler;
    private String destinationRoom;
    private DiscreteCoordinates destinationCoordinates;
    private final static int MOVE_DURATION = 6;

    private ArrayList<Integer> keys = new ArrayList<>();
    /**
     * Demo actor
     *
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        this.hp = 10;
        message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        sprites = Sprite.extractSprites("zelda/player", 4, 1, 2,this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        animations = Animation.createAnimations(MOVE_DURATION/2, sprites);
        handler = new ICRoguePlayerInteractionHandler();
        canUseFire = false;
        resetMotion();
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    @Override
    public void update(float deltaTime) {
        isChangingRoom = false;
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        if(isDisplacementOccurs()) {
            animations[getOrientation().ordinal()].update(deltaTime);
        }
        else {
            animations[getOrientation().ordinal()].reset();
        }
        if(keyboard.get(Keyboard.X).isPressed() && canUseFire){
            getOwnerArea().registerActor(new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()));
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

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    public boolean isChangingRoom() {
        return isChangingRoom;
    }

    /**
     *
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
    public void takeDamages(float damage){
        this.hp -= damage;
    }
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

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isDown();
    }

    public String getDestinationRoom(){
        return destinationRoom;
    }
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
        public void interactWith(Cherry other, boolean isCellInteraction) {
            other.collect();
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
            if(isCellInteraction){
                other.kill();
            }
        }
    }
}