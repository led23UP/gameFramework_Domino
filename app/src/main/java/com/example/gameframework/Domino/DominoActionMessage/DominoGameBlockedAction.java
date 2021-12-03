package com.example.gameframework.Domino.DominoActionMessage;

import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

/**
 * DominoGameBlockedAction is a class that represents a blocked action.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoGameBlockedAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DominoGameBlockedAction(GamePlayer player) {
        super(player);
    }
}
