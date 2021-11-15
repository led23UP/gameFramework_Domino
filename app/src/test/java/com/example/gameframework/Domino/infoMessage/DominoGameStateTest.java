package com.example.gameframework.Domino.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class DominoGameStateTest {

    @Test
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



}




