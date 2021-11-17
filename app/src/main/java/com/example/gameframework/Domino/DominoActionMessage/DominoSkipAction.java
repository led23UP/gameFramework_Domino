package com.example.gameframework.Domino.DominoActionMessage;

import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

public class DominoSkipAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DominoSkipAction(GamePlayer player) {
        super(player);
        
    }

}
