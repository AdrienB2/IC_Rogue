package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0StaffRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0TurretRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends Level {

    public Level0(ICRogue game) {
        super(new DiscreteCoordinates(1,5), 4,2);
        setFirstRoomTitle(new DiscreteCoordinates(1,0));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
               if(map[i][j]!=null){
                   game.addArea(map[i][j]);
               }
            }
        }
    }

    @Override
    protected void generateFixedMap() {
        //generateMap1();
        generateMap2();
    }
    private void generateMap1(){
        int keyID = 1;

        //création de la salle 00
        DiscreteCoordinates room00 = new DiscreteCoordinates(0,0);
        setRoom(room00, new Level0KeyRoom(room00, keyID));
        //mise en place du connecteur
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        //Verrouillage de la porte
        lockRoomConnector(room00, Level0Room.Level0Connectors.E, keyID);

        //création de la salle 01
        DiscreteCoordinates room01 = new DiscreteCoordinates(0,1);
        setRoom(room01, new Level0Room(room01));
        setRoomConnector(room01, "icrogue/level000", Level0Room.Level0Connectors.W, Connector.ICRogueConnectorState.OPEN);
    }

    private void generateMap2(){
        int BOSS_KEY_ID = 2;
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level001", Level0Room.Level0Connectors.E, Connector.ICRogueConnectorState.CLOSED);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S, Connector.ICRogueConnectorState.CLOSED);
        setRoomConnector(room10, "icrogue/level002", Level0Room.Level0Connectors.E, Connector.ICRogueConnectorState.CLOSED);

        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level001", Level0Room.Level0Connectors.W, Connector.ICRogueConnectorState.CLOSED);
        setRoomConnector(room20, "icrogue/level003", Level0Room.Level0Connectors.E, Connector.ICRogueConnectorState.CLOSED);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level002", Level0Room.Level0Connectors.W, Connector.ICRogueConnectorState.CLOSED);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level001", Level0Room.Level0Connectors.N, Connector.ICRogueConnectorState.CLOSED);

    }
}
