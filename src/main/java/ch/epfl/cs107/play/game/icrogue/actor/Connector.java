package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity {
    public enum ICRogueConnectorState {
        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE;
    }

    private static int NO_KEY_ID = Integer.MAX_VALUE;
    private ICRogueConnectorState state;
    private String destination;
    private DiscreteCoordinates arrivalCoordinates;
    private int keyID;
    private Sprite sprite;
    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Connector(Area area, Orientation orientation, DiscreteCoordinates position, ICRogueConnectorState state, String destination, DiscreteCoordinates arrivalCoordinates, int keyID) {
        super(area, orientation, position);
        this.state = state;
        this.destination = destination;
        this.arrivalCoordinates = arrivalCoordinates;
        this.keyID = keyID;
        this.sprite = selectSprite(state);
    }
    public Connector(Area area, Orientation orientation, DiscreteCoordinates position, ICRogueConnectorState state, String destination, DiscreteCoordinates arrivalCoordinates) {
        this(area, orientation,position, state, destination, arrivalCoordinates, NO_KEY_ID);
    }

    public void setArrivalCoordinates(DiscreteCoordinates arrivalCoordinates) {
        this.arrivalCoordinates = arrivalCoordinates;
    }

    public DiscreteCoordinates getArrivalCoordinates(){
        return arrivalCoordinates;
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != ICRogueConnectorState.OPEN){
            sprite.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord, coord.jump(new Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));
    }

    private Sprite selectSprite(ICRogueConnectorState state){
        switch (state){
            case CLOSED -> {
                return new Sprite("icrogue/door_"+this.getOrientation().opposite().ordinal(), (this.getOrientation().ordinal()+1)%2+1, this.getOrientation().ordinal()%2+1, this);
            }
            case LOCKED -> {
                return new Sprite("icrogue/lockedDoor_"+this.getOrientation().opposite().ordinal(), (this.getOrientation().ordinal()+1)%2+1, this.getOrientation().ordinal()%2+1, this);
            }
        }
        return new Sprite("icrogue/invisibleDoor_"+this.getOrientation().opposite().ordinal(), (this.getOrientation().ordinal()+1)%2+1, this.getOrientation().ordinal()%2+1, this);
    }

    @Override
    public boolean takeCellSpace() {
        return state != ICRogueConnectorState.OPEN;
    }
    public void setState(ICRogueConnectorState state){
        this.state = state;
        sprite = selectSprite(state);
    }
    @Override
    public boolean isCellInteractable() {
        return state == ICRogueConnectorState.OPEN;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
    public void openConnector(){
        if (this.state != ICRogueConnectorState.LOCKED && this.state !=ICRogueConnectorState.INVISIBLE) {
            this.state = ICRogueConnectorState.OPEN;
        }
    }
    public void closeConnector(){
        if(this.state != ICRogueConnectorState.LOCKED) this.state = ICRogueConnectorState.CLOSED;
    }
    public void lockConnector(int id){
        this.state = ICRogueConnectorState.LOCKED;
        this.keyID = id;
        this.sprite = selectSprite(state);
    }

    public int unLock(List<Integer> playerKeys){
        if(keyID == Integer.MAX_VALUE){
            state = ICRogueConnectorState.OPEN;
            return -1;
        }
        for (int i = 0;  i<playerKeys.size() ; i++) {
            if(playerKeys.get(i) == keyID){
                state = ICRogueConnectorState.OPEN;
                return i;
            }
        }
        return -1;
    }
    public void switchState(){
        if(this.state == ICRogueConnectorState.OPEN) this.state=ICRogueConnectorState.CLOSED;
        else if (this.state == ICRogueConnectorState.CLOSED) this.state = ICRogueConnectorState.OPEN;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        return destination;
    }
}