package com.example.gameframework.Domino.infoMessage;

public class MoveInfo {
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

    public void setRow(int row){
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setDominoIndex(int d){
        this.dominoIndex = d;
    }
}
