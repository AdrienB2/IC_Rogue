package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.PauseMenu;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level1.Level1;
import ch.epfl.cs107.play.game.icrogue.hud.HUD;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private Area currentArea;
    private ICRoguePlayer player;
    private Level level;
    private HUD hud;

    private void initLevel(){
        level = new Level1(this);
        currentArea = setCurrentArea(level.getFirstRoomTitle(), true);
        player = new ICRoguePlayer(currentArea, Orientation.UP, new DiscreteCoordinates(2,2), "zelda/player");
        player.enterArea(currentArea, new DiscreteCoordinates(2,2));
        hud = new HUD(this);
        currentArea.registerActor(hud);
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
        hud.updateHUD((int) player.getHp());

        if(player.isChangingRoom()){
            switchRoom(player.getDestinationRoom(), player.getArrivalCoordinate());
        }

        if(level.isOn()){
            System.out.println("WIN");
        }
        if(player.getHp()<=0){
            System.out.println("GameOver");
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
        currentArea.unregisterActor(hud);
        player.leaveArea();
        currentArea = setCurrentArea(destinationRoom, false);
        currentArea.registerActor(hud);
        player.enterArea(currentArea, playerArrivalCoordinates);
    }
}
