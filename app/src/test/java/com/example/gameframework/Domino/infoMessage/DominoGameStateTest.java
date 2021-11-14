package com.example.gameframework.Domino.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class DominoGameStateTest {

    @Test
    public void testFirstMove() {

        DominoGameState game_state= new DominoGameState();
        //setting number of players to 4
game_state.setNumPlayersStart(4);

        game_state.startRound(true);
        //to randomize the cards dealt to each player

        int playerWithMax=-1;//index of player array with maximum weighted domino
        int dominoIndexMax=-1;// index of player hand with maximum weighted domino
        int maxDominoWeight=-1; //maximum weight of domino


        PlayerInfo [] players=game_state.getPlayerInfo();
        int numPlayers=players.length;
//outer for-loop walks through playerinfo objects in players array
        for(int i=0;i<numPlayers;i++){
            ArrayList<Domino> playerHand= players[i].getHand();
            //inner for-loop walks through the dominos in the hands of each player
            for(int j=0;j<playerHand.size();j++){
                //if the  maximum weight is smaller than the current domino's wieght,
                if(playerHand.get(j).getWeight()>maxDominoWeight){
                    playerWithMax=i;//set with current player
                    dominoIndexMax=j;//set with current playerHand index
                    maxDominoWeight=playerHand.get(j).getWeight();//set with new maximum weight
                }



            }



        }
// expected first move data calculated from the for loop above
        int [] expectedFirstMove= new int [2];
        expectedFirstMove[0]=playerWithMax;
        expectedFirstMove[1]=dominoIndexMax;
//unexpected data
        int [] notExpectedFirstMove= new int [2];
        notExpectedFirstMove[0]=playerWithMax-1;
        notExpectedFirstMove[1]=dominoIndexMax-1;

        assertArrayEquals(expectedFirstMove, game_state.firstMove());
        assertNotEquals(notExpectedFirstMove[0],game_state.firstMove()[0]);
        assertNotEquals(notExpectedFirstMove[1],game_state.firstMove()[1]);








    }
}




