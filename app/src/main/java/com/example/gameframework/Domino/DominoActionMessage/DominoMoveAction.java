package com.example.gameframework.Domino.DominoActionMessage;

import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

/**
 * DominoMoveAction is a class that represents a move action.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoMoveAction extends GameAction {
    //Tag for logging
    private static final String TAG = "DominoMoveAction";
    private static final long serialVersionUID = -2242980258970485343L;

    // instance variables: the selected row and column
    private final int row;
    private final int col;
    private final int dominoIndex;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DominoMoveAction(GamePlayer player,int row, int col, int d) {
        super(player);
        this.row = row;
        this.col = col;
        this.dominoIndex = d;
    }
    /**
     * get the object's row
     *
     * @return the row selected
     */
    public int getRow() { return row; }

    /**
     * get the object's column
     *
     * @return the column selected
     */
    public int getCol() { return col; }

    public int getDominoIndex(){
        return dominoIndex;
    }
}
