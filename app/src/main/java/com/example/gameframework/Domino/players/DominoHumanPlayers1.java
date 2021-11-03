package com.example.gameframework.Domino.players;

import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.views.DSurfaceView;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.GameMainActivity;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gameframework.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gameframework.game.GameFramework.players.GameHumanPlayer;
import com.example.gameframework.game.GameFramework.utilities.Logger;

import java.util.ArrayList;

public class DominoHumanPlayers1 extends GameHumanPlayer implements View.OnClickListener,
    View.OnTouchListener{
    private static final String TAG = "DominoHumanPlayer1";

    private ImageButton[] dominosInHand;
    private TextView player0ScoreView;
    private TextView player1ScoreView;
    private TextView player2ScoreView;
    private TextView player3ScoreView;
    private DSurfaceView surfaceView;
    private Button helpButton;
    private Button newGameButton;
    private Button quitGameButton;

    private int layoutId;
    private int selectedDomino;

    private GameMainActivity myActivity;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DominoHumanPlayers1(String name, int layoutId) {
        super(name);
        this.layoutId = layoutId;
        this.selectedDomino = -1;
    }

    @Override
    public void receiveInfo(GameInfo info) {
        if (surfaceView == null){
            return;
        }

        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo){
            surfaceView.flash(Color.RED, 50);
            return;
        }

        if (!(info instanceof DominoGameState)){
            return;
        }
        // Cast info as a DominoGameState to get information from it.
        DominoGameState gameInfo = (DominoGameState) info;
        // Update player score TextViews.
        player0ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[0].getScore()));
        player1ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[1].getScore()));
        player2ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[2].getScore()));
        player3ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[3].getScore()));


        surfaceView.setState((DominoGameState)info);
        // Only show them imageButtons for dominoes that are in their hand.
        for (int i = 0; i < gameInfo.getPlayerInfo()[playerNum].getHand().size(); i++){
            dominosInHand[i].setVisibility(View.VISIBLE);
        }
        surfaceView.invalidate();
        Logger.log(TAG, "recieving");

    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;
        activity.setContentView(layoutId);

        this.player0ScoreView = (TextView)activity.findViewById(R.id.player0Score);
        this.player1ScoreView = (TextView)activity.findViewById(R.id.player1Score);
        this.player2ScoreView = (TextView)activity.findViewById(R.id.player2Score);
        this.player3ScoreView = (TextView)activity.findViewById(R.id.player3Score);
        this.newGameButton = (Button)activity.findViewById(R.id.newGameButton);
        this.quitGameButton = (Button)activity.findViewById(R.id.quitGameButton);
        this.helpButton = (Button)activity.findViewById(R.id.helpButton);

        this.dominosInHand = new ImageButton[23];
        int[] numbers = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22};
        for (int i = 0; i < 23; i++){
            int id = Integer.parseInt("R.id.hand" + numbers[i]);
            dominosInHand[i] = activity.findViewById(id);
        }

        // If the domino isn't in their hand, set the remaining buttons alpha to zero.

        this.surfaceView = (DSurfaceView) myActivity.findViewById(R.id.surfaceView);
        Logger.log("set listener", "OnTouch");
        surfaceView.setOnTouchListener(this);

    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    protected void initAfterReady(){
        switch (allPlayerNames.length) {
            case 2:
                myActivity.setTitle("Domino: " + allPlayerNames[0] + "vs. " + allPlayerNames[1]);
                break;
            case 3:
                myActivity.setTitle("Domino: " + allPlayerNames[0] + "vs. " + allPlayerNames[1] +
                        " vs. " + allPlayerNames[2]);
                break;
            case 4:
                myActivity.setTitle("Domino: " + allPlayerNames[0] + "vs. " + allPlayerNames[1] +
                        " vs. " + allPlayerNames[2] + " vs. " + allPlayerNames[3]);
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageButton){
            //TODO Update selectedDomino to which button they pressed.
            // selectedDomino = the buttons position in the imageButton array.
        }
        else if (view instanceof Button){
            if (view.getId() == R.id.newGame){
                //TODO Perform newGame action.
            }
            else if (view.getId() == R.id.quitGame){
                //TODO Perform quitGame action.
            }
            else if (view.getId() == R.id.helpButton){
                //TODO Perform help action. (Bring up manual.)
            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP){
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();
        Point p = surfaceView.mapPixelToSquare(x,y);

        if (p == null){
            surfaceView.flash(Color.RED, 50);
        }
        else{
            DominoMoveAction action = new DominoMoveAction(this,p.x,p.y,selectedDomino);
            Logger.log("onTouch", "Human player sending move action");
            game.sendAction(action);
            surfaceView.invalidate();
        }
        return true;
    }
}
