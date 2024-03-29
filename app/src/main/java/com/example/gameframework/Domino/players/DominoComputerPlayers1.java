package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoGameBlockedAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoPlacedAllPiecesAction;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;
import com.example.gameframework.game.GameFramework.utilities.Logger;

/**
 * DominoComputerPlayers1 is the dumb computer player.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoComputerPlayers1 extends GameComputerPlayer {
    private int r;
    private int c;
    private int idx;
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
        if (info instanceof IllegalMoveInfo){

            game.sendAction(new DominoMoveAction(this, r,c,idx));
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

        //grabs the first legal move available and store it for use later
        r = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getRow();
        c = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getCol();
        idx = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getDominoIndex();

        Logger.log("i", "Row: " + r + " Col: " + c + " Index: " + idx);

        sleep(1);
        game.sendAction(new DominoMoveAction(this, r,c,idx));

    }
}
