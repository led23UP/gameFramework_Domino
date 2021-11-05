package com.example.gameframework.Domino.players;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        switch(playerNum){
            case 0:
                player0ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[0].getScore()));
            case 1:
                player1ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[1].getScore()));
            case 2:
                player2ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[2].getScore()));
            case 3:
                player3ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[3].getScore()));
        }

        /*player1ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[1].getScore()));
        player2ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[2].getScore()));
        player3ScoreView.setText(String.valueOf(gameInfo.getPlayerInfo()[3].getScore()));*/


        surfaceView.setState((DominoGameState)info);
        // Only show them imageButtons for dominoes that are in their hand.
        for (int i = 0; i < gameInfo.getPlayerInfo()[playerNum].getHand().size(); i++){
            dominosInHand[i].setVisibility(View.VISIBLE);
            dominosInHand[i].setImageResource(determineDominoPic(gameInfo.getPlayerInfo()[playerNum].getHand().get(i)));
            dominosInHand[i].setClickable(true);
        }

    }

    private int determineDominoPic(Domino d){
        switch (d.getWeight()){
            case 0:
                return R.drawable.domino0_1;
            case 1:
                return R.drawable.domino0_2;
            case 2:
                return R.drawable.domino0_3;
            case 3:
                return R.drawable.domino0_4;
            case 4:
                return R.drawable.domino0_5;
            case 5:
                return R.drawable.domino0_6;
            case 6:
                return R.drawable.domino1_2;
            case 7:
                return R.drawable.domino1_3;
            case 8:
                return R.drawable.domino1_4;
            case 9:
                return R.drawable.domino1_5;
            case 10:
                return R.drawable.domino1_6;
            case 11:
                return R.drawable.domino2_3;
            case 12:
                return R.drawable.domino2_4;
            case 13:
                return R.drawable.domino2_5;
            case 14:
                return R.drawable.domino2_6;
            case 15:
                return R.drawable.domino3_4;
            case 16:
                return R.drawable.domino3_5;
            case 17:
                return R.drawable.domino3_6;
            case 18:
                return R.drawable.domino4_5;
            case 19:
                return R.drawable.domino4_6;
            case 20:
                return R.drawable.domino5_6;
            case 21:
                return R.drawable.domino0_0;
            case 22:
                return R.drawable.domino1_1;
            case 23:
                return R.drawable.domino2_2;
            case 24:
                return R.drawable.domino3_3;
            case 25:
                return R.drawable.domino4_4;
            case 26:
                return R.drawable.domino5_5;
            case 27:
                return R.drawable.domino6_6;
            default:
                return R.drawable.domino_highlight;
        }
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
        // Don't want to write 23 lines of findViewById, so the ImageButtons are being declared this way.
        int[] bIdArray = {R.id.hand0,R.id.hand1,R.id.hand2,R.id.hand3,R.id.hand4,R.id.hand5,R.id.hand6,
                R.id.hand7,R.id.hand8,R.id.hand9,R.id.hand10,R.id.hand11,R.id.hand12,R.id.hand13,
                R.id.hand14,R.id.hand15,R.id.hand16,R.id.hand17,R.id.hand18,R.id.hand19,R.id.hand20,
                R.id.hand21,R.id.hand22};

        for (int i = 0; i < bIdArray.length; i++){
            dominosInHand[i] = activity.findViewById(bIdArray[i]);
            dominosInHand[i].setOnClickListener(this);
            dominosInHand[i].setClickable(false);
            dominosInHand[i].setVisibility(View.INVISIBLE);
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
        if (view instanceof ImageButton) {
            //TODO Update selectedDomino to which button they pressed.
            // selectedDomino = the buttons position in the imageButton array.
            int clickedId = view.getId();
            int i = 0;
            for (ImageButton imageBT : dominosInHand) {
                if (imageBT.getId() == clickedId) {

                    selectedDomino = i;
                }

                i++;
            }
            Logger.log("onClick",String.valueOf(selectedDomino));
        }
        else if (view instanceof Button){
            if (view.getId() == R.id.newGameButton){
                //TODO Perform newGame action.
            }
            else if (view.getId() == R.id.quitGameButton){
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
            surfaceView.flash(Color.GREEN, 50);
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
