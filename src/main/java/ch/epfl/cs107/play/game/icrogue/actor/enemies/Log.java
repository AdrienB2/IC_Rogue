package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueAreaGraph;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;
import java.util.Queue;

public class Log extends Enemy {
    private AreaGraph graph; // représente le graphe de la salle
    private DiscreteCoordinates[] targetsPositions; // liste des positions à atteindre
    private Queue<Orientation> path; // représente le chemin à suivre
    private int currentDestination = 0; // représente l'index de la destination courante
    private final float COOLDOWN = 1f; // temps entre chaque tir
    private float counter; // compte le temps écoulé depuis le dernier tir

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Log(Area area, Orientation orientation, DiscreteCoordinates position, AreaGraph graph) {
        super(area, orientation, position);
        MOVE_DURATION = 6;
        setSprites(Sprite.extractSprites("zelda/logMonster", 4, 1.5f, 1.5f, this, 32,32, new Orientation[]{
                Orientation.RIGHT,
                Orientation.LEFT,
                Orientation.UP,
                Orientation.DOWN}));
        this.graph = graph;
        targetsPositions = new DiscreteCoordinates[] {
                new DiscreteCoordinates(1,1),
                new DiscreteCoordinates(1,8),
                new DiscreteCoordinates(8,8),
                new DiscreteCoordinates(8,1),
        };
        // on récupère le chemin à suivre pour aller de la position courante à la première destination
        path = graph.shortestPath(getCurrentMainCellCoordinates(), targetsPositions[0]);
        this.counter = 0;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }


    @Override
    public void update(float deltaTime) {
        this.counter+=deltaTime;
        //si le chemin est vide, on récupère le chemin pour aller à la destination suivante
        if(path.size() == 0){
            getNextTarget();
        }
        Orientation nextOrientation = path.poll(); //On récupère la prochaine orientation à prendre, on oriente le monstre et on le déplace
        orientate(nextOrientation);
        move(MOVE_DURATION);

        if(isDead()){ // On supprime le monstre de l'aire s'il est mort
            getOwnerArea().unregisterActor(this);
        }

        // On tire une flèche si le cooldown est écoulé
        if(this.counter >= COOLDOWN){
            getOwnerArea().registerActor(new Arrow(getOwnerArea(),
                    getArrowOrientation() , getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
            this.counter = 0;
        }
        super.update(deltaTime);
    }
    private void getNextTarget(){
        //On récupère la target actuelle et on vérifie que l'on y soit bien arrivé. Si c'est le cas, on passe à la suivante et on récupère le chemin
        DiscreteCoordinates currentTarget = targetsPositions[currentDestination];
        if(currentTarget.equals(getCurrentMainCellCoordinates())){
            if(currentDestination == targetsPositions.length-1){ // quand on arrive à la dernière destination, on revient à la première
                currentDestination = 0;
            }
            else {
                currentDestination++;
            }
        }
        path = graph.shortestPath(getCurrentMainCellCoordinates(), targetsPositions[currentDestination]);
    }

    //On récupère orientation de la flèche en fonction de l'orientation du monstre
    private Orientation getArrowOrientation(){
        switch (getOrientation()){
            case UP -> {return Orientation.RIGHT;}
            case DOWN -> {return Orientation.LEFT;}
            case LEFT -> {return Orientation.UP;}
            case RIGHT -> {return Orientation.DOWN;}
        }
        return null;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
}
