package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueHPBar;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Bombe;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Boss extends Enemy{

    //TELEPORTATION VARIABLES
    private static final float TELEPORT_COOLDOWN = 1.5f; // temps en seconde avant que l'ennemi puisse téléporter à nouveau
    private float teleportCooldown; // temps restant avant que l'ennemi puisse téléporter à nouveau

    //HP BAR
    private final ICRogueHPBar hpBar;

    private Sprite sprite;

    /**
     * Default MovableAreaEntity constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Boss(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        teleportCooldown = TELEPORT_COOLDOWN;
        sprite = new Sprite("zelda/darkLord.spell",1,1.5f,this, new RegionOfInterest(32,64,32,32), Vector.ZERO);
        setSprite(sprite);
        setSpritesDepth(3000);
        hp = 30;
        hpBar = new ICRogueHPBar(hp);
    }

    private void teleportBoss() {
        //select a random direction
        int x = (int) (Math.random() * 8) + 1;
        int y = (int) (Math.random() * 8) + 1;
        DiscreteCoordinates newPos = new DiscreteCoordinates(x, y);
        //drop a bomb at the old position
        getOwnerArea().registerActor(new Bombe(getOwnerArea(), getCurrentMainCellCoordinates()));
        //teleport to the new position
        changePosition(newPos);
    }


    //INTERACTION
    @Override
    public boolean isCellInteractable() {
        return true;
    }
    @Override
    public boolean isViewInteractable() {
        return true;
    }
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
    @Override
    public boolean takeCellSpace() {
        return true;
    }


    //UPDATE AND DRAW
    @Override
    public void draw(Canvas canvas) {
        hpBar.draw(canvas);
        super.draw(canvas);
    }
    @Override
    public void update(float deltaTime) {
        teleportCooldown -= deltaTime;
        hpBar.update(deltaTime);
        if (hp <= 0){
            kill();
            getOwnerArea().unregisterActor(this);
        }
        hpBar.updateHP(hp);
        if(teleportCooldown <= 0) {
            teleportBoss();
            teleportCooldown = TELEPORT_COOLDOWN;
        }

        super.update(deltaTime);
    }
}
