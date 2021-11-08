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
        legalMoves = new ArrayList<>();
    }
    public PlayerInfo(PlayerInfo other)
    {

        this.playerActive = other.playerActive;

        this.id = other.id;
        this.playerHand= new ArrayList<>(other.playerHand.size());
        for(int i=0; i< other.playerHand.size();i++)
        {
            this.playerHand.add(new Domino(other.playerHand.get(i)));
        }
        this.playerHand = other.playerHand;
        this.score= other.score;

        this.legalMoves = new ArrayList<>(other.legalMoves.size());

        for (int i = 0; i < other.legalMoves.size(); i++){
            this.legalMoves.add(new MoveInfo(other.legalMoves.get(i)));
        }
    }

    public boolean getPlayerActive() {return this.playerActive; }

    public void setPlayerActive(boolean value){ this.playerActive = value;}

    public void addPoints(int points){
        this.score += points;
    }

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
