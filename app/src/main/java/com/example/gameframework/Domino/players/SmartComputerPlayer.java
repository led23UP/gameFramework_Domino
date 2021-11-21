package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoGameBlockedAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoPlacedAllPiecesAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;
import com.example.gameframework.game.GameFramework.utilities.Logger;

import java.util.ArrayList;


public class SmartComputerPlayer extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public SmartComputerPlayer(String name) {
        super(name);
    }
    int row;
    int col;
    int dominoIndex;

    @Override
    protected void receiveInfo(GameInfo info) {
        if (info instanceof NotYourTurnInfo){
            return;
        }
        if (info instanceof IllegalMoveInfo){

            game.sendAction(new DominoMoveAction(this, row,col,dominoIndex));
            Logger.log("I", "Illegal move, resending prev. action");
            return;
        }



        DominoGameState gameStateObj = new DominoGameState((DominoGameState) info);

        if (gameStateObj.isGameBlocked()){
            game.sendAction(new DominoGameBlockedAction(this));
        }

        if (gameStateObj.getPlayerInfo()[playerNum].getHand().size() == 0){
            game.sendAction(new DominoPlacedAllPiecesAction(this,name));
        }

        //if player doesn't have legal move, draw until there is a legal move. If boneyard is empty
        //skip turn
        if(gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0 &&
                gameStateObj.getBoneyard().size() == 0)
        {
            sleep(1);
            game.sendAction(new DominoSkipAction(this));
            return;
        }
        if(gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0)
        {
            game.sendAction(new DominoDrawAction(this));
            return;
        }
       ArrayList<MoveInfo> playerlegalMoves= gameStateObj.getPlayerInfo()[playerNum].getLegalMoves();
        int maxPoints=-999;
        int maxMoveIndex=-999;

        for(int i=0;i<playerlegalMoves.size();i++){
            DominoGameState tempGameState= new DominoGameState(gameStateObj);

            row=(playerlegalMoves.get(i)).getRow();
            col=(playerlegalMoves.get(i)).getCol();
            dominoIndex=(playerlegalMoves.get(i)).getDominoIndex();
            tempGameState.placePiece(row,col,playerNum,dominoIndex);

            int currentMovePoints=tempGameState.reportScoredPoints(playerNum);

            if(currentMovePoints>maxPoints){
                maxPoints=currentMovePoints;
                maxMoveIndex=i;


            }




        }

        MoveInfo bestMove=gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(maxMoveIndex);


        Logger.log("i", "Row: " + row + " Col: " + col + " Index: " + dominoIndex);

        sleep(1);
        game.sendAction(new DominoMoveAction(this, bestMove.getRow(), bestMove.getCol(), bestMove.getDominoIndex()));

    }
}
