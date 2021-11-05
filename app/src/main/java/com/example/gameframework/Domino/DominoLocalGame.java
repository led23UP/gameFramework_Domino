package com.example.gameframework.Domino;

import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoQuitGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
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
    public DominoLocalGame(DominoGameState dState){
        super.state = new DominoGameState(dState);
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        p.sendInfo(new DominoGameState(((DominoGameState)state)));
    }
    @Override
    protected boolean canMove(int playerIdx) {
        return playerIdx == ((DominoGameState)state).getTurnID();
    }

    @Override
    protected String checkIfGameOver() {
        DominoGameState dState = (DominoGameState) super.state;
        if (dState.getPlayerInfo()[0].getScore() >=150){
            return "Player 0 wins with " + dState.getPlayerInfo()[0].getScore() + " points!";
        }
        /*else if (dState.getPlayerInfo()[1].getScore() >=150){
            return "Player 1 wins with " + dState.getPlayerInfo()[1].getScore() + " points!";
        }
        else if (dState.getPlayerInfo()[2].getScore() >=150){
            return "Player 2 wins with " + dState.getPlayerInfo()[2].getScore() + " points!";
        }
        else if (dState.getPlayerInfo()[3].getScore() >=150){
            return "Player 3 wins with " + dState.getPlayerInfo()[3].getScore() + " points!";
        }*/
        else{
            return null;
        }
    }
    //TODO Finish this method and fix it.
    @Override
    protected boolean makeMove(GameAction action) {
        DominoMoveAction dm = (DominoMoveAction) action;
        DominoGameState state = (DominoGameState) super.state;

        int row = dm.getRow();
        int col = dm.getCol();
        int idx = dm.getDominoIndex();

        int playerID = getPlayerIdx(dm.getPlayer());
        if (canMove(playerID)){
            //skips the forfeited player's turn
            if(playerID == -1)
            {
                state.setTurnID();
                return false;
            }
            if( action instanceof DominoMoveAction)
            {
                row = dm.getRow();
                col = dm.getCol();
                idx = dm.getDominoIndex();
                state.placePiece(row,col,playerID,idx);
                state.getPlayerInfo()[playerID].getHand().remove(idx); // remove that domino from hand
                state.setTurnID();
                return true;
            }
            if( action instanceof DominoSkipAction)
            {
                state.setMessage("Player x cannot make a legal move. Their turn has been skipped");
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


