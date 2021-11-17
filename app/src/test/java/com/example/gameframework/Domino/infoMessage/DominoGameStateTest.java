package com.example.gameframework.Domino.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class DominoGameStateTest {


    @Test
    // Written by Pranav Rajan.
    public void testFirstMove() {
        DominoGameState game_state = new DominoGameState();
        //setting number of players to 4
        game_state.setNumPlayersStart(4);
        game_state.startRound(true);
        //to randomize the cards dealt to each player
        int playerWithMax = -1;//index of player array with maximum weighted domino
        int dominoIndexMax = -1;// index of player hand with maximum weighted domino
        int maxDominoWeight = -1; //maximum weight of domino
        PlayerInfo[] players = game_state.getPlayerInfo();
        int numPlayers = players.length;
        //outer for-loop walks through playerinfo objects in players array
        for (int i = 0; i < numPlayers; i++) {
            ArrayList<Domino> playerHand = players[i].getHand();
            //inner for-loop walks through the dominos in the hands of each player
            for (int j = 0; j < playerHand.size(); j++) {
                //if the  maximum weight is smaller than the current domino's wieght,
                if (playerHand.get(j).getWeight() > maxDominoWeight) {
                    playerWithMax = i;//set with current player
                    dominoIndexMax = j;//set with current playerHand index
                    maxDominoWeight = playerHand.get(j).getWeight();//set with new maximum weight
                }
            }
        }
        // expected first move data calculated from the for loop above
        int[] expectedFirstMove = new int[2];
        expectedFirstMove[0] = playerWithMax;
        expectedFirstMove[1] = dominoIndexMax;
        int[] actualFirstMove = new int[2];
        actualFirstMove = game_state.firstMove();
        //expected first move compared to actual return value from firstMove() function
        assertArrayEquals(expectedFirstMove, actualFirstMove);
        //re-starting the round so that shuffled Dominos to each player are different
        //therefore calculating the first move again based on different player hands
        //and checking if they match the actual first move return value
        game_state.startRound(true);
        playerWithMax = -1;
        dominoIndexMax = -1;
        maxDominoWeight = -1;
        players = game_state.getPlayerInfo();
        numPlayers = players.length;
        for (int i = 0; i < numPlayers; i++) {
            ArrayList<Domino> playerHand = players[i].getHand();
            for (int j = 0; j < playerHand.size(); j++) {
                if (playerHand.get(j).getWeight() > maxDominoWeight) {
                    playerWithMax = i;
                    dominoIndexMax = j;
                    maxDominoWeight = playerHand.get(j).getWeight();
                }
            }
        }
        expectedFirstMove[0] = playerWithMax;
        expectedFirstMove[1] = dominoIndexMax;
        actualFirstMove = game_state.firstMove();
        assertArrayEquals(expectedFirstMove, actualFirstMove);

    }

    @Test
    // Written by Paul Kenstler
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
    // Written by Paul Kenstler
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
    // Written by Paul Kenstler
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
    @Test
    // Written by David Le.
    public void testGetTurnID() {
        DominoGameState state = new DominoGameState();
        state.setNumPlayersStart(2);
        state.setTurnID();//0
        state.setTurnID();//1
        state.setTurnID();//0
        state.setTurnID();//1
        state.setTurnID();//0
        assertEquals(0, state.getTurnID());

    }
    @Test
    // Written by David Le.
    public void testQuitGame() {
        DominoGameState state = new DominoGameState();
        state.setNumPlayersStart(1);
        PlayerInfo p1 = state.getPlayerInfo()[0];
        state.quitGame(p1.getID());
        assertEquals(false, p1.getPlayerActive());
    }

    private boolean dominoEquals(Domino d1, Domino d2){
        return d1.getLeftPipCount() == d2.getLeftPipCount()
                && d2.getRightPipCount() == d1.getRightPipCount();
    }

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