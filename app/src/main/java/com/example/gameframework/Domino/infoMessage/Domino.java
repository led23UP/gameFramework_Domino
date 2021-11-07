package com.example.gameframework.Domino.infoMessage;
/**
 * Domino class that creates dominoes for use in the game.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class Domino {

    private int leftPipsCount;
    private int rightPipsCount;
    private int orientation;
    private final int weight;
    private boolean spinner;
    private char chain;

    public Domino(int pipsLeft, int pipsRight, int paramOrientation, int weightParam){
        leftPipsCount = pipsLeft;
        rightPipsCount = pipsRight;
        orientation = paramOrientation;
        weight = weightParam;
        spinner = false;
        chain = ' ';
    }

    public Domino(Domino other){
        this.leftPipsCount = other.leftPipsCount;
        this.rightPipsCount = other.rightPipsCount;
        this.orientation = other.orientation;
        this.weight = other.weight;
        this.spinner = other.spinner;
        this.chain = other.chain;
    }

    public int getLeftPipCount(){
        return this.leftPipsCount;
    }

    public int getRightPipCount(){
        return this.rightPipsCount;
    }

    public int getWeight(){
        return this.weight;
    }

    public int getOrientation(){
        return this.orientation;
    }

    public char getChain(){
        return this.chain;
    }

    public boolean isSpinner(){
        return this.spinner;
    }

    public void setSpinner(){
        this.spinner = true;
    }

    public void setOrientation(int o){
        this.orientation = o;
        // Orientation 3 is 180 degree rotation, swap pips.
        if (o == 3 || o == 4){
            int tmp = this.leftPipsCount;
            this.leftPipsCount = rightPipsCount;
            rightPipsCount = tmp;

        }
    }

    public void setChain(char c){
        this.chain = c;
    }

    @Override
    public String toString(){
        String s = "";
        s += "[" + leftPipsCount + "|" + rightPipsCount + "]";

        s += " Orientation: " + orientation;
        s += " Weight:" + weight;
        return s;
    }
    // We're using a seperate method here to not print Orientation or Weight. When the domino is
    // placed, these values no longer matter.
    public String pipsToString(){
        if (orientation == 3 || orientation == 1 || orientation == -1 ) {
            return "[" + leftPipsCount + "|" + rightPipsCount + "]";
        }
        return "[T:" + leftPipsCount + "| B:" + rightPipsCount + "]";
    }

}
