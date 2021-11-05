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
import android.util.AttributeSet;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.Domino.infoMessage.MoveInfo;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gameframework.game.GameFramework.utilities.Logger;

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

    public int pipColor(){
        return Color.BLACK;
    }

    public int highlightColor(){
        return Color.GREEN;
    }

    public void onDraw(Canvas g){

        if (dState == null) {
            return;
        }
        updateDimensions(g);

        Paint p = new Paint();
        p.setColor(dominoColor());

        for (int i = 0; i < dState.getBOARDHEIGHT(); i++){
            for (int j = 0; j < dState.getBOARDWIDTH(); j++){
                if (dState.getDomino(i,j) == null){
                    continue;
                }
                Domino d = dState.getDomino(i,j);
                drawDomino(g,d,i,j);
            }
        }

        for (MoveInfo m : dState.getPlayerInfo()[0].getLegalMoves()){
            drawHighlights(g,m.getRow(), m.getCol(),0);
        }

//Domino d= new Domino(3,4,1,2);
       // drawDomino(g,d,0,0);
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
        float xLoc = BORDER_PERCENT + row *SQUARE_DELTA_PERCENT;
        float yLoc = BORDER_PERCENT + col*SQUARE_DELTA_PERCENT;
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

    public void drawHighlights(Canvas g, int row, int col, int playerID){
        float xLoc = (float) getWidth()*col/11;
        float yLoc = (float) getHeight()*row/5;

        Paint p = new Paint();
        p.setColor(dominoColor());

        Bitmap highlight = BitmapFactory.decodeResource(getResources(),R.drawable.domino_highlight);

        g.drawBitmap(highlight,xLoc, yLoc, p);
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

        int c =  getWidth()/x;
        int r =  getHeight()/y;
        Logger.log("info","Row: " + r + " Col: " + c);
        return new Point(r,c);


        // loop through each square and see if we get a "hit"; if so, return
        // the corresponding Point in "square" coordinates
        /*for (int i = 0; i < dState.getBOARDHEIGHT(); i++) {
            for (int j = 0; j < dState.getBOARDWIDTH() ; j++) {
                float left = h(BORDER_PERCENT + (i * SQUARE_DELTA_PERCENT));
                float right = h(BORDER_PERCENT + SQUARE_SIZE_PERCENT
                        + (i * SQUARE_DELTA_PERCENT));
                float top = v(BORDER_PERCENT + (j * SQUARE_DELTA_PERCENT));
                float bottom = v(BORDER_PERCENT + SQUARE_SIZE_PERCENT
                        + (j * SQUARE_DELTA_PERCENT));
                System.out.println(left + " " + right + " " + top + " " + bottom);
                if ((x > left) != (x > right) && (y > top) != (y > bottom)) {
                    return new Point(i, j);
                }
            }
        }*/

        // no match: return null
        //return null;
    }
    /**
     * helper-method to convert from a percentage to a horizontal pixel location
     *
     * @param percent
     * 		the percentage across the drawing square
     * @return
     * 		the pixel location that corresponds to that percentage
     */
    private float h(float percent) {
        return hBase + percent * fullSquare / 100;
    }

    /**
     * helper-method to convert from a percentage to a vertical pixel location
     *
     * @param percent
     * 		the percentage down the drawing square
     * @return
     * 		the pixel location that corresponds to that percentage
     */
    private float v(float percent) {
        return vBase + percent * fullSquare / 100;
    }
}
