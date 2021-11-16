package com.example.gameframework.Domino.infoMessage;

import junit.framework.TestCase;

import java.util.ArrayList;

public class DominoGameStateTest extends TestCase {



    public void testGetTurnID() {
        DominoGameState state = new DominoGameState();
        state.setNumPlayersStart(2);
        state.setTurnID();//0
        state.setTurnID();//1
        state.setTurnID();//0
        //state.setTurnID();//1
        //state.setTurnID();//0

        assertEquals(0, state.getTurnID());

    }

    public void testQuitGame() {
        DominoGameState state = new DominoGameState();
        state.setNumPlayersStart(1);
        PlayerInfo p1 = state.getPlayerInfo()[0];
        state.quitGame(p1.getID());
        assertEquals(false, p1.getPlayerActive());
    }

    public void testIsGameBlocked() {
        DominoGameState state2 = new DominoGameState();
        state2.setNumPlayersStart(1);

        assertFalse(state2.isGameBlocked());

    }
}