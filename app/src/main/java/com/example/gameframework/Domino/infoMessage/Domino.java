package com.example.gameframework.Domino.infoMessage;

import java.io.Serializable;

/**
 * Domino class that creates dominoes for use in the game.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class Domino implements Serializable {

    private static final long serialVersionUID = -5109179264333136954L;

    private int leftPipsCount;
    private int rightPipsCount;
    private int orientation;
    private final int weight;
    private boolean spinner;

    public Domino(int pipsLeft, int pipsRight, int paramOrientation, int weightParam){
        leftPipsCount = pipsLeft;
        rightPipsCount = pipsRight;
        orientation = paramOrientation;
        weight = weightParam;
        spinner = false;
    }

    public Domino(Domino other){
        this.leftPipsCount = other.leftPipsCount;
        this.rightPipsCount = other.rightPipsCount;
        this.orientation = other.orientation;
        this.weight = other.weight;
        this.spinner = other.spinner;
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
}
