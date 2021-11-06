package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;

public class DominoComputerPlayers2 extends GameComputerPlayer {
    private int score;
    private ArrayList<Domino> playerHand;
    private ArrayList<MoveInfo> legalMoves;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoComputerPlayers2(String name) {
        super(name);
        score = 0;
        playerHand = new ArrayList<>();
        legalMoves = new ArrayList<>();
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        DominoGameState gameStateObj = new DominoGameState((DominoGameState) info);


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
        //grabs the first legal move available and store it for use later
        int row = 0, col = 0, idx = 0;
        row = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getRow();
        col = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getCol();
        idx = gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().get(0).getDominoIndex();

        //removes the legalMove from array since we will play that move.
        gameStateObj.getPlayerInfo()[playerNum].getLegalMoves().remove(0);

        sleep(1);
        game.sendAction(new DominoMoveAction(this, row,col,idx));

    }
}
