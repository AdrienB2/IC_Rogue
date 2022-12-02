package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;

public interface ICRogueInteractionHandler extends AreaInteractionVisitor {
    default void interactWith(ICRogueBehavior.ICRogueCell other, boolean isCellInteraction) {}
    default void interactWith(ICRoguePlayer other, boolean isCellInteraction) {}
    default void interactWith(Cherry other, boolean isCellInteraction) {}
    default void interactWith(Staff other, boolean isCellInteraction) {}
    default void interactWith(Fire other, boolean isCellInteraction) {}

}
