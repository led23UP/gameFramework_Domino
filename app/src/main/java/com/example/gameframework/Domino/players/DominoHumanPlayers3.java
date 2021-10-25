package com.example.gameframework.Domino.players;

import android.view.View;

import com.example.gameframework.game.GameFramework.GameMainActivity;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.players.GameHumanPlayer;

public class DominoHumanPlayers3 extends GameHumanPlayer {
    /**
     * constructor
     *
     * @param name the name of the player
     */
    public DominoHumanPlayers3(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }
}
