package com.example.gameframework.Domino.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.GameMainActivity;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameHumanPlayer;

import java.util.ArrayList;

public class DominoHumanPlayers1 extends GameHumanPlayer implements View.OnClickListener,
    View.OnTouchListener{
    private int id;
    private int score;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoHumanPlayers1(String name, int id) {
        super(name);
        this.id = id;
        this.score = 0;
        this.playerHand = new ArrayList<>();
        this.legalMoves = new ArrayList<>();
    }

    public void addPoints(int points){
        this.score += points;
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

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
