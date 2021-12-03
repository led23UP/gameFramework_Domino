package com.example.gameframework.Domino;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoGameBlockedAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoQuitGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoPlacedAllPiecesAction;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.game.GameFramework.LocalGame;
import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GameHumanPlayer;
import com.example.gameframework.game.GameFramework.players.GamePlayer;
import com.example.gameframework.game.GameFramework.players.ProxyPlayer;
import com.example.gameframework.game.GameFramework.utilities.MessageBox;

import java.lang.reflect.Proxy;


/**
 * DominoLocalGame is the game that is currently being played.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoLocalGame extends LocalGame {
    public DominoLocalGame() {
        super();
        super.state = new DominoGameState();
    }

    @Override
    public void start(GamePlayer[] players) {
        super.start(players);
        DominoGameState a = (DominoGameState) super.state;
        a.setNumPlayersStart(players.length);
        for (int i = 0; i < players.length; i++){
           GamePlayer p =  players[i];
            if (p instanceof GameHumanPlayer){
                ((GameHumanPlayer) p).setPlayerNum(i);
            }
            if (p instanceof ProxyPlayer){
                ((ProxyPlayer) p).setPlayerNum(i);
            }
        }
    }

    public DominoLocalGame(DominoGameState dState){
        super();
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
        if (dState.getPlayerInfo()[0].getPlayerActive() &&
                dState.getPlayerInfo()[0].getScore() >=75){

            return playerNames[0]+ " wins with " + dState.getPlayerInfo()[0].getScore() + " points!";
        }

        if (playerNames.length >=2 && dState.getPlayerInfo()[1].getPlayerActive() &&
                dState.getPlayerInfo()[1].getScore() >=75){
            return playerNames[1]+" wins with " + dState.getPlayerInfo()[1].getScore() + " points!";
        }
        if (playerNames.length >=3 && dState.getPlayerInfo()[2].getPlayerActive() &&
                dState.getPlayerInfo()[2].getScore() >=75){
            return playerNames[2]+" wins with " + dState.getPlayerInfo()[2].getScore() + " points!";
        }
        if (playerNames.length >=4 && dState.getPlayerInfo()[0].getPlayerActive() &&dState.getPlayerInfo()[3].getScore() >=75){
            return playerNames[3]+" wins with " + dState.getPlayerInfo()[3].getScore() + " points!";
        }



        return null;
    }


    //TODO Finish this method and fix it.
    @Override
    protected boolean makeMove(GameAction action) {
        DominoGameState state = (DominoGameState) super.state;

        //int playerID;
        //playerID=state.getTurnID();
        int playerID = getPlayerIdx(action.getPlayer());

        //TODO Give better indication that game is blocked.

        if (state.isGameBlocked()){
            state.endRound();
            state.startRound(false);
            sendAction(new DominoPlacedAllPiecesAction(players[playerID], playerNames[playerID]));
        }


        if (canMove(playerID)){
            //TODO Give better indication that a round was won

            if (action instanceof DominoPlacedAllPiecesAction){
                DominoPlacedAllPiecesAction r = (DominoPlacedAllPiecesAction) action;
                state.setText(r.getName() + " won the round!");
                state.setText("Awarding points to "+r.getName());
                state.setText("Board has been reset");
                state.placedAllPieces(playerID);
                state.startRound(false);
                return true;
            }

            if (action instanceof DominoGameBlockedAction){
                state.setText("Game is blocked.");
                state.endRound();
                state.startRound(false);
            }

            if( action instanceof DominoMoveAction)
            {
                DominoMoveAction dm = (DominoMoveAction) action;
                int row = dm.getRow();
                int col = dm.getCol();
                int idx = dm.getDominoIndex();

                    if (state.placePiece(row,col,playerID,idx)) {

                        state.setText(playerNames[playerID] + " scored " +
                                state.getPlayerInfo()[playerID].getCurrentPoints() +" points");
                        state.getPlayerInfo()[playerID].setCurrentPoints(0);


                        state.getPlayerInfo()[playerID].getLegalMoves().clear();
                        state.findLegalMoves(playerID);
                        state.setBoneyardMsg("Boneyard(Dominoes remaining)\n"
                                + state.getBoneyard().size());

                        state.setBoneyardMsg(Integer.toString(state.getBoneyard().size()));

                       if (state.getPlayerInfo()[playerID].getHand().size() != 0) {
                            state.setTurnID();
                       }
                        return true;
                    }
                return false;
            }
            if (action instanceof DominoDrawAction)
            {
                int count = 0;
                while(state.getPlayerInfo()[playerID].getLegalMoves().size() == 0)
                {
                    if(state.getBoneyard().size() == 0)
                    {
                        return true;
                    }
                    state.setBoneyardMsg( "Boneyard(Dominoes remaining)\n"
                            + state.getBoneyard().size());
                    state.drawPiece(playerID);
                    count++;

                }
                state.setText(playerNames[playerID]+" draws x" + count +" domino");
                return true;
            }
            if( action instanceof DominoSkipAction)
            {

                state.setText(playerNames[playerID]+" cannot make a legal move. Their turn has been skipped");
                state.setTurnID();
                return true;
            }
            if (action instanceof DominoQuitGameAction)
            {
                state.setText("Player " + playerNames[playerID] + " has forfeited their turn");

                state.getPlayerInfo()[playerID].setPlayerActive(false);
                state.setTurnID();

                return true;
            }
            if (action instanceof DominoNewGameAction)
            {

                state.setTurnID();
                return true;
            }
        }


        //ss
        return false;
    }


}


