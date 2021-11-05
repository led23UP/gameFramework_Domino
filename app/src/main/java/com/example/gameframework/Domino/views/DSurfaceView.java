package com.example.gameframework.Domino.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.gameframework.Domino.infoMessage.Domino;
import com.example.gameframework.Domino.infoMessage.DominoGameState;
import com.example.gameframework.R;
import com.example.gameframework.game.GameFramework.utilities.FlashSurfaceView;

public class DSurfaceView extends FlashSurfaceView {
    private static final String TAG = "DSurfaceView";

    private final static float BORDER_PERCENT = 2;
    private final static float SQUARE_SIZE_PERCENT = 9;
    //private final static float LINE_WIDTH_PERCENT = 3;
    private final static float SQUARE_DELTA_PERCENT = SQUARE_SIZE_PERCENT; //+ LINE_WIDTH_PERCENT;

    protected DominoGameState dState;
    protected float hBase;
    protected float vBase;

    protected float fullSquare;


    public DSurfaceView(Context context) {
        super(context);
        init();
    }

    public DSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
                Domino d = dState.getDomino(i,j);
                drawDomino(g,d,i,j);
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
            hBase = (width - height) / (float) 2.0;
        } else {
            fullSquare = width;
            hBase = 0;
            vBase = (height - width) / (float) 2.0;
        }

    }

    public void drawDomino(Canvas g, Domino d, int row, int col){
        float xLoc = BORDER_PERCENT + row *SQUARE_DELTA_PERCENT;
        float yLoc = BORDER_PERCENT + col*SQUARE_DELTA_PERCENT;
        // If domino is invalid, DO NOT DRAW.
        if (d.getLeftPipCount() == -1){
            return;
        }
        Paint p = new Paint();
        p.setColor(dominoColor());
        RectF dominoRect = new RectF(xLoc, yLoc, xLoc + 50, yLoc +50);
        p.setColor(pipColor());
        g.drawRect(dominoRect,p);
        switch (d.getLeftPipCount()){
            case 1:
                g.drawCircle(dominoRect.centerX()/4, dominoRect.centerY(), 10,p);
                break;
            case 2:
                // Top left pip.
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY()/2, 10,p);
                // Bottom right pip.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/4,
                        dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                break;
            case 3:
                // Top left pip.
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY()/2, 10,p);
                // Center pip.
                g.drawCircle(dominoRect.centerX()/4, dominoRect.centerY(), 10,p);
                // Bottom right pip.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                break;
            case 4:
                // Top left.
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY()/2, 10,p);
                // Bottom right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                // Top right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY()/2, 10,p);
                // Bottom left.
                g.drawCircle(dominoRect.centerX()/12,dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                break;
            case 5:
                // Center pip.
                g.drawCircle(dominoRect.centerX()/4, dominoRect.centerY(), 10,p);
                // Top left.
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY()/2, 10,p);
                // Bottom right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                // Top right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY()/2, 10,p);
                // Bottom left.
                g.drawCircle(dominoRect.centerX()/12,dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                break;
            case 6:
                // Top left.
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY()/2, 10,p);
                // Bottom right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                // Top right.
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12,
                        dominoRect.centerY()/2, 10,p);
                // Bottom left.
                g.drawCircle(dominoRect.centerX()/12,dominoRect.centerY() + 1*dominoRect.centerY()/3, 10,p);
                // Middle left
                g.drawCircle(dominoRect.centerX()/12, dominoRect.centerY(), 10,p);
                // Middle right
                g.drawCircle(dominoRect.centerX() - dominoRect.centerX()/12, dominoRect.centerY(), 10,p);
                break;
            default:
        }
        //TODO Finish this.
        switch (d.getRightPipCount()){
            case 1:
                g.drawCircle(3*dominoRect.centerX()/4, dominoRect.centerY(), 10,p);
                break;
            case 2:
                break;
        }

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

        // loop through each square and see if we get a "hit"; if so, return
        // the corresponding Point in "square" coordinates
        for (int i = 0; i < dState.getBOARDHEIGHT(); i++) {
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
        }

        // no match: return null
        return null;
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
