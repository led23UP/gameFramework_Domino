package com.example.gameframework.Domino.DominoActionMessage;

import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

/**
 * DominoDrawAction is a class that represents a draw action.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoDrawAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DominoDrawAction(GamePlayer player) {
        super(player);
    }
}
