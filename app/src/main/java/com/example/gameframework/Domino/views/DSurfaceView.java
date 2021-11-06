package com.example.gameframework.Domino.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.Domino.infoMessage.PlayerInfo;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gameframework.game.GameFramework.utilities.Logger;

import java.util.ArrayList;

public class DSurfaceView extends FlashSurfaceView {
    private static final String TAG = "DSurfaceView";

    private final static float BORDER_PERCENT = 5;
    private final static float SQUARE_SIZE_PERCENT = 8;
    private final static float LINE_WIDTH_PERCENT = 3;
    private final static float SQUARE_DELTA_PERCENT = SQUARE_SIZE_PERCENT + LINE_WIDTH_PERCENT;

    protected DominoGameState dState;
    protected float hBase;
    protected float vBase;

    protected float fullSquare;


    public DSurfaceView(Context context) {
        super(context);
        init();
        setWillNotDraw(false);
    }

    public DSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        setWillNotDraw(false);
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

    public void onDraw(Canvas g){

        if (dState == null) {
            return;
        }
        updateDimensions(g);

        for (int i = 0; i < dState.getBOARDHEIGHT(); i++){
            for (int j = 0; j < dState.getBOARDWIDTH(); j++){
                if (dState.getDomino(i,j) == null){
                    continue;
                }
                Domino d = dState.getDomino(i,j);
                drawDomino(g,d,i,j);
            }
        }

        drawHighlights(g);

        mapPixelToSquare(100,100);
    }

    public void drawHighlights(Canvas g){


        Paint p = new Paint();
        p.setColor(dominoColor());
        int whoseTurn;
        PlayerInfo playerInfo;

        ArrayList<MoveInfo> playerLegalMoves= new ArrayList<MoveInfo>();

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
            currentLegalMove= playerLegalMoves.get(i);
            row=currentLegalMove.getRow();
            col=currentLegalMove.getCol();
            orientation= currentLegalMove.getOrientation();
            if(orientation==1 || orientation==3){
                g.drawBitmap(horizontalHighlight,col*163, row*163,p );

            }
            else if(orientation==2 || orientation==4){
                g.drawBitmap(verticalHighlight,col*163, row*163,p );
            }
        }
    }

    /**
     * update the instance variables that relate to the drawing surface
     *
     * @param g
     * 		an object that references the drawing surface
     */
    private void updateDimensions(Canvas g) {

        // initially, set the height and width to be that of the
        // drawing surface
        int width = g.getWidth();
        int height = g.getHeight();

        // Set the "full square" size to be the minimum of the height and
        // the width. Depending on which is greater, set either the
        // horizontal or vertical base to be partway across the screen,
        // so that the "playing square" is in the middle of the screen on
        // its long dimension
        if (width > height) {
            fullSquare = height;
            vBase = 0;
            hBase = (width - height) / (float) 11.0;
        } else {
            fullSquare = width;
            hBase = 0;
            vBase = (height - width) / (float) 5.0;
        }

        //Domino d= new Domino(3,5,1,3);
        //drawDomino(g,d,0,0);
    }

    public void drawDomino(Canvas g, Domino d, int row, int col){
        float xLoc = col*163;
        float yLoc = row*163;
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

        if(leftPipCount<=rightPipCount) {
            //the reason for +270 degrees is because domino png is vertical and we want to get it back
            //orientation 1 initially
            one.postRotate(0+270);
            two.postRotate(90+270);
            three.postRotate(180+270);
            four.postRotate(270+270);
            //string Id of domino based on left and right pip count
            dominoClipartId="domino"+leftPipCount+"_"+rightPipCount;

        }

        else{
            one.postRotate(0+270+180);
            two.postRotate(90+270+180);
            three.postRotate(180+270+180);
            four.postRotate(270+270+180);
            dominoClipartId="domino"+rightPipCount+"_"+leftPipCount;
        }

        //converts string id into an int id to link domino btmap image to corresponding resource file
        int integerDominoID = DSurfaceView.this.getResources().getIdentifier(dominoClipartId , "drawable", getContext().getPackageName());
        Bitmap dominoImage= BitmapFactory.decodeResource(getResources(),integerDominoID );
        Bitmap rotatedDominoImage=null;
        if(dominoOrientation==1) {
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), one, true);
        }
        else if(dominoOrientation==2){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), four, true);


        }
        else if(dominoOrientation==3){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), three, true);


        }

        else if(dominoOrientation==4){
            rotatedDominoImage = Bitmap.createBitmap(dominoImage, 0, 0, dominoImage.getWidth(), dominoImage.getHeight(), two, true);


        }
        g.drawBitmap(rotatedDominoImage,xLoc, yLoc,null);


    }

    /**
     * maps a point from the canvas' pixel coordinates to "square" coordinates
     *
     * @param x
     * 		the x pixel-coordinate
     * @param y
     * 		the y pixel-coordinate
     * @return
     *		a Point whose components are in the range 0-2, indicating the
     *		column and row of the corresponding square on the tic-tac-toe
     * 		board, or null if the point does not correspond to a square
     */
    public Point mapPixelToSquare(int x, int y) {

        Drawable d = getResources().getDrawable(R.drawable.domino0_0);
        int squareDim = 163;
        int c =  x/squareDim;
        int r = y/squareDim -1;
        Logger.log("i", "Row "+r+" Col"+c);
        return new Point(r,c) ;

    }
}
