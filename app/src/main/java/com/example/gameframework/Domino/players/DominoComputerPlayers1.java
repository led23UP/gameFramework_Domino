package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.infoMessage.PlayerInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;

public class DominoComputerPlayers1 extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoComputerPlayers1(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if (info instanceof NotYourTurnInfo){
            return;
        }

        DominoGameState gameStateObj = new DominoGameState((DominoGameState) info);
        // Get the player's legal moves, clear, then update them.
        gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().clear();
        gameStateObj.findLegalMoves(playerNum);

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
        /*

        */

        //grabs the first legal move available and store it for use later
        int row, col, idx;
        row = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getRow();
        col = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getCol();
        idx = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getDominoIndex();

        //removes the legalMove from array since we will play that move.
        //gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().remove(0);

        sleep(1);
        game.sendAction(new DominoMoveAction(this, row,col,idx));

    }
}
