package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class ICRogueAreaGraph extends AreaGraph {
    public ICRogueAreaGraph(int width, int height){
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                System.out.println(i + " " + j);
                boolean hasLeftEdge = true;
                boolean hasRightEdge = true;
                boolean hasUpEdge = true;
                boolean hasDownEdge = true;
                //Vérification de l'existence des bordures autour de la cellule
                if(i==1){
                    hasLeftEdge = false;
                }
                else if (i == width-2){
                    hasRightEdge = false;
                }
                if(j == 1){
                    hasDownEdge = false;
                }
                if(j == height-2){
                    hasUpEdge = false;
                }

                this.addNode(new DiscreteCoordinates(i,j), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
    }
}
