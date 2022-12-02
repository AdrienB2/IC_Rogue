package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private Level0Room currentArea;
    private ICRoguePlayer player;
    private final String[] areas = {"zelda/Ferme", "zelda/Village"};

    private int areaIndex;

    private void initLevel(){
        currentArea = new Level0Room(new DiscreteCoordinates(0,0));
        addArea(currentArea);
        setCurrentArea(currentArea.getTitle(), true);
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
            areaIndex = 0;
            //initArea(areas[areaIndex]);
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
        /*if(player.isWeak()){
            switchArea();
        }*/
        Keyboard keyboard = getWindow().getKeyboard();
        if(keyboard.get(Keyboard.R).isDown()){
            reset();
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
/*
    protected void switchArea() {

        player.leaveArea();

        areaIndex = (areaIndex==0) ? 1 : 0;

        Tuto2Area currentArea = (Tuto2Area)setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());

        player.strengthen();
    }
*/
}
