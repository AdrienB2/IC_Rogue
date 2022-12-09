package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Level0EnemyRoom extends Level0Room{
    private List<Enemy> enemies = new ArrayList<>();
    public Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
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
            if (enemi.getIsDead()) {
                killedEnemies.add(enemi);
            }
        }
        for (Enemy killedEnemy:
             killedEnemies) {
            enemies.remove(killedEnemy);
        }
        super.update(deltaTime);
    }

    @Override
    public boolean isOn() {
        return enemies.size() == 0 && isVisited;
    }
}
