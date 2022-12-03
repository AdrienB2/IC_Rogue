package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {

    private ICRogueBehavior behavior;

    protected DiscreteCoordinates position;
    protected static String behaviorName;
    protected ArrayList<Connector> connectors = new ArrayList<>();

    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations, String behaviorName, DiscreteCoordinates roomCoordinates){
        this.behaviorName = behaviorName;
        this.position = roomCoordinates;

        for (int i = 0; i<connectorsCoordinates.size(); i++) {
            connectors.add(new Connector(this, orientations.get(i), connectorsCoordinates.get(i), Connector.ICRogueConnectorState.INVISIBLE, "", new DiscreteCoordinates(0,0)));
        }
    }

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    /// EnigmeArea extends Area

    @Override
    public final float getCameraScaleFactor() {
        return 11;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    /// Demo2Area implements Playable

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, behaviorName);
            setBehavior(behavior);
            createArea();
            for (Connector connector: connectors) {
                registerActor(connector);
            }
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getKeyboard();
        if(keyboard.get(Keyboard.O).isPressed()){
            for (Connector connector:
                 connectors) {
                connector.openConnector();
            }
        }
        if (keyboard.get(Keyboard.L).isPressed()){
            connectors.get(0).lockConnector(1);
        }
        if(keyboard.get(Keyboard.T).isPressed()){
            for (Connector connector:
                connectors) {
            connector.switchState();
        }
        }
        super.update(deltaTime);
    }
}
