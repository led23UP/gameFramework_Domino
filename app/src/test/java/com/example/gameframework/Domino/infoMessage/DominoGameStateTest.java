package com.example.gameframework.Domino.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class DominoGameStateTest {

    @Test
    public void setNumPlayersStart() {
    }

    @Test
    public void firstMove() {
    }

    @Test
    public void placeFirstPiece() {
    }

    @Test
    public void findLegalMoves() {
    }

    @Test
    public void placePiece() {
    }

    @Test
    public void calculateScoredPoints() {
    }

    @Test
    public void drawPiece() {
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(1);
        ArrayList<Domino> oldBoneyard = dgs.getBoneyard();
        int oldBoneyardSize = oldBoneyard.size();
        Domino temp = oldBoneyard.get(0);
        PlayerInfo oldPlayer = dgs.getPlayerInfo()[0];
        int oldHandSize = oldPlayer.getHand().size();

        dgs.drawPiece(0);

        int newHandSize = dgs.getPlayerInfo()[0].getHand().size();

        assertTrue(dgs.getBoneyard().size() < oldBoneyardSize);
        assertTrue(newHandSize > oldHandSize);
        assertSame(temp, dgs.getPlayerInfo()[0].getHand().get(newHandSize - 1));
    }

    @Test
    public void quitGame() {
        int playerID = 0;
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(1);
        dgs.quitGame(playerID);
        PlayerInfo [] playerArray = dgs.getPlayerInfo();
        assertEquals(playerArray[0].getScore(), -1);
    }

    @Test
    public void newGame() {
        int playerID = 0;
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(1);
        dgs.newGame(playerID);
        PlayerInfo [] playerArray = dgs.getPlayerInfo();
        assertEquals(playerArray[0].getScore(), -2);
    }

    @Test
    public void getBOARDHEIGHT() {
    }

    @Test
    public void getBOARDWIDTH() {
    }

    @Test
    public void getDomino() {
    }

    @Test
    public void getPlayerInfo() {
    }

    @Test
    public void getTurnID() {
    }

    @Test
    public void getBoneyard() {
    }

    @Test
    public void getMessage() {
    }

    @Test
    public void setMessage() {
    }

    @Test
    public void getBoneyardMsg() {
    }

    @Test
    public void setBoneyardMsg() {
    }

    @Test
    public void isGameBlocked() {
    }

    @Test
    public void setTurnID() {
    }

    @Test
    public void startRound() {
    }

    @Test
    public void endRound() {
    }

    @Test
    public void placedAllPieces() {
    }
}