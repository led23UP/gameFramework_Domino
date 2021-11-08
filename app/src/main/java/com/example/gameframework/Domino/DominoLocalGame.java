package com.example.gameframework.Domino;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewRoundAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoQuitGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.LocalGame;
import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

import java.util.ArrayList;

public class DominoLocalGame extends LocalGame {
    public DominoLocalGame() {
        super();
        super.state = new DominoGameState();
    }

    public void setNumPlayers(int playerC){
        DominoGameState a = (DominoGameState) super.state;
        a.setNumPlayersStart(playerC);
    }
    public DominoLocalGame(DominoGameState dState){
        super.state = new DominoGameState(dState);
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

        p.sendInfo(new DominoGameState(((DominoGameState)state)));
    }
    @Override
    protected boolean canMove(int playerIdx) {
        return ((DominoGameState) state).getPlayerInfo()[playerIdx].getPlayerActive()
                && playerIdx == ((DominoGameState) state).getTurnID();
    }

    @Override

    protected String checkIfGameOver() {

        DominoGameState dState = (DominoGameState) super.state;

        //playersName.length is to prevent crashes out of bounds
        if (dState.getPlayerInfo()[0].getPlayerActive()== true &&
                dState.getPlayerInfo()[0].getScore() >=150){
            return playerNames[0]+ " wins with " + dState.getPlayerInfo()[0].getScore() + " points!";
        }
        if (playerNames.length >=2 && dState.getPlayerInfo()[1].getPlayerActive()== true &&
                dState.getPlayerInfo()[1].getScore() >=150){
            return playerNames[1]+" wins with " + dState.getPlayerInfo()[1].getScore() + " points!";
        }
        if (playerNames.length >=3 && dState.getPlayerInfo()[2].getPlayerActive()== true &&
                dState.getPlayerInfo()[2].getScore() >=150){
            return playerNames[2]+" wins with " + dState.getPlayerInfo()[2].getScore() + " points!";
        }
        if (playerNames.length >=4 && dState.getPlayerInfo()[0].getPlayerActive()== true &&dState.getPlayerInfo()[3].getScore() >=150){
            return playerNames[3]+" wins with " + dState.getPlayerInfo()[3].getScore() + " points!";
        }



        return null;
    }


    //TODO Finish this method and fix it.
    @Override
    protected boolean makeMove(GameAction action) {
        DominoGameState state = (DominoGameState) super.state;
        //if game is blocked, no player can make a turn, move on
        if (state.isGameBlocked()){
            state.endRound();
            state.startRound(false);
        }


        int playerID=state.getTurnID();
        //check if current player can move
        if (canMove(playerID)){

            //check if game is blocked
            if (state.isGameBlocked()){
                state.endRound();
                state.startRound(false);
                sendAction(new DominoNewRoundAction(players[playerID]));
            }

            if (state.getPlayerInfo()[playerID].getHand().size() == 0) {
                state.placedAllPieces(playerID);
                state.startRound(false);
                sendAction(new DominoNewRoundAction(players[playerID]));
            }
            if( action instanceof DominoMoveAction)
            {
                DominoMoveAction dm = (DominoMoveAction) action;
                //gets row, col, and idx from either computer player or human player
                int row = dm.getRow();
                int col = dm.getCol();
                int idx = dm.getDominoIndex();
                //gets the players move
                for (MoveInfo m : state.getPlayerInfo()[playerID].getLegalMoves()) {
                    if (m.getRow() == row && m.getCol() == col && m.getDominoIndex() == idx) {
                        state.placePiece(row, col, playerID, idx);

                        //update player's score
                        state.setMessage(playerNames[playerID] + " scored " +
                                state.getPlayerInfo()[playerID].getScore() +" points");

                        state.setTurnID(); //next player's turn

                        //clear legal moves and finds it again
                        state.getPlayerInfo()[playerID].getLegalMoves().clear();
                        state.findLegalMoves(playerID);
                        //update current boneyard dominos remaining
                        state.setBoneyardMsg(Integer.toString(state.getBoneyard().size()));
                        return true;
                    }
                }
                return false;
            }
            if (action instanceof DominoDrawAction)
            {
                //this section does not change the player turn because we want this action to "loop"

                //if there's no legal moves, draw a piece until there's a legal more
                while(state.getPlayerInfo()[playerID].getLegalMoves().size() == 0)
                {
                    //if boneyard is empty, return back to computer player class which will
                    //send a Domino Skip action
                    if(state.getBoneyard().size() == 0)
                    {
                        return true;
                    }
                    state.drawPiece(playerID);

                    //update log
                    state.setBoneyardMsg(Integer.toString(state.getBoneyard().size()));
                    state.setMessage(playerNames[playerID]+" draws a domino");

                }
                return true;
            }
            if( action instanceof DominoSkipAction)
            {
                state.setMessage(playerNames[playerID]+" cannot make a legal move. Their turn has been skipped");
                state.setTurnID();
                return true;
            }
            if (action instanceof DominoQuitGameAction)
            {
                state.setMessage("Player x has forfeited their turn");
                return true;
            }
            if (action instanceof DominoNewGameAction)
            {

            }
        }

        return false;
    }


}


