package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;

public class DominoComputerPlayers1 extends GameComputerPlayer {
    private int score;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoComputerPlayers1(String name) {
        super(name);
        score = 0;
        playerHand = new ArrayList<>();
        legalMoves = new ArrayList<>();
    }

    @Override
    protected void receiveInfo(GameInfo info) {

    }
}
