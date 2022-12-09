package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Level implements Logic {
    protected ICRogueRoom[][] map;
    protected DiscreteCoordinates arrivalCoordinates;
    protected DiscreteCoordinates bossRoom;
    protected String firstRoomTitle;

    /**
     * @param arrivalCoordinates Coordonnées de départ dans les salles
     * @param width Largeur de la carte
     * @param height Hauteur de la salle
     */
    public Level(DiscreteCoordinates arrivalCoordinates, int width, int height){
        this.arrivalCoordinates = arrivalCoordinates;
        this.map = new ICRogueRoom[width][height];
        this.bossRoom = new DiscreteCoordinates(0, 0);
        generateFixedMap();
    }

    /**
     * @param coords Position de la salle
     * @param room Salle
     */
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        this.map[coords.x][coords.y] = room;
    }

    /**
     * @param coords Position de la salle
     * @param destination Nom de la salle d'arrivée
     * @param connector Connecteur à modifier
     */
    protected  void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setDestination(destination);
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setArrivalCoordinates(connector.getDestination());
    }

    /**
     * @param coords Position de la salle
     * @param destination Nom de la salle d'arrivée
     * @param connector Connecteur à modifier
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnector(coords, destination, connector, Connector.ICRogueConnectorState.INVISIBLE);
    }

    /**
     * @param coords Position de la salle
     * @param destination Non de la salle d'arrivée
     * @param connector Connecteur à modifier
     * @param state état du connecteur (INVISIBLE, OPEN, CLOSE)
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector, Connector.ICRogueConnectorState state){
        setRoomConnectorDestination(coords, destination, connector);
        map[coords.x][coords.y].connectors.get(connector.getIndex()).closeConnector();
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setState(state);
    }

    /**
     * @param coords Position de la salle
     * @param connector Connecteur à modifier
     * @param keyId Identifiant de la clé
     */
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        map[coords.x][coords.y].connectors.get(connector.getIndex()).lockConnector(keyId);
    }

    /**
     * @param coords Position de la salle de départ
     */
    public void setFirstRoomTitle(DiscreteCoordinates coords) {
        this.firstRoomTitle = map[coords.x][coords.y].getTitle();
    }

    public String getFirstRoomTitle() {
        return firstRoomTitle;
    }
    public DiscreteCoordinates getArrivalCoordinates(){
        return arrivalCoordinates;
    }

    protected abstract void generateFixedMap();

    @Override
    public boolean isOn() {
        return (map[bossRoom.x][bossRoom.y]!=null&&map[bossRoom.x][bossRoom.y].isOn());
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }

    @Override
    public float getIntensity() {
        return 0;
    }
}