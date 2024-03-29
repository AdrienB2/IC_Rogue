package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * ICRogueAreaGraph is a class that represents the graph of the ICRogue game
 */
public class ICRogueAreaGraph extends AreaGraph {
    /**
     * @param width (int) Largeur du graphe
     * @param height (int) Hauteur du graphe
     */
    public ICRogueAreaGraph(int width, int height){
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                boolean hasLeftEdge = true;
                boolean hasRightEdge = true;
                boolean hasUpEdge = true;
                boolean hasDownEdge = true;
                //Vérification de l'existence des bordures autour de la cellule
                if(i==1){
                    hasLeftEdge = false;
                }
                else if (i == width-1){
                    hasRightEdge = false;
                }
                if(j == 1){
                    hasDownEdge = false;
                }
                if(j == height-1){
                    hasUpEdge = false;
                }

                this.addNode(new DiscreteCoordinates(i,j), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
    }
}
