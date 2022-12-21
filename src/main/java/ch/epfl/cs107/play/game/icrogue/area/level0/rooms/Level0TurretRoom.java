package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0TurretRoom extends Level0EnemyRoom{
    //list of all the possible positions of the turrets
    private static final DiscreteCoordinates[][] TURRET_COORDINATES_PATTERNS = new DiscreteCoordinates[][]{
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(5,5)
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,1),
                    new DiscreteCoordinates(8,8)
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,8),
                    new DiscreteCoordinates(8,1)
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,1),
                    new DiscreteCoordinates(8,1),
                    new DiscreteCoordinates(1,8),
                    new DiscreteCoordinates(8,8)
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,1),
                    new DiscreteCoordinates(8,1),
                    new DiscreteCoordinates(1,8),
                    new DiscreteCoordinates(8,8),
                    new DiscreteCoordinates(5,5)
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,1),
                    new DiscreteCoordinates(8,8),
                    new DiscreteCoordinates(5,5),
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,8),
                    new DiscreteCoordinates(8,1),
                    new DiscreteCoordinates(5,5),
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,1),
                    new DiscreteCoordinates(2,2),
                    new DiscreteCoordinates(3,3),
                    new DiscreteCoordinates(4,4),
                    new DiscreteCoordinates(5,5),
                    new DiscreteCoordinates(6,6),
                    new DiscreteCoordinates(7,7),
                    new DiscreteCoordinates(8,8),
            },
            new DiscreteCoordinates[] {
                    new DiscreteCoordinates(1,8),
                    new DiscreteCoordinates(2,7),
                    new DiscreteCoordinates(3,6),
                    new DiscreteCoordinates(4,5),
                    new DiscreteCoordinates(5,4),
                    new DiscreteCoordinates(6,3),
                    new DiscreteCoordinates(7,2),
                    new DiscreteCoordinates(8,1),
            }
    };
    //POur chaque pattern, on a une liste de content les liste de direction de tir de chaque turret
    private static final Orientation[][][] TURRET_DIRECTIONS = new Orientation[][][]{
            new Orientation[][]{
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.UP, Orientation.RIGHT},
                    new Orientation[]{Orientation.DOWN, Orientation.LEFT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.DOWN, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.LEFT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.RIGHT},
                    new Orientation[]{Orientation.UP},
                    new Orientation[]{Orientation.DOWN},
                    new Orientation[]{Orientation.LEFT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.RIGHT},
                    new Orientation[]{Orientation.UP},
                    new Orientation[]{Orientation.DOWN},
                    new Orientation[]{Orientation.LEFT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.UP, Orientation.RIGHT},
                    new Orientation[]{Orientation.DOWN, Orientation.LEFT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.DOWN, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.LEFT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.UP, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.DOWN, Orientation.LEFT}
            },
            new Orientation[][]{
                    new Orientation[]{Orientation.DOWN, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT},
                    new Orientation[]{Orientation.UP, Orientation.LEFT}
            }
    };

    public Level0TurretRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
       setEnemies(generateEnemiesList());
    }

    public List<Enemy> generateEnemiesList(){
        //choisit un des patterns de la liste
        int pattern = RandomHelper.roomGenerator.nextInt(0, TURRET_COORDINATES_PATTERNS.length);
        //crée une liste d'ennemis et y ajoute les turrets
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < TURRET_COORDINATES_PATTERNS[pattern].length; i++) {
            enemies.add(new Turret(this, Orientation.UP, TURRET_COORDINATES_PATTERNS[pattern][i], TURRET_DIRECTIONS[pattern][i]));
        }
        return enemies;
    }
}
