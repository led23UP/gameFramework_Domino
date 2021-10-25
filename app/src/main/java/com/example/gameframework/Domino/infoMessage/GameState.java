package com.example.gameframework.Domino.infoMessage;

import java.util.ArrayList;
/**
 * GameState class for Dominoes.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class GameState {

    private int playerCount = 3;

    private final DominoSet dominoSet;
    private final Domino[][] board;
    private int leftEndX, leftEndY, rightEndX, rightEndY;
    private final ArrayList<Domino> boneyard;
    private ArrayList<Players> user = new ArrayList<>(playerCount);

    public GameState() {

        leftEndX = -1;
        leftEndY = -1;
        rightEndX = -1;
        rightEndY = -1;


        //create player objects equal to the amount of players playing.
        for(int i=0; i <playerCount;i++) {
            Players x = new Players(i,0);
            user.add(x);
        }

        // First domino will be placed at [2][5]!!
        board = new Domino[5][11];
        // Each player is dealt 5 dominoes. 28-playerCount*5 is the highest num of dominoes that
        // will ever be in the boneyard.
        boneyard = new ArrayList<>(28-playerCount*5);
        // Create a new set of dominoes and shuffle it!
        dominoSet = new DominoSet();

        // THIS IS NOT HOW DOMINOES WILL BE DEALT NORMALLY!!!!!
        // ASSIGNING THEM MANUALLY FOR TESTING!!!
        dealDominoesTest();
        int size = dominoSet.dominos.size();
        // Fill the boneyard with the leftover dominoes in set. Empty dominoSet after.
        for (int i = 0; i < size; i++){
            // Fill each piece of boneyard with remaining dominoes in set. Then remove from dominoSet.
            boneyard.add(dominoSet.dominos.get(0));
            dominoSet.dominos.remove(0);
        }

    }

    public GameState(GameState other) {
        this.playerCount = other.playerCount;

        this.leftEndX = other.leftEndX;
        this.leftEndY = other.leftEndY;
        this.rightEndX = other.rightEndX;
        this.rightEndY = other.rightEndY;

        this.user = new ArrayList<>(this.playerCount);

        for(int i=0; i <playerCount;i++) {
            this.user.add(new Players(other.user.get(i)));
        }

        this.board = new Domino[5][11];
        this.boneyard = new ArrayList<Domino>(other.boneyard.size());
        this.dominoSet = new DominoSet(other.dominoSet);

        for (int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                this.board[i][j] = other.board[i][j];
            }
        }
        int size = other.boneyard.size();
        for (int i = 0; i < size; i++){
            this.boneyard.add(new Domino(other.boneyard.get(i)));
        }
    }
    // This function deals a pre-determined hand to each player for testing purposes.
    public void dealDominoesTest(){
        user.get(0).getHand().add(dominoSet.dominos.get(6));
        dominoSet.dominos.remove(6);
        user.get(0).getHand().add(dominoSet.dominos.get(7-1));
        dominoSet.dominos.remove(7-1);
        user.get(0).getHand().add(dominoSet.dominos.get(8-2));
        dominoSet.dominos.remove(8-2);
        user.get(0).getHand().add(dominoSet.dominos.get(9-3));
        dominoSet.dominos.remove(9-3);
        user.get(0).getHand().add(dominoSet.dominos.get(11-4));
        dominoSet.dominos.remove(11-4);

        user.get(1).getHand().add(dominoSet.dominos.get(0));
        dominoSet.dominos.remove(0);
        user.get(1).getHand().add(dominoSet.dominos.get(0));
        dominoSet.dominos.remove(0);
        user.get(1).getHand().add(dominoSet.dominos.get(3-2));
        dominoSet.dominos.remove(3-2);
        user.get(1).getHand().add(dominoSet.dominos.get(4-3));
        dominoSet.dominos.remove(4-3);
        user.get(1).getHand().add(dominoSet.dominos.get(5-4));
        dominoSet.dominos.remove(5-4);

        user.get(2).getHand().add(dominoSet.dominos.get(21-10));
        dominoSet.dominos.remove(21-10);
        user.get(2).getHand().add(dominoSet.dominos.get(24-11));
        dominoSet.dominos.remove(24-11);
        user.get(2).getHand().add(dominoSet.dominos.get(25-12));
        dominoSet.dominos.remove(25-12);
        user.get(2).getHand().add(dominoSet.dominos.get(26-13));
        dominoSet.dominos.remove(26-13);
        user.get(2).getHand().add(dominoSet.dominos.get(27-14));
        dominoSet.dominos.remove(27-14);
    }

    public int[] firstMove() {
        int lowestMax = -1;
        int playerLowestMax = -1;
        int dominoHighestWeight = -1;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < 5; j++) {
                if (user.get(i).getHand().get(j).getWeight() > lowestMax) {
                    lowestMax = user.get(i).getHand().get(j).getWeight();
                    playerLowestMax = i;
                    dominoHighestWeight = j;
                }
            }
        }
        int[] goesFirstInfo = new int[2];
        goesFirstInfo[0] = playerLowestMax;
        goesFirstInfo[1] = dominoHighestWeight;
        return goesFirstInfo;
    }

    public void placeFirstPiece(int playerID, int dominoIndex){
        // Initialize the end variables at center of board to keep track for scoring purposes.
        leftEndX = 2;
        leftEndY = 5;
        rightEndX = 2;
        rightEndY = 5;
        // Place domino in center then remove from player's hand.
        board[2][5] = user.get(playerID).getHand().get(dominoIndex);
        user.get(playerID).getHand().remove(dominoIndex);
    }

    public boolean placePiece(int playerID, int dominoIndex, int x, int y){
        // If spot is not empty, inputs will go out of bounds, or you try to place a domino
        // that is not  in the users hand, return false.
        if(board[2+x][5+y] != null || 2 + x > board.length - 1 || 5 + y > board[0].length - 1
            || 2 - x < 0 || 5 - y < 0 || user.get(playerID).getHand().size() <= dominoIndex){
            return false;
        }

        Domino playedDomino = user.get(playerID).getHand().get(dominoIndex);
        // xC and yC are used to calculate the increment from the center of the board to place
        // the piece.
        int xC, yC;
        xC = Integer.compare(0, x);
        yC = Integer.compare(0, y);
        // If one is not zero, set other to zero so we don't count diagonal matches.
        if (yC != 0){
            xC = 0;
        }
        // The piece is being checked against an empty spot. Return false.
        if (board[2 + x + xC][5 + y + yC] == null){
            return false;
        }
        /*
         * Check if the leftPips of placed domino matches rightPips of domino already on board.
         * If there is a match, place the domino there.
         */
        if (playedDomino.getLeftPipCount() == board[2 + x + xC][5 + y + yC].getRightPipCount()){
            board[2+x][5+y] = playedDomino;
            // If we reach the leftmost end of board, make domino's orientation vertical up.
            if (5 + y == 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(2);
            }
            // If we reach the rightmost end of board, make domino's orientation vertical down.
            else if (5 + y == board[x].length - 1){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(4);
            }
            // If we are placing to left of center of board, rotate piece 180 degrees.
            else if (y < 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(3);
            }
            // Calculate if the move made will score points, then remove domino from player's hand.
            calculateScoredPoints(playerID,dominoIndex,y);
            user.get(playerID).getHand().remove(dominoIndex);
            // Change the value to keep track of where the end of the chain is to calculate points.
            if(y > 0){
                rightEndX = 2 + x;
                rightEndY = 5 + y;
            }
            else{
                leftEndX = 2 + x;
                leftEndY = 5 + y;
            }
            return true;
        }
        /*
         * Check if the rightPips of placed domino matches leftPips of domino already on board.
         * If there is a match, place the domino there.
         */
        if (playedDomino.getRightPipCount() == board[2 + x + xC][5 + y + yC].getLeftPipCount()){
            board[2+x][5+y] = playedDomino;
            // If we reach the leftmost end of board, make domino's orientation vertical up.
            if (5 + y == 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(2);
            }
            // If we reach the rightmost end of board, make domino's orientation vertical down.
            else if (5 + y == board[x].length -1){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(4);
            }
            // If domino is placed to right of center, rotate 180 degrees.
            else if (y > 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(3);
            }
            // Calculate if the move made will score points, then remove domino from player's hand.
            calculateScoredPoints(playerID,dominoIndex,y);
            user.get(playerID).getHand().remove(dominoIndex);
            // If we are to right of center, update rightEnd variables.
            if(y > 0){
                rightEndX = 2 + x;
                rightEndY = 5 + y;

            }
            // If we are to left of center, update leftEnd variables.
            else if (y < 0){
                leftEndX = 2 + x;
                leftEndY = 5 + y;
            }
            return true;
        }
        /*
         * Check if the leftPips of placed domino matches leftPips of domino already on board.
         * If there is a match, place the domino there.
         */
        if (playedDomino.getLeftPipCount() == board[2 + x + xC][5 + y + yC].getLeftPipCount()){
            board[2+x][5+y] = playedDomino;
            // If we reach the leftmost end of board, make domino's orientation vertical up.
            if (5 + y == 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(4);
            }
            // If we reach the rightmost end of board, make domino's orientation vertical down.
            else if (5 + y == board[x].length - 1){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(2);
            }
            // If left pips matches left pips, domino MUST be rotated 180 degrees.
            else{
                user.get(playerID).getHand().get(dominoIndex).setOrientation(3);
            }
            // Calculate if the move made will score points, then remove domino from player's hand.
            calculateScoredPoints(playerID,dominoIndex,y);
            user.get(playerID).getHand().remove(dominoIndex);
            if(y > 0){
                rightEndX = 2 + x;
                rightEndY = 5 + y;
            }
            else{
                leftEndX = 2 + x;
                leftEndY = 5 + y;
            }
            return true;
        }
        /*
         * Check if the rightPips of placed domino matches rightPips of domino already on board.
         * If there is a match, place the domino there.
         */
        if (playedDomino.getRightPipCount() == board[2 + x + xC][5 + y + yC].getRightPipCount()){
            board[2+x][5+y] = playedDomino;
            // If we reach the leftmost end of board, make domino's orientation vertical up.
            if (5 + y == 0){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(4);
            }
            // If we reach the rightmost end of board, make domino's orientation vertical down.
            else if (5 + y == board[x].length - 1){
                user.get(playerID).getHand().get(dominoIndex).setOrientation(2);
            }
            // If right pips matches right pips, domino MUST be rotated 180 degrees.
            else{
                user.get(playerID).getHand().get(dominoIndex).setOrientation(3);
            }
            // Calculate if the move made will score points, then remove domino from player's hand.
            calculateScoredPoints(playerID,dominoIndex,y);
            user.get(playerID).getHand().remove(dominoIndex);
            if(y > 0){
                rightEndX = 2 + x;
                rightEndY = 5 + y;
            }
            else{
                leftEndX = 2 + x;
                leftEndY = 5 + y;
            }
            return true;
         }
        // Inputting spot to place piece is not a valid move. Return false.
        return false;
    }

    public void calculateScoredPoints(int playerID,int dominoIndex, int y){
        // If to right of center of board, check placed domino's rightPips vs end of chain's
        // leftPips. If they are divisible by three, award player sum of pips.
        if (y > 0){
            int playedPips = user.get(playerID).getHand().get(dominoIndex).getRightPipCount();
            int endPips = board[leftEndX][leftEndY].getLeftPipCount();

            if ((playedPips + endPips) % 3 == 0){
                user.get(playerID).addPoints(playedPips + endPips);
            }
        }
        // If to right of center of board, check placed domino's ;eftPips vs end of chain's
        // rightPips. If they are divisible by three, award player sum of pips.
        else if (y < 0){
            int playedPips = user.get(playerID).getHand().get(dominoIndex).getLeftPipCount();
            int endPips = board[leftEndX][leftEndY].getRightPipCount();

            if ((playedPips + endPips) % 3 == 0){
                user.get(playerID).addPoints(playedPips + endPips);
            }
        }
    }

    public boolean drawPiece(int playerID){
        // If boneyard is empty, player cannot draw piece. Return false.
        if (boneyard.isEmpty()){
            return false;
        }

        user.get(playerID).getHand().add(boneyard.get(0));
        boneyard.remove(0);
        return true;
    }

    public boolean quitGame(int playerID){
        // Set the player who pressed "Quit" to -1 to indicate that they pressed Quit.
        user.get(playerID).setScore(-1);
        return true;
    }

    public boolean newGame(int playerID){
       // Set the player who pressed "New Game" to -2 to indicate that they have forfeited.
        user.get(playerID).setScore(-2);
        return true;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("\nPlayer Count: " + playerCount + "\n");
        s.append(dominoSet.toString());

        s.append("\n" + "Leftmost Domino Coords: " + leftEndX + " " + leftEndY);
        s.append("\n" + "Rightmost Domino Coords: " + rightEndX + " " + rightEndY);
        s.append("\n");

        s.append("\nBoard: \n");
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == null){
                    s.append("[      ]");
                }
                else {
                    s.append(board[i][j].pipsToString());
                    s.append("");
                }
                if (j == board[i].length -1){
                    s.append("\n");
                }
            }
        }

        s.append("\nBoneyard{");
        for (int i = 0; i < boneyard.size(); i++){
            s.append(boneyard.get(i).toString());
            s.append("   ,");
        }
        s.append("}\n\n");

        for (int i = 0; i < user.size(); i++){
            s.append(user.get(i).toString());
            s.append("\n");
        }

        return s.toString();
    }

}
