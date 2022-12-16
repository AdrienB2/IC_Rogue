package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level1EnemyRoom extends Level1Room {
    private List<Enemy> enemies = new ArrayList<>();
    private boolean isHeartGiven = false;
    public Level1EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
    }
 protected void setEnemies(List<Enemy> enemies){
        for (Enemy enemy: enemies){
            this.enemies.add(enemy);
        }
 }
    @Override
    protected void createArea() {
        super.createArea();
        for (Enemy enemy:
             enemies) {
            registerActor(enemy);
        }
    }

    @Override
    public void update(float deltaTime) {
        ArrayList<Enemy> killedEnemies = new ArrayList<>();
        for (Enemy enemi:
             enemies) {
            if (enemi.isDead()) {
                killedEnemies.add(enemi);
            }
        }
        for (Enemy killedEnemy:
             killedEnemies) {
            enemies.remove(killedEnemy);
        }
        if(enemies.size() == 0 && !isHeartGiven){
            Heart heart = new Heart(this, Orientation.UP, new DiscreteCoordinates(5,5));
            isHeartGiven= true;
            registerActor(heart);
        }
        super.update(deltaTime);
    }

    @Override
    public boolean isOn() {
        return enemies.size() == 0 && isVisited;
    }
}
