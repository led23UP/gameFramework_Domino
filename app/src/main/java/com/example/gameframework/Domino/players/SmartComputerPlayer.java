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
        //if not turn do nothing
        if (info instanceof NotYourTurnInfo) {
            return;
        }
        //if illegal move
        if (info instanceof IllegalMoveInfo) {

            game.sendAction(new DominoMoveAction(this, row, col, dominoIndex));
            Logger.log("I", "Illegal move, resending prev. action");
            return;
        }

        DominoGameState gameStateObj = new DominoGameState((DominoGameState) info);
//if game blocked send game blocked action
        if (gameStateObj.isGameBlocked()) {
            game.sendAction(new DominoGameBlockedAction(this));
        }
//if player placed all pieces
        if (gameStateObj.getPlayerInfo()[playerNum].getHand().size() == 0) {
            game.sendAction(new DominoPlacedAllPiecesAction(this, name));
        }

        //if player doesn't have legal move, draw until there is a legal move. If boneyard is empty
        //skip turn
        if (gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0 &&
                gameStateObj.getBoneyard().size() == 0) {
            sleep(1);
            game.sendAction(new DominoSkipAction(this));
            return;
        }
        if (gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0) {
            game.sendAction(new DominoDrawAction(this));
            return;
        }
        //getting legal moves of smart computer player
        ArrayList<MoveInfo> playerlegalMoves = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves();
        int maxPoints = -999;
        int maxMoveIndex = -999;
//for loop walks through all the legal moves of smart computer player
        for (int i = 0; i < playerlegalMoves.size(); i++) {
            //temporary state initialized from current state of game
            DominoGameState tempGameState = new DominoGameState(gameStateObj);
//row of legal move
            row = (playerlegalMoves.get(i)).getRow();
            //column of legal move
            col = (playerlegalMoves.get(i)).getCol();
            //get domino index of move
            dominoIndex = (playerlegalMoves.get(i)).getDominoIndex();
            //applying move to temporary game state
            tempGameState.placePiece(row, col, playerNum, dominoIndex);

            int currentMovePoints = tempGameState.reportScoredPoints(playerNum);
//finds the maximum points generated from each move
            if (currentMovePoints > maxPoints) {
                maxPoints = currentMovePoints;
                maxMoveIndex = i;
            }
        }
//the best move based on the maxpoints
        MoveInfo bestMove = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(maxMoveIndex);

        Logger.log("i", "Row: " + row + " Col: " + col + " Index: " + dominoIndex);

        sleep(1);
        //sends best move to game
        game.sendAction(new DominoMoveAction(this, bestMove.getRow(), bestMove.getCol(), bestMove.getDominoIndex()));

    }
}
