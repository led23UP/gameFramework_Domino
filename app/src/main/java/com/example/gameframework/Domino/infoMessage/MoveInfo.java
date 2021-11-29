package com.example.gameframework.Domino.infoMessage;

import java.io.Serializable;

public class MoveInfo implements Serializable {

    private static final long serialVersionUID = -5159179064333136954L;

    private int row;
    private int col;
    private int orientation;
    private int dominoIndex;

    public MoveInfo(int r, int c, int o, int d){
        this.row = Math.max(r, 0);
        this.col = Math.max(c,0);
        this.orientation = o;
        this.dominoIndex = d;
    }

    public MoveInfo(MoveInfo other){
        this.row = other.row;
        this.col = other.col;
        this.orientation = other.orientation;
        this.dominoIndex = other.dominoIndex;
    }

    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getOrientation(){
        return orientation;
    }

    public int getDominoIndex(){
        return this.dominoIndex;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
