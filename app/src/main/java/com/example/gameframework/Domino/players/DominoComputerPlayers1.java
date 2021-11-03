package com.example.gameframework.Domino.players;

import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;

public class DominoComputerPlayers1 extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     **/

    //dumb ai
    public DominoComputerPlayers1(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
    }
}
