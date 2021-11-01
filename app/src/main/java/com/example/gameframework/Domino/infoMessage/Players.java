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
public class Players {
    private final int id;
    private int score;
    private ArrayList<Domino> playerHand;

    public Players(int id, int score)
    {
        this.id = id;
        playerHand = new ArrayList<>();
        this.score=score;
    }
    public Players(Players other)
    {
        this.id = other.id;
        this.playerHand= new ArrayList<Domino>(other.playerHand.size());
        for(int i=0; i< other.playerHand.size();i++)
        {
            this.playerHand.add(new Domino(other.playerHand.get(i)));
        }
        this.playerHand = other.playerHand;
        this.score= other.score;
    }

    public int getId()
    {
        return id;
    }

    public void addPoints(int points){
        this.score += points;
    }

    public ArrayList<Domino> getHand()
    {
        return playerHand;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    @NonNull
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Player ").append(id + 1).append(" Score: ").append(score).append("\nHand: ");
        for (Domino d:playerHand) {
            s.append(d.toString()).append(", ");
        }
        return s.toString();
    }
}
