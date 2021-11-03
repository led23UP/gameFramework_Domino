package com.example.gameframework.Domino.infoMessage;

public class MoveInfo {
    private int row;
    private int col;
    private int orientation;
    private char chain;
    private int dominoIndex;

    public MoveInfo(int r, int c, int o, int d){
        this.row = r;
        this.col = c;
        this.orientation = o;
        this.chain = ' ';
        this.dominoIndex = d;
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

    public char getChain() {
        return chain;
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

    public void setChain(char chain) {
        this.chain = chain;
    }

    public void setDominoIndex(int d){
        this.dominoIndex = d;
    }
}
