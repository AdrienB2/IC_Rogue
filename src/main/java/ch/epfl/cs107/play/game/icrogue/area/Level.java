package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class Level implements Logic {
    protected ICRogueRoom[][] map;
    protected DiscreteCoordinates startPosition;
    protected DiscreteCoordinates bossRoom;
    protected String firstRoomTitle;


    /**
     * @param startPosition Coordonnées de départ dans les salles
     * @param width Largeur de la carte
     * @param height Hauteur de la salle
     */
    public Level(boolean randomMap, DiscreteCoordinates startPosition, int[] roomsDistribution, int width, int height){
        this.startPosition = startPosition;
        if(!randomMap){
            this.map = new ICRogueRoom[width][height];
            this.bossRoom = new DiscreteCoordinates(0, 0);
            generateFixedMap();
        }
        else{
            generateRandomMap(roomsDistribution);
        }
    }
    public Level(DiscreteCoordinates startPosition, int width, int height){
        this(false, startPosition, null, width, height);
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
     * Crée un connecteur verouillé
     * @param coords Position de la salle
     * @param destination Non de la salle d'arrivée
     * @param connector Connecteur à modifier
     * @param keyID (int) identifiant de la clé
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector, int keyID){
        setRoomConnector(coords, destination, connector, Connector.ICRogueConnectorState.LOCKED);
        lockRoomConnector(coords, connector, keyID);
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

    protected abstract void generateFixedMap();

    //GENERATION ALEATOIRE DES NIVEAUX
    protected void generateRandomMap(int[] roomsDistribution){
        int nbRooms = 0;
        for (int i:
             roomsDistribution) {
            nbRooms += i;
        }
        this.map = new ICRogueRoom[nbRooms][nbRooms];
        MapState[][] roomsPlacement = generateRandomRoomPlacement();
        createRooms(roomsPlacement, roomsDistribution);
        for (ICRogueRoom[] icRogueRooms : map) {
            for (ICRogueRoom icRogueRoom : icRogueRooms) {
                setUpConnector(roomsPlacement, icRogueRoom);
            }

        }
    }
    protected MapState[][] generateRandomRoomPlacement(){
        //Initialisation de la map de placement des salles avec des salles NULL
        MapState[][] roomsPlacement = new MapState[map.length][map.length];
        for (int i = 0; i < roomsPlacement.length; i++) {
            for (int j = 0; j < roomsPlacement.length; j++) {
                roomsPlacement[i][j] = MapState.NULL;
            }
        }
        //On crée une liste de coordonnées des salles placées
        ArrayList<DiscreteCoordinates> placedRooms = new ArrayList<>();
        //On place la salle de départ au centre de la map
        roomsPlacement[map.length/2][map.length/2] = MapState.PLACED;
        placedRooms.add(new DiscreteCoordinates(map.length/2, map.length/2));

        //On place les autres salles
        int roomsToPlace = map.length - 1; // nombre de salles à placer
        while (roomsToPlace > 0){
            int nbPlacedRooms = placedRooms.size();
            //pour chaque salle placée, on place une salle autour
            for (int i = 0; i<nbPlacedRooms; i++) {
                if(roomsToPlace == 0) break; //si on a placé toutes les salles, on sort de la boucle
                //On récupère les coordonnées de la salle placée et on vérifie que la salle ne soit pas déjà explorée
                DiscreteCoordinates currentPlacedRoom = placedRooms.get(i);
                if(roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y] == MapState.EXPLORED) {
                    continue;
                }

                int nbFreeSlots = 0;//compte le nombre de cases libres autour de la salle
                ArrayList<DiscreteCoordinates> freeSlots = new ArrayList<>(); //liste des cases libres autour de la salle
                //On vérifie que les cases autour de la salle sont libres et on les ajoute à la liste des cases libres
                if(currentPlacedRoom.y - 1 >= 0
                        && roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y - 1] == MapState.NULL){
                    nbFreeSlots++;
                    freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x, currentPlacedRoom.y-1));
                }
                if(currentPlacedRoom.y + 1 < map.length
                        && roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y + 1] == MapState.NULL){
                    nbFreeSlots++;
                    freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x, currentPlacedRoom.y + 1));
                }
                if(currentPlacedRoom.x + 1 < map.length
                        && roomsPlacement[currentPlacedRoom.x + 1][currentPlacedRoom.y] == MapState.NULL){
                    nbFreeSlots++;
                    freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x + 1, currentPlacedRoom.y));
                }
                if(currentPlacedRoom.x - 1 >= 0
                        && roomsPlacement[currentPlacedRoom.x - 1][currentPlacedRoom.y] == MapState.NULL){
                    nbFreeSlots += 1;
                    freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x - 1, currentPlacedRoom.y));
                }

                if(nbFreeSlots == 0){continue;} //On vérifie qu'il y ait au moins une case libre autour de la salle
                int roomsToPlaceNow = RandomHelper.roomGenerator.nextInt(0, Integer.min(roomsToPlace, nbFreeSlots)); //On choisit le nombre de salles à placer autour de la salle
                if (roomsToPlaceNow == 0) roomsToPlaceNow++; //On place au moins une salle autour de la salle
                List<Integer> roomsIndexesList = IntStream.rangeClosed(0, freeSlots.size()-1).boxed().toList(); //On crée une liste des index des cases libres
                List<Integer> roomsChosenPosition = RandomHelper.chooseKInList(roomsToPlaceNow,roomsIndexesList); //On choisit aléatoirement les cases où placer les salles
                //On place les salles
                for (Integer posIndex:
                     roomsChosenPosition) {
                    DiscreteCoordinates pos = freeSlots.get(posIndex);
                    roomsPlacement[pos.x][pos.y] = MapState.PLACED;
                    placedRooms.add(pos);
                }
                //On marque la salle comme explorée et on décrémente le nombre de salles à placer
                roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y] = MapState.EXPLORED;
                roomsToPlace -= roomsToPlaceNow;
            }
        }

        //On place la salle du boss
        boolean isBoosRoomPlaced = false;
        int i = 0;
        while (!isBoosRoomPlaced && i<placedRooms.size()){ // On sort de la boucle si on a placé la salle du boss ou si on a parcouru toutes les salles placées
            DiscreteCoordinates currentPlacedRoom = placedRooms.get(i);
            int nbFreeSlots = 0; //compte le nombre de cases libres autour de la salle
            ArrayList<DiscreteCoordinates> freeSlots = new ArrayList<>(); //liste des cases libres autour de la salle

            //On vérifie que les cases autour de la salle sont libres et on les ajoute à la liste des cases libres
            if(currentPlacedRoom.y - 1 >= 0
                    && roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y - 1] == MapState.NULL){
                nbFreeSlots += 1;
                freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x, currentPlacedRoom.y-1));
            }
            if(currentPlacedRoom.y + 1 < map.length
                    && roomsPlacement[currentPlacedRoom.x][currentPlacedRoom.y + 1] == MapState.NULL){
                nbFreeSlots += 1;
                freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x, currentPlacedRoom.y + 1));
            }
            if(currentPlacedRoom.x + 1 < map.length
                    && roomsPlacement[currentPlacedRoom.x + 1][currentPlacedRoom.y] == MapState.NULL){
                nbFreeSlots += 1;
                freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x + 1, currentPlacedRoom.y));
            }
            if(currentPlacedRoom.x - 1 >= 0
                    && roomsPlacement[currentPlacedRoom.x - 1][currentPlacedRoom.y] == MapState.NULL){
                nbFreeSlots += 1;
                freeSlots.add(new DiscreteCoordinates(currentPlacedRoom.x - 1, currentPlacedRoom.y));
            }
            if(nbFreeSlots == 0){i++; continue;}

            //On choisit aléatoirement une case libre autour de la salle et on place la salle du boss
            List<Integer> roomsIndexesList = IntStream.rangeClosed(0, freeSlots.size()-1).boxed().toList();
            List<Integer> roomsChosenPosition =  RandomHelper.chooseKInList(1,roomsIndexesList);
            DiscreteCoordinates pos = freeSlots.get(roomsChosenPosition.get(0));
            roomsPlacement[pos.x][pos.y] = MapState.BOSS_ROOM;
            placedRooms.add(pos);
            isBoosRoomPlaced = true;
        }

        return roomsPlacement;
    }

    private void createRooms(MapState[][] roomsPlacement, int[] roomDistribution){
        ArrayList<DiscreteCoordinates> usableLocations = new ArrayList<>();
        //On cherche les salles libres
        for (int i = 0; i < roomsPlacement.length; i++) {
            for (int j = 0; j < roomsPlacement[i].length; j++) {
                if (roomsPlacement[i][j] == MapState.EXPLORED || roomsPlacement[i][j] == MapState.PLACED){
                    usableLocations.add(new DiscreteCoordinates(i,j));
                }
                else if (roomsPlacement[i][j] == MapState.BOSS_ROOM){ // Si c'est la salle du boss, on la crée
                    map[i][j] = createBoosRoom(new DiscreteCoordinates(i,j));
                    this.bossRoom = new DiscreteCoordinates(i,j);
                }
            }
        }
        //On crée les salles
        for(int i = 0; i<roomDistribution.length; i++){
            //On choisit aléatoirement les coordonnées pour chaque type de salle
            int k = roomDistribution[i]; //nombre de salles de type i à créer
            List<Integer> usableLocationsIndexesList = IntStream.rangeClosed(0, usableLocations.size()-1).boxed().toList();
            List<Integer> chosenLocationIndexes = RandomHelper.chooseKInList(k, usableLocationsIndexesList);
            List<DiscreteCoordinates> usedLocation = new ArrayList<>(); //On stocke les coordonnées des salles déjà utilisées
            //Pour chaque salle de type i, on crée les salles aux coordonnées choisies et on les ajoute à la liste des salles utilisées
            for (int j = 0; j < chosenLocationIndexes.size(); j++) {
                DiscreteCoordinates pos = usableLocations.get(chosenLocationIndexes.get(j));
                usedLocation.add(pos);
                createRoom(i, pos);
                roomsPlacement[pos.x][pos.y] = MapState.CREATED;
            }
            //On enlève les salles utilisées de la liste des salles libres
            for (int j = 0; j<usedLocation.size(); j++){
                usableLocations.remove(usedLocation.get(j));
            }
        }
    }
    protected abstract void createRoom(int roomType, DiscreteCoordinates roomCoordinates);
    protected abstract ICRogueRoom createBoosRoom(DiscreteCoordinates roomCoordinates);
    protected abstract void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room);

    private void printMap(MapState[][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j + " "); }
        System.out.println(); System.out.print("--|-");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print("--"); }
        System.out.println();
        for (int i = 0; i < map.length; i++) { System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " "); }
            System.out.println(); }
        System.out.println(); }
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

    public boolean isBoosRoom(DiscreteCoordinates coordinates){
        return coordinates.equals(bossRoom);
    }

    protected enum MapState {
        NULL, // Empty space
        PLACED, // The room has been placed but not yet explored by the room placement algorithm
        EXPLORED, // The room has been placed and explored by the algorithm
        BOSS_ROOM, // The room is a boss room
        CREATED; // The room has been instantiated in the room map
        @Override
        public String toString() {
            return Integer.toString(ordinal());
        }
    }

}