package com.example.gameframework.Domino.infoMessage;

public class MoveInfo {
    private int row;
    private int col;
    private int orientation;
    private char chain;

    public MoveInfo(int r, int c, int o){
        this.row = r;
        this.col = c;
        this.orientation = o;
        this.chain = ' ';
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
}
