package com.example.gameframework.Domino;

import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.players.DominoComputerPlayers1;
import com.example.gameframework.Domino.players.DominoComputerPlayers2;
import com.example.gameframework.Domino.players.DominoHumanPlayers1;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.GameMainActivity;
import com.example.gameframework.game.GameFramework.LocalGame;
import com.example.gameframework.game.GameFramework.gameConfiguration.GameConfig;
import com.example.gameframework.game.GameFramework.gameConfiguration.GamePlayerType;
import com.example.gameframework.game.GameFramework.infoMessage.GameState;
import com.example.gameframework.game.GameFramework.players.GamePlayer;
import com.example.gameframework.game.GameFramework.utilities.Logger;
import com.example.gameframework.game.GameFramework.utilities.Saving;

import java.util.ArrayList;

public class DominoMainActivity extends GameMainActivity {
    //Tag for logging
    private static final String TAG = "DominoMainActivity";
    public static final int PORT_NUMBER = 5213;

    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // yellow-on-blue GUI
        playerTypes.add(new GamePlayerType("Local Human Player (blue-yellow)") {
            public GamePlayer createPlayer(String name) {
                return new DominoHumanPlayers1(name, R.layout.domino_human_player1);
            }
        });

        // red-on-yellow GUI
        /*playerTypes.add(new GamePlayerType("Local Human Player (yellow-red)") {
            public GamePlayer createPlayer(String name) {
                return new DominoHumanPlayers1(name, R.layout.ttt_human_player1_flipped);
            }
        });*/

        // dumb computer player
        playerTypes.add(new GamePlayerType("Computer Player (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new DominoComputerPlayers1(name);
            }
        });

        /*// smarter computer player
        playerTypes.add(new GamePlayerType("Computer Player (smart)") {
            public GamePlayer createPlayer(String name) {
                return new DominoComputerPlayers2(name);
            }
        });*/

        // Create a game configuration class for Tic-tac-toe
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 4, "Dominos", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0); // yellow-on-blue GUI
        defaultConfig.addPlayer("Computer", 3); // dumb computer player

        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Remote Player", "", 1); // red-on-yellow GUI

        //done!
        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if (gameState == null) {
            return new DominoLocalGame();
        }
        return new DominoLocalGame((DominoGameState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName Desired save name
     * @return String representation of the save
     */
    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the game specific state
     *
     * @param gameName The file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName) {
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);
        return (GameState) new DominoGameState((DominoGameState) Saving.readFromFile(appName, this.getApplicationContext()));
    }
}

