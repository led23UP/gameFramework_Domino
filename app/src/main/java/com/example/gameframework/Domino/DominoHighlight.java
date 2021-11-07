package com.example.gameframework.Domino;

import com.example.gameframework.Domino.infoMessage.MoveInfo;

public class DominoHighlight {
    // Fields leftX, rightX, topY, bottomY are used to see if player clicked inside highlight.
    private float leftX,rightX;
    private float topY, bottomY;
    private int orientation;
    // We store the MoveInfo so surface can return the move that the player wants to place.
    private MoveInfo m;

    public DominoHighlight(float leftX, float topY, int o, MoveInfo m){
        this.leftX = leftX;
        this.topY = topY;
        this.orientation = o;
        if (this.orientation == 1 || this.orientation == 3){
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
