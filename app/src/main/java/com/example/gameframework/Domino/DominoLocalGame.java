package com.example.gameframework.Domino;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewGameAction;
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
        if (state.isGameBlocked()){
            state.endRound();
            state.startRound();
        }

        int playerID;
        playerID=state.getTurnID();

        if (canMove(playerID)){
            //skips the forfeited player's turn

            if( action instanceof DominoMoveAction)
            {
                DominoMoveAction dm = (DominoMoveAction) action;
                int row = dm.getRow();
                int col = dm.getCol();
                int idx = dm.getDominoIndex();

                for (MoveInfo m : state.getPlayerInfo()[playerID].getLegalMoves()) {
                    if (m.getRow() == row && m.getCol() == col && m.getDominoIndex() == idx) {
                        state.placePiece(row, col, playerID, idx);

                        state.setMessage(playerNames[playerID] + " scored " +
                                state.getPlayerInfo()[playerID].getScore() +" points");
                        state.setTurnID();

                        state.getPlayerInfo()[playerID].getLegalMoves().clear();
                        state.findLegalMoves(playerID);
                        state.setBoneyardMsg(Integer.toString(state.getBoneyard().size()));
                        return true;
                    }
                }
                return false;
            }
            if (action instanceof DominoDrawAction)
            {
                while(state.getPlayerInfo()[playerID].getLegalMoves().size() == 0)
                {
                    if(state.getBoneyard().size() == 0)
                    {
                        return true;
                    }
                    state.setBoneyardMsg(Integer.toString(state.getBoneyard().size()));
                    state.drawPiece(playerID);
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

        //ss
        return false;
    }


}


