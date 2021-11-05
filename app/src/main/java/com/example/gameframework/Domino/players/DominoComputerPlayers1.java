package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.infoMessage.PlayerInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;

public class DominoComputerPlayers1 extends GameComputerPlayer {
    private int score;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoComputerPlayers1(String name) {
        super(name);
        score = 0;
        playerHand = new ArrayList<>();
        legalMoves = new ArrayList<>();
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        //Creates copy of gameState
        DominoGameState gameStateObj = new DominoGameState((DominoGameState) info);

        //if there are no legal moves, the game will incremnt the turn, wait, then send a skip action
        if(gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0)

        //if player doesn't have legal move, draw until there is a legal move. If boneyard is empty
        //skip turn
        while(gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().size() == 0)
        {
            if(gameStateObj.getBoneyard().size() == 0)
            {
                sleep(1);
                game.sendAction(new DominoSkipAction(this));
                return;
            }
            gameStateObj.drawPiece(playerNum);
        }

        //if there's a legal move to be made

        int row = 0, col = 0, idx = 0;
        //gets vars for first possible legal move
        row = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getRow();
        col = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getCol();
        idx = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getDominoIndex();

        //removes the legalMove from array since we will play that move.
        //Not sure if this should be here or in local game
        gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().remove(0);

        sleep(1);
        sleep(1000);
        //sends first legal move to the game
        game.sendAction(new DominoMoveAction(this, row,col,idx));

    }
}
