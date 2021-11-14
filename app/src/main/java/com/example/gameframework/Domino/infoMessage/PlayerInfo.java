package com.example.gameframework.Domino.infoMessage;

import java.util.ArrayList;
/**
 * Players class that creates a player object to keep track of score and hand.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class PlayerInfo {
    private final int id;
    private int currentPoints;
    private int score;
    private boolean playerActive;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;

    public PlayerInfo(int id, boolean active)
    {
        this.id = id;
        this.playerActive = active;
        playerHand = new ArrayList<>();
        this.score= 0;
        this.currentPoints=0;
        legalMoves = new ArrayList<>();
    }
    public PlayerInfo(PlayerInfo other)
    {
        this.playerActive = other.playerActive;

        this.id = other.id;
        this.playerHand= new ArrayList<>(other.playerHand.size());
        for(Domino d : other.playerHand) {
            this.playerHand.add(new Domino(d));
        }

        this.score= other.score;
        this.currentPoints = other.currentPoints;
        this.legalMoves = new ArrayList<>(other.legalMoves.size());

        for (MoveInfo m : other.legalMoves){
            this.legalMoves.add(new MoveInfo(m));
        }
    }

    public boolean getPlayerActive() {return this.playerActive; }

    public void setPlayerActive(boolean value){ this.playerActive = value;}

    public void addPoints(int points){
        this.score += points;
    }

    public void setCurrentPoints(int points){this.currentPoints = points;}
    public int getCurrentPoints(){return currentPoints;}
    public int getScore(){
        return this.score;
    }

    public ArrayList<Domino> getHand()
    {
        return this.playerHand;
    }

    public ArrayList<MoveInfo> getLegalMoves(){
        return this.legalMoves;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

}
