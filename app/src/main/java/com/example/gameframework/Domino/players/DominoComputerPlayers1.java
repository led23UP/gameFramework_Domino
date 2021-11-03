package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.infoMessage.PlayerInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;
import java.util.Random;

public class DominoComputerPlayers1 extends GameComputerPlayer {
    private int score;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     **/

    //dumb ai
    public DominoComputerPlayers1(String name) {
        super(name);
        score = 0;
        playerHand = new ArrayList<>();
        legalMoves = new ArrayList<>();
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        DominoGameState gameState = new DominoGameState((DominoGameState) info);
        ArrayList<MoveInfo> legalMoves = gameState.getPlayerInfo()[playerNum].getLegalMoves();
        int size = legalMoves.size();

        Random rand = new Random();
        int randomInt = rand.nextInt( size - 1);

        MoveInfo move = legalMoves.get(randomInt);
        //game.sendAction();

    }
}
