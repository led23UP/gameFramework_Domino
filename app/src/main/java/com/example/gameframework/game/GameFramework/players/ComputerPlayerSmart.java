package com.example.gameframework.game.GameFramework.players;

import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;

public class ComputerPlayerSmart extends GameComputerPlayer{

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ComputerPlayerSmart(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {

    }
}
