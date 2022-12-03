package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private Area currentArea;
    private ICRoguePlayer player;
    private Level0 level;

    private void initLevel(){
        level = new Level0();
        for (ICRogueRoom room:
                level.getRooms()) {
            addArea(room);
        }

        currentArea = setCurrentArea(level.getFirstRoomTitle(), true);
        player = new ICRoguePlayer(currentArea, Orientation.UP, new DiscreteCoordinates(2,2), "zelda/player");
        player.enterArea(currentArea, new DiscreteCoordinates(2,2));
    }
    private  void reset(){
        setCurrentArea(currentArea.getTitle(), true);
        player = new ICRoguePlayer(currentArea, Orientation.DOWN, new DiscreteCoordinates(2,2), "zelda/player");
        player.enterArea(currentArea, new DiscreteCoordinates(2,2));
    }
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initLevel();
            return true;
        }
        return false;
    }

   /*
    private void initArea(String areaKey) {

        Tuto2Area area = (Tuto2Area)setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new GhostPlayer(area, Orientation.DOWN, coords,"ghost.1");
        player.enterArea(area, coords);
        player.centerCamera();

    }
    */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard();
        if(keyboard.get(Keyboard.R).isDown()){
            reset();
        }
        if(player.isChangingRoom()){
            switchRoom(player.getDestinationRoom(), player.getArrivalCoordinate());
        }
        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "ICRogue";
    }
    protected void switchRoom(String destinationRoom, DiscreteCoordinates playerArrivalCoordinates){
        player.leaveArea();
        currentArea = setCurrentArea(destinationRoom, false);
        player.enterArea(currentArea, playerArrivalCoordinates);
        System.out.println(playerArrivalCoordinates);
        player.changePosition(playerArrivalCoordinates);
    }
}
