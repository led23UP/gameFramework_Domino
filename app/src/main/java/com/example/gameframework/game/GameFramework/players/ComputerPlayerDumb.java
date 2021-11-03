package com.example.gameframework.game.GameFramework.players;

import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;

public class ComputerPlayerDumb extends GameComputerPlayer{

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ComputerPlayerDumb(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {

    }
}
