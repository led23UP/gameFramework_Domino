package com.example.gameframework.Domino.infoMessage;

import androidx.annotation.NonNull;

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
    private boolean playerOn;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;

    public PlayerInfo(int id)
    {
        this.id = id;
        playerHand = new ArrayList<>();
        this.score= 0;
        legalMoves = new ArrayList<>();
    }
    public PlayerInfo(PlayerInfo other)
    {
        this.id = other.id;
        this.playerHand= new ArrayList<>(other.playerHand.size());
        for(int i=0; i< other.playerHand.size();i++)
        {
            this.playerHand.add(new Domino(other.playerHand.get(i)));
        }
        this.playerHand = other.playerHand;
        this.score= other.score;

        this.legalMoves = new ArrayList<>(other.legalMoves.size());

        this.legalMoves.addAll(other.legalMoves);
    }

    public void addPoints(int points){
        this.score += points;
    }

    public int getScore(){
        return this.score;
    }

    public ArrayList<Domino> getHand()
    {
        return playerHand;
    }

    public ArrayList<MoveInfo> getLegalMoves(){
        return this.legalMoves;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    @NonNull
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("PlayerInfo ").append(id + 1).append(" Score: ").append(score).append("\nHand: ");
        for (Domino d:playerHand) {
            s.append(d.toString()).append(", ");
        }
        return s.toString();
    }
}
