package com.example.gameframework.Domino.players;

import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.gameframework.Domino.DominoActionMessage.DominoDrawAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoMoveAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoNewRoundAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoQuitGameAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoSkipAction;
import com.example.gameframework.Domino.DominoActionMessage.DominoPlacedAllPiecesAction;
import com.example.gameframework.Domino.DominoMainActivity;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.views.DSurfaceView;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.GameMainActivity;
import com.example.gameframework.game.GameFramework.infoMessage.GameInfo;
import com.example.gameframework.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gameframework.game.GameFramework.players.GameHumanPlayer;
import com.example.gameframework.game.GameFramework.utilities.Logger;
import com.example.gameframework.game.GameFramework.utilities.MessageBox;

import java.util.ArrayList;

public class DominoHumanPlayers1 extends GameHumanPlayer implements View.OnClickListener,
    View.OnTouchListener{
    private static final String TAG = "DominoHumanPlayer1";

    private ImageButton[] dominosInHand;
    private TextView player0ScoreView;
    private TextView player1ScoreView;
    private TextView player2ScoreView;
    private TextView player3ScoreView;

    private TextView messageText;

    private TextView boneyardText;

    private DSurfaceView surfaceView;
    private Button helpButton;
    private Button newGameButton;
    private Button quitGameButton;

    private ScrollView myScrollViewV;
    private HorizontalScrollView myScrollViewH;

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
        this.selectedDomino = 0;
    }

    @Override
    public void receiveInfo(GameInfo info) {
        if (surfaceView == null){
            return;
        }

        if (info instanceof NotYourTurnInfo){
            //surfaceView.flash(Color.RED, 50);
            return;
        }
        else if (!(info instanceof DominoGameState)){
            return;
        }

        surfaceView.invalidate();

        // Cast info as a DominoGameState to get information from it.
        DominoGameState gameInfo = (DominoGameState) info;

        if (((DominoGameState) info).boardEmpty){
            myScrollViewV.post(new Runnable() {
                @Override
                public void run() {
                    //setting position here :
                    myScrollViewV.scrollTo(0, 2500);
                }
            });

            myScrollViewH.post(new Runnable() {
                @Override
                public void run() {
                    //setting position here :
                    myScrollViewH.scrollTo(2500, 0);
                }
            });
        }


        if (gameInfo.getPlayerInfo()[playerNum].getHand().size() == 0){
            game.sendAction(new DominoPlacedAllPiecesAction(this,name));
        }

        // Update player score TextViews.
        player0ScoreView.setText("");
        player1ScoreView.setText("");
        player2ScoreView.setText("");
        player3ScoreView.setText("");

        //messageText.setText(gameInfo.getMessage());
        messageText.setText(gameInfo.getText().get(0)+gameInfo.getText().get(1)
                +gameInfo.getText().get(2)+gameInfo.getText().get(3));



        messageText.setTextColor(Color.YELLOW);
        boneyardText.setText(gameInfo.getBoneyardMsg());
        boneyardText.setTextColor(Color.YELLOW);

        if(gameInfo.getTurnID() == 0) {
            player0ScoreView.setTextColor(Color.YELLOW);
            player1ScoreView.setTextColor(Color.WHITE);
            player2ScoreView.setTextColor(Color.WHITE);
            player3ScoreView.setTextColor(Color.WHITE);
        }
        if(gameInfo.getTurnID() == 1) {
            player1ScoreView.setTextColor(Color.YELLOW);
            player0ScoreView.setTextColor(Color.WHITE);
            player2ScoreView.setTextColor(Color.WHITE);
            player3ScoreView.setTextColor(Color.WHITE);
        }
        if(gameInfo.getTurnID() == 2) {
            player2ScoreView.setTextColor(Color.YELLOW);
            player1ScoreView.setTextColor(Color.WHITE);
            player0ScoreView.setTextColor(Color.WHITE);
            player3ScoreView.setTextColor(Color.WHITE);
        }
        if(gameInfo.getTurnID() == 3) {
            player3ScoreView.setTextColor(Color.YELLOW);
            player1ScoreView.setTextColor(Color.WHITE);
            player2ScoreView.setTextColor(Color.WHITE);
            player0ScoreView.setTextColor(Color.WHITE);
        }

        switch(gameInfo.getPlayerInfo().length){
            case 4:
                player3ScoreView.setText(allPlayerNames[3]+" "+ gameInfo.getPlayerInfo()[3].getScore());

            case 3:
                player2ScoreView.setText(allPlayerNames[2]+" "+ gameInfo.getPlayerInfo()[2].getScore());
            case 2:
                player1ScoreView.setText(allPlayerNames[1]+" "+ gameInfo.getPlayerInfo()[1].getScore());
            case 1:

                player0ScoreView.setText(allPlayerNames[0]+ gameInfo.getPlayerInfo()[0].getScore());

        }

        surfaceView.setState((DominoGameState)info);
        // Only show them imageButtons for dominoes that are in their hand.
        for (int i = 0; i < gameInfo.getPlayerInfo()[playerNum].getHand().size(); i++){
            dominosInHand[i].setVisibility(View.VISIBLE);
            dominosInHand[i].setImageResource(determineDominoPic(gameInfo.getPlayerInfo()[playerNum].getHand().get(i)));
            dominosInHand[i].setClickable(true);
        }

        for (int i = gameInfo.getPlayerInfo()[playerNum].getHand().size(); i < 23; i++){
            dominosInHand[i].setVisibility(View.INVISIBLE);
            dominosInHand[i].setClickable(false);
        }

        // Get legal moves.
        ArrayList<MoveInfo> myLegalMoves = gameInfo.getPlayerInfo()[playerNum].getLegalMoves();
        if(myLegalMoves.size() == 0) {
            if (gameInfo.getBoneyard().size() == 0){
                game.sendAction(new DominoSkipAction(this));
                return;
            }
            game.sendAction(new DominoDrawAction(this));
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

        this.messageText = (TextView)activity.findViewById(R.id.messageTextView);
        this.boneyardText = (TextView)activity.findViewById(R.id.boneyardTextView);
        this.newGameButton = (Button)activity.findViewById(R.id.newGameButton);
        this.quitGameButton = (Button)activity.findViewById(R.id.quitGameButton);
        this.helpButton = (Button)activity.findViewById(R.id.helpButton);

        this.myScrollViewV = activity.findViewById(R.id.scrollV);

        myScrollViewV.post(new Runnable() {
            @Override
            public void run() {
                //setting position here :
                myScrollViewV.scrollTo(0, 2500);
            }
        });

        this.myScrollViewH = activity.findViewById(R.id.scrollH);

        myScrollViewH.post(new Runnable() {
            @Override
            public void run() {
                //setting position here :
                myScrollViewH.scrollTo(2500, 0);
            }
        });

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
        this.surfaceView.setSVPlayerID(playerNum);
        Logger.log("set listener", "OnTouch");
        surfaceView.setOnTouchListener(this);

        helpButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        quitGameButton.setOnClickListener(this);

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
            int clickedId = view.getId();
            int i = 0;
            for (ImageButton imageBT : dominosInHand) {
                if (imageBT.getId() == clickedId) {
                    selectedDomino = i;
                }
                i++;
            }
            Logger.log("onClick",String.valueOf(selectedDomino));
            surfaceView.setSelectedDomino(selectedDomino);
            surfaceView.invalidate();
        }

        else if (view instanceof Button){
            if (view.getId() == R.id.newGameButton){
                myActivity.restartGame();
            }
            else if (view.getId() == R.id.quitGameButton){
                game.sendAction(new DominoQuitGameAction(this));
            }
            else if (view.getId() == R.id.helpButton){

                //getActivity().startActivity(new Intent(getActivity(), Pop.class));

                MessageBox.popUpMessage(
                        "HOW TO PLAY ALL 3'S DOMINOES: \n\n" +
                        "1. When it is your turn (indicated by player name being yellow), " +
                                "select a domino from your scrollable hand. The game will " +
                                "automatically highlight on the board, all legal moves with the " +
                                "selected domino\n\n" +
                        "2. Select one of the highlighted squares to place down a piece.\n\n" +
                        "3. The game will automatically switch to the next player\n\n" +
                        "RULES\n\n" +
                                "1.Player with the highest double, goes first and " +
                                "subsequently, must place down that double\n\n" +
                                "2.Player who reaches 100 points first, wins the game\n\n" +
                                "3. When all Dominoes are played, reset the board and " +
                                "award the player points\n\n" +
                                "4. Domino pips (the ends) must match the pips of the place " +
                                "down domino ",myActivity);

            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // If the event is not an up event, return.
        if (event.getAction() != MotionEvent.ACTION_UP){
            return true;
        }

        float x =  event.getX();
        float y =  event.getY();
        // If the user clicked inside a highlight, they will get MoveInfo.
        // If the user did not, they will get null.
        MoveInfo a = surfaceView.clickedInsideHighlight(x,y);
        // If the MoveInfo is null, flash screen.

        if (a == null){
            surfaceView.flash(Color.RED, 50);
        }
        // If move info is valid, send a new MoveAction.
        else{
            DominoMoveAction action = new DominoMoveAction(this,a.getRow(),a.getCol(),selectedDomino);
            Logger.log("onTouch", "Human player sending move action");
            game.sendAction(action);
            selectedDomino = -1;
            surfaceView.invalidate();
        }
        return true;
    }




}

