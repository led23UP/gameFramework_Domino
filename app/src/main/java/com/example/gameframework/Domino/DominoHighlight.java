package com.example.gameframework.Domino;

import com.example.gameframework.Domino.infoMessage.MoveInfo;

/**
 * DominoHighlight holds information of where a highlight is drawn and this information is used to
 * see if a player taps inside of a highlight.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoHighlight {
    // Fields leftX, rightX, topY, bottomY are used to see if player clicked inside highlight.
    private final float leftX;
    private final float rightX;
    private final float topY;
    private final float bottomY;
    // We store the MoveInfo so surface can return the move that the player wants to place.
    private final MoveInfo m;

    public DominoHighlight(float leftX, float topY, int o, MoveInfo m){
        this.leftX = leftX;
        this.topY = topY;
        if (o == 1 || o == 3){
            this.rightX = leftX + 82 + 75;
            this.bottomY = topY + 41 + 75;
        }
        else{
            this.rightX = leftX + 41 + 75;
            this.bottomY = topY + 82 + 75;
        }
        this.m = m;
    }

    public boolean isInHighlight(float x, float y){
        return x > leftX && x < rightX && y > topY && y < bottomY;
    }

    public MoveInfo getMoveInfo(){
        return m;
    }
}
