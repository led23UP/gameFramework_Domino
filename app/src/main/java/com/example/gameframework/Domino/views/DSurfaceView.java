package com.example.gameframework.Domino.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.gameframework.Domino.DominoHighlight;
import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.infoMessage.PlayerInfo;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.utilities.FlashSurfaceView;

import java.util.ArrayList;

/**
 * DSurfaceView is the surface view used by a human player.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DSurfaceView extends FlashSurfaceView {
    private static final String TAG = "DSurfaceView";

    protected DominoGameState dState;
    private ArrayList<DominoHighlight> highlights;
    private int selectedDomino;
    private int playerNum;

    public DSurfaceView(Context context) {
        super(context);
        init();
        setWillNotDraw(false);
        highlights = new ArrayList<>();
    }

    public DSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        setWillNotDraw(false);
        highlights = new ArrayList<>();
    }

    private void init(){
        setBackgroundResource(R.drawable.wood);
    }

    public void setState(DominoGameState state){
        this.dState = state;
    }

    public int dominoColor(){
        return Color.WHITE;
    }


    //Citation for domino pics
    //https://publicdomainvectors.org/en/tag/domino

    //draws the domino and green highlights
    public void onDraw(Canvas g){

        if (dState == null) {
            return;
        }

        for (int i = 0; i < dState.getBoardXSize(); i++){
            for (int j = 0; j < dState.getBoardYSize(i); j++){
                if (dState.getDomino(i,j).getLeftPipCount() == -1){
                    continue;
                }
                Domino d = dState.getDomino(i,j);
                drawDomino(g,d,i,j);
            }
        }

        drawHighlights(g, selectedDomino);
    }

    public void drawHighlights(Canvas g, int selectedDomino){
        highlights.clear();

        // If it is NOT the human player's turn, do not draw highlights.
        if (this.playerNum != dState.getTurnID()){
            return;
        }

        Paint p = new Paint();
        p.setColor(dominoColor());
        int whoseTurn;
        PlayerInfo playerInfo;

        ArrayList<MoveInfo> playerLegalMoves;

        whoseTurn=dState.getTurnID();
        playerInfo=dState.getPlayerInfo()[whoseTurn];

        playerLegalMoves=playerInfo.getLegalMoves();
        MoveInfo currentLegalMove;
        int numLegalMoves=playerLegalMoves.size();
        int row;
        int col;
        int orientation;
        Bitmap highlight = BitmapFactory.decodeResource(getResources(),R.drawable.domino_highlight);
        Matrix vertical=new Matrix();
        Matrix horizontal= new Matrix();
        vertical.postRotate(0);
        horizontal.postRotate(90);
        Bitmap verticalHighlight=Bitmap.createBitmap(highlight, 0, 0, highlight.getWidth(), highlight.getHeight(), vertical, true);
        Bitmap horizontalHighlight=Bitmap.createBitmap(highlight, 0, 0, highlight.getWidth(), highlight.getHeight(), horizontal, true);
        for(int i=0;i<numLegalMoves;i++){
            if (!(playerLegalMoves.get(i).getDominoIndex() == selectedDomino)){
                continue;
            }
            currentLegalMove = playerLegalMoves.get(i);
            row=currentLegalMove.getRow();
            col=currentLegalMove.getCol();
            orientation= currentLegalMove.getOrientation();
            if(orientation==1 || orientation==3){
                highlights.add(new DominoHighlight(col*150+2500,row*150+2500,orientation,currentLegalMove));
                g.drawBitmap(horizontalHighlight,col*150+2500, row*150+2500,p );

            }
            else if(orientation==2 || orientation==4){
                highlights.add(new DominoHighlight(col*150+2500,row*150+2500,orientation,currentLegalMove));
                g.drawBitmap(verticalHighlight,col*150+2500,row*150+2500,p );
            }
        }
    }

    public void drawDomino(Canvas g, Domino d, int row, int col){
        float xLoc = col*150+2500;
        float yLoc = row*150+2500;
        // If domino is invalid, DO NOT DRAW.
        if (d.getLeftPipCount() == -1 ||d.getRightPipCount() == -1 ){
            return;
        }

        int leftPipCount=d.getLeftPipCount();
        int rightPipCount=d.getRightPipCount();
        int dominoOrientation=d.getOrientation();
        //matricies rotate domino into the different orientations
        //for example matrix one rotates domino bitmap into orientation number 1
        Matrix one= new Matrix();
        Matrix two= new Matrix();
        Matrix three= new Matrix();
        Matrix four= new Matrix();
        String dominoClipartId=null;

        one.postRotate(270);
        two.postRotate(0);
        three.postRotate(90);
        four.postRotate(180);

        if(leftPipCount<=rightPipCount) {
            dominoClipartId = "domino" + leftPipCount + "_" + rightPipCount;
        }
        if (leftPipCount > rightPipCount){
            dominoClipartId="domino"+rightPipCount+"_"+leftPipCount;
            two.postRotate(180);
            four.postRotate(0);
        }

        //converts string id into an int id to link domino btmap image to corresponding resource file
        int integerDominoID = DSurfaceView.this.getResources().getIdentifier(dominoClipartId , "drawable", getContext().getPackageName());
        Bitmap dominoImage= BitmapFactory.decodeResource(getResources(),integerDominoID );
        Bitmap rotatedDominoImage=null;
        if(dominoOrientation==1) {
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), one, true);
        }
        else if(dominoOrientation==2){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), two, true);
        }
        else if(dominoOrientation==3){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), three, true);
        }

        else if(dominoOrientation==4){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), four, true);
        }

        g.drawBitmap(rotatedDominoImage,xLoc, yLoc,null);
    }

    public MoveInfo clickedInsideHighlight(float x, float y){
        for (int i = 0; i < highlights.size(); i++){
            if (highlights.get(i).isInHighlight(x,y)){
                return highlights.get(i).getMoveInfo();
            }
        }
        return null;
    }

    public void setSelectedDomino(int sD){
        this.selectedDomino = sD;
    }

    // This is only called once to set the surfaceViews playerNum to the
    // human player's playerID.
    public void setSVPlayerID(int ID){
        this.playerNum = ID;
    }

}
