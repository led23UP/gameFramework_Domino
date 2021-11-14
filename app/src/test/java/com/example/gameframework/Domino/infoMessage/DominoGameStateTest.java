package com.example.gameframework.Domino.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class DominoGameStateTest {

    @Test
    public void placePiece() {
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(1);

        MoveInfo m = dgs.getPlayerInfo()[0].getLegalMoves().get(0);

        Domino d = dgs.getPlayerInfo()[0].getHand().get(m.getDominoIndex());

        dgs.placePiece(4,4,0,m.getDominoIndex());
        assertTrue(dominoEquals(d,dgs.getDomino(4,4)));
        assertEquals(4, dgs.getPlayerInfo()[0].getHand().size());
    }

    @Test
    public void calculateScoredPoints() {
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(1);

        ArrayList<MoveInfo> playerMoves = dgs.getPlayerInfo()[0].getLegalMoves();
        ArrayList<Domino> playerHand = dgs.getPlayerInfo()[0].getHand();

        playerMoves.clear();
        playerHand.clear();
        // Player should get 12 points.
        Domino d1 = new Domino(6,6,27,1);
        playerHand.add(d1);

        playerMoves.add(new MoveInfo(4,4,1,0));
        dgs.placePiece(4,4,0,0);

        assertEquals(12,dgs.getPlayerInfo()[0].getScore());
        //Player should get another 12 points.
        playerHand.add(new Domino(3,6,1,-1));
        playerMoves.add(new MoveInfo(4,5,1,0));

        dgs.placePiece(4,5,0,0);

        assertEquals(24, dgs.getPlayerInfo()[0].getScore());
        // Should be awarded NO points here.
        playerHand.add(new Domino(6,1,1,-1));
        playerMoves.add(new MoveInfo(4,6,1,0));

        dgs.placePiece(4,6,0,0);

        assertEquals(24, dgs.getPlayerInfo()[0].getScore());

        playerHand.add(new Domino(3,6,1,-1));
        playerMoves.add(new MoveInfo(3,4,2,0));

        dgs.placePiece(3,4,0,0);

        playerHand.add(new Domino(1,3,1,-1));
        playerMoves.add(new MoveInfo(4,7,1,0));
        dgs.placePiece(4,7,0,0);

        assertEquals(36, dgs.getPlayerInfo()[0].getScore());
    }

    @Test
    public void drawPiece() {
    }

    @Test
    public void isGameBlocked() {
        DominoGameState dgs = new DominoGameState();
        dgs.setNumPlayersStart(2);
        // If all players have no legal moves and boneyard is empty, isGameBlocked
        // should return true.
        dgs.getPlayerInfo()[0].getLegalMoves().clear();
        dgs.getPlayerInfo()[1].getLegalMoves().clear();
        dgs.getBoneyard().clear();
        assertTrue(dgs.isGameBlocked());
        // If not all players have no legal moves and boneyard is empty, isGameBlocked
        // should return false.
        dgs.getPlayerInfo()[1].getLegalMoves().add(new MoveInfo(-1, -1, -1,-1));
        assertFalse(dgs.isGameBlocked());
        // If not all players have no legal moves and boneyard is NOT empty, isGameBlocked
        // should return false.
        dgs.getPlayerInfo()[1].getLegalMoves().clear();
        dgs.getBoneyard().add(new Domino(6,6,1,27));
        assertFalse(dgs.isGameBlocked());

    }

    private boolean dominoEquals(Domino d1, Domino d2){
        return d1.getLeftPipCount() == d2.getLeftPipCount()
                && d2.getRightPipCount() == d1.getRightPipCount();
    }

}