package com.example.gameframework.Domino.DominoActionMessage;

import com.example.gameframework.game.GameFramework.actionMessage.GameAction;
import com.example.gameframework.game.GameFramework.players.GamePlayer;

public class DominoPlacedAllPiecesAction extends GameAction {
    private String name;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DominoPlacedAllPiecesAction(GamePlayer player, String name) {
        super(player);
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
