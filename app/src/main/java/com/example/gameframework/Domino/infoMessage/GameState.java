package com.example.gameframework.Domino.infoMessage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GameState class for Dominoes.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class GameState {

    private int playerCount = 3;

    private int BOARDHEIGHT = 5;
    private int BOARDWIDTH = 11;
    private DominoSet dominoSet;
    private Domino[][] board;
    private int[] chainEnds;
    private boolean doesBoardHaveSpinner;
    private ArrayList<Domino> boneyard;
    private ArrayList<Player> players = new ArrayList<>(playerCount);

    public GameState() {
        chainEnds = new int[8];
        Arrays.fill(chainEnds, -1);
        doesBoardHaveSpinner = false;

        //create player objects equal to the amount of players playing.
        for(int i=0; i <playerCount;i++) {
            Player x = new Player(i,0);
            players.add(x);
        }

        // First domino will be placed at [2][5]!!
        board = new Domino[BOARDHEIGHT][BOARDWIDTH];
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

        this.BOARDWIDTH = other.BOARDWIDTH;
        this.BOARDHEIGHT = other.BOARDHEIGHT;

        this.doesBoardHaveSpinner = other.doesBoardHaveSpinner;

        chainEnds = new int[8];
        System.arraycopy(other.chainEnds,0,this.chainEnds,0,other.chainEnds.length);

        this.players = new ArrayList<>(this.playerCount);

        for(int i=0; i <playerCount;i++) {
            this.players.add(new Player(other.players.get(i)));
        }

        this.board = new Domino[BOARDHEIGHT][BOARDWIDTH];
        this.boneyard = new ArrayList<>(other.boneyard.size());
        this.dominoSet = new DominoSet(other.dominoSet);

        for (int i = 0; i < board.length; i++){
            System.arraycopy(other.board[i], 0, this.board[i], 0, board[i].length);
        }
        int size = other.boneyard.size();
        for (int i = 0; i < size; i++){
            this.boneyard.add(new Domino(other.boneyard.get(i)));
        }
    }
    // This function deals a pre-determined hand to each player for testing purposes.
    public void dealDominoesTest(){
        players.get(0).getHand().add(dominoSet.dominos.get(6));
        dominoSet.dominos.remove(6);
        players.get(0).getHand().add(dominoSet.dominos.get(7-1));
        dominoSet.dominos.remove(7-1);
        players.get(0).getHand().add(dominoSet.dominos.get(8-2));
        dominoSet.dominos.remove(8-2);
        players.get(0).getHand().add(dominoSet.dominos.get(9-3));
        dominoSet.dominos.remove(9-3);
        players.get(0).getHand().add(dominoSet.dominos.get(11-4));
        dominoSet.dominos.remove(11-4);

        players.get(1).getHand().add(dominoSet.dominos.get(0));
        dominoSet.dominos.remove(0);
        players.get(1).getHand().add(dominoSet.dominos.get(0));
        dominoSet.dominos.remove(0);
        players.get(1).getHand().add(dominoSet.dominos.get(3-2));
        dominoSet.dominos.remove(3-2);
        players.get(1).getHand().add(dominoSet.dominos.get(4-3));
        dominoSet.dominos.remove(4-3);
        players.get(1).getHand().add(dominoSet.dominos.get(5-4));
        dominoSet.dominos.remove(5-4);

        players.get(2).getHand().add(dominoSet.dominos.get(21-10));
        dominoSet.dominos.remove(21-10);
        players.get(2).getHand().add(dominoSet.dominos.get(24-11));
        dominoSet.dominos.remove(24-11);
        players.get(2).getHand().add(dominoSet.dominos.get(25-12));
        dominoSet.dominos.remove(25-12);
        players.get(2).getHand().add(dominoSet.dominos.get(26-13));
        dominoSet.dominos.remove(26-13);
        players.get(2).getHand().add(dominoSet.dominos.get(27-14));
        dominoSet.dominos.remove(27-14);
    }

    /**
     *
     * @return An array of ints containing the first move info. index 0 is the playerId. index 1 is dominoIndex,
     */
    public int[] firstMove() {
        int lowestMax = -1;
        int playerLowestMax = -1;
        int dominoHighestWeight = -1;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < 5; j++) {
                if (players.get(i).getHand().get(j).getWeight() > lowestMax) {
                    lowestMax = players.get(i).getHand().get(j).getWeight();
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

    /**
     *
     * @param playerID  ID of player calling function.
     * @param dominoIndex Position of domino in player's hand.
     */
    public void placeFirstPiece(int playerID, int dominoIndex){
        // Initialize the end variables at center of board to keep track for scoring purposes.
        Domino playedDomino = players.get(playerID).getHand().get(dominoIndex);

        if (playedDomino.getLeftPipCount() == playedDomino.getRightPipCount()){
            playedDomino.setSpinner();
            //chainEnds[4] is spinnerTopEndX.
            chainEnds[4] = BOARDHEIGHT/2;
            //chainEnds[5] is spinnerTopEndY;
            chainEnds[5] = BOARDWIDTH/2;
            //chainEnds[6] is spinnerBottomEndX.
            chainEnds[6]= BOARDHEIGHT/2;
            //chainEnds[7] is spinnerBottomEndY.
            chainEnds[7] = BOARDWIDTH/2;
        }
        //chainEnds[0] is leftEndX.
        chainEnds[0] = BOARDHEIGHT/2;
        //chainEnds[1] is leftEndY.
        chainEnds[1] = BOARDWIDTH/2;
        //chainEnds[2] is rightEndX.
        chainEnds[2] = BOARDHEIGHT/2;
        //chainEnds[3] is rightEndY.
        chainEnds[3] = BOARDWIDTH/2;

        // Place domino in center then remove from player's hand.
        board[BOARDHEIGHT/2][BOARDWIDTH/2] = players.get(playerID).getHand().get(dominoIndex);
        calculateScoredPoints(playerID);
        players.get(playerID).getHand().remove(dominoIndex);
    }
    //TODO Instead of returning true and placing the piece, add the coordinates to player's legalMoves array!
    public boolean findLegalMoves(int playerID, int dominoIndex, int x, int y){
        // If spot is not empty, inputs will go out of bounds, or you try to place a domino
        // that is not  in the users hand, return false.
        if( x >= BOARDHEIGHT || y >= BOARDWIDTH || x < 0 || y < 0 || board[x][y] != null
                || players.get(playerID).getHand().size() <= dominoIndex){
            return false;
        }

        Domino playedDomino = players.get(playerID).getHand().get(dominoIndex);

        if(!doesBoardHaveSpinner && playedDomino.getLeftPipCount() == playedDomino.getRightPipCount()){
            doesBoardHaveSpinner = true;
        }
        // This arrayList contains places where all the prevDominoes have been placed.
        //ArrayList<Domino> prevDominos = new ArrayList<>();
        ArrayList<Integer> moveCoords = new ArrayList<>();

        if (chainEnds[0] != -1){
            moveCoords.add(chainEnds[0]);
            moveCoords.add(chainEnds[1]);
        }
        if (chainEnds[2] != -1){
            moveCoords.add(chainEnds[2]);
            moveCoords.add(chainEnds[3]);
        }
        if (chainEnds[4] != -1){
            moveCoords.add(chainEnds[4]);
            moveCoords.add(chainEnds[5]);
        }
        if (chainEnds[6] != -1){
            moveCoords.add(chainEnds[6]);
            moveCoords.add(chainEnds[7]);
        }

        for (int i = 0; i < moveCoords.size(); i+= 2) {
            int r = moveCoords.get(i);
            int c = moveCoords.get(i+1);
            Domino prevDomino = board[r][c];
            // If prevDomino is NOT adjacent to board[x][y], skip.
            if (!((x - 1 == r && y == c) || (x + 1 == r && y == c) || (x == r && y - 1 == c) || (x == r && y + 1 == c))){
                continue;
            }
            ArrayList<MoveInfo> playerLegalMoves = players.get(playerID).getLegalMoves();

            /*
             * Check if the leftPips of placed domino matches rightPips of domino already on board.
             * If there is a match, place the domino there.
             */
            if (playedDomino.getLeftPipCount() == prevDomino.getRightPipCount()) {
                //board[x][y] = playedDomino;
                // If we reach the leftmost end of board, make domino's orientation vertical down.
                if (0 == y) {
                    playerLegalMoves.add(new MoveInfo(x,y,2));
                   // playedDomino.setOrientation(2);
                }
                // If we reach the rightmost end of board, make domino's orientation vertical up.
                else if (y == BOARDWIDTH - 1) {
                    playerLegalMoves.add(new MoveInfo(x,y,4));
                   // playedDomino.setOrientation(4);
                }
                // If we are placing to left of center of board, rotate piece 180 degrees.
                else if (y < BOARDWIDTH/2) {
                    playerLegalMoves.add(new MoveInfo(x,y,3));
                    //playedDomino.setOrientation(3);
                }

                if (x == 0 && y <= BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(3);
                    //playedDomino.setOrientation(3);
                } else if (x == 0 && y > BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(3);
                    //playedDomino.setOrientation(1);
                }
                setDominoChain(x,y,playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                /*setDominoChain(x,y,playedDomino,prevDomino);
                setInvalidSpots(x, y, playedDomino);
                setChainEnd(playedDomino.getChain(), x, y);
                // Calculate if the move made will score points, then remove domino from player's hand.
                calculateScoredPoints(playerID);
                players.get(playerID).getHand().remove(dominoIndex);*/
                return true;
            }
            /*
             * Check if the rightPips of placed domino matches leftPips of domino already on board.
             * If there is a match, place the domino there.
             */
            if (playedDomino.getRightPipCount() == prevDomino.getLeftPipCount()) {
                //board[x][y] = playedDomino;
                // If we reach the leftmost end of board, make domino's orientation vertical down.
                if (y == 0) {
                    playerLegalMoves.add(new MoveInfo(x,y,2));
                    //playedDomino.setOrientation(2);
                }
                // If we reach the rightmost end of board, make domino's orientation vertical up.
                else if (y == BOARDWIDTH - 1) {
                    playerLegalMoves.add(new MoveInfo(x,y,4));
                    //playedDomino.setOrientation(4);
                }
                // If domino is placed to right of center, rotate 180 degrees.
                else if (y > BOARDWIDTH/2) {
                    playerLegalMoves.add(new MoveInfo(x,y,3));
                    //playedDomino.setOrientation(3);
                }

                if (x == 0 && y <= BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(3);
                    //playedDomino.setOrientation(3);
                } else if (x == 0 && y > BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(1);
                    //playedDomino.setOrientation(1);
                }
                setDominoChain(x,y,playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                /*setDominoChain(x,y,playedDomino,prevDomino);
                setInvalidSpots(x, y, playedDomino);
                setChainEnd(playedDomino.getChain(), x, y);
                // Calculate if the move made will score points, then remove domino from player's hand.
                calculateScoredPoints(playerID);
                players.get(playerID).getHand().remove(dominoIndex);*/
                return true;
            }
            /*
             * Check if the leftPips of placed domino matches leftPips of domino already on board.
             * If there is a match, place the domino there.
             */
            if (playedDomino.getLeftPipCount() == prevDomino.getLeftPipCount()) {
                //board[x][y] = playedDomino;
                // If we reach the leftmost end of board, make domino's orientation vertical up.
                if (y == 0) {
                    playerLegalMoves.add(new MoveInfo(x,y,4));
                    //playedDomino.setOrientation(4);
                }
                // If we reach the rightmost end of board, make domino's orientation vertical down.
                else if (y == BOARDWIDTH - 1) {
                    playerLegalMoves.add(new MoveInfo(x,y,2));
                    //playedDomino.setOrientation(2);
                }
                // If left pips matches left pips, domino MUST be rotated 180 degrees.
                else {
                    playerLegalMoves.add(new MoveInfo(x,y,3));
                    //playedDomino.setOrientation(3);
                }

                if (x == 0 && y <= BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(1);
                    //playedDomino.setOrientation(1);
                } else if (x == 0 && y > BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(3);
                    //playedDomino.setOrientation(3);
                }
                setDominoChain(x,y,playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                /*setDominoChain(x,y,playedDomino,prevDomino);
                setInvalidSpots(x, y, playedDomino);
                setChainEnd(playedDomino.getChain(), x, y);
                // Calculate if the move made will score points, then remove domino from player's hand.
                calculateScoredPoints(playerID);
                players.get(playerID).getHand().remove(dominoIndex);*/
                return true;
            }
            /*
             * Check if the rightPips of placed domino matches rightPips of domino already on board.
             * If there is a match, place the domino there.
             */
            if (playedDomino.getRightPipCount() == prevDomino.getRightPipCount()) {
                //board[x][y] = playedDomino;
                // If we reach the leftmost end of board, make domino's orientation vertical up.
                if (y == 0) {
                    playerLegalMoves.add(new MoveInfo(x,y,4));
                    //playedDomino.setOrientation(4);
                }
                // If we reach the rightmost end of board, make domino's orientation vertical down.
                else if (y == BOARDWIDTH - 1) {
                    playerLegalMoves.add(new MoveInfo(x,y,2));
                    //playedDomino.setOrientation(2);
                }
                // If right pips matches right pips, domino MUST be rotated 180 degrees.
                else {
                    playerLegalMoves.add(new MoveInfo(x,y,3));
                   // playedDomino.setOrientation(3);
                }

                if (x == 0 && y <= BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(1);
                    //playedDomino.setOrientation(1);
                } else if (x == 0 && y > BOARDWIDTH/2) {
                    playerLegalMoves.get(playerLegalMoves.size() -1).setOrientation(3);
                    //playedDomino.setOrientation(3);
                }
                setDominoChain(x,y,playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
               /* setDominoChain(x,y,playedDomino,prevDomino);
                setInvalidSpots(x, y, playedDomino);
                setChainEnd(playedDomino.getChain(), x, y);
                // Calculate if the move made will score points, then remove domino from player's hand.
                calculateScoredPoints(playerID);
                players.get(playerID).getHand().remove(dominoIndex);*/
                return true;
            }
        }
        // Inputting spot to place piece is not a valid move. Return false.
        return false;
    }

    public boolean placePiece(int x, int y, int playerID, int dominoIndex){
        int orientation = -1;
        MoveInfo move = null;
        for (int i = 0; i < players.get(playerID).getLegalMoves().size(); i++){
            move = players.get(playerID).getLegalMoves().get(i);
            if (x == move.getRow() && y == move.getCol()) {
                orientation = move.getOrientation();
                break;
            }
        }
        // If no matching legal move was found, return false.
        if (orientation == -1){
            return false;
        }

        Domino playedDomino = players.get(playerID).getHand().get(dominoIndex);
        playedDomino.setOrientation(orientation);
        playedDomino.setChain(move.getChain());
        board[x][y] = playedDomino;

        setInvalidSpots(x,y,playedDomino);
        setChainEnd(playedDomino.getChain(),x,y);
        calculateScoredPoints(playerID);

        players.get(playerID).getHand().remove(dominoIndex);
        return true;
    }

    public void setDominoChain(int x, int y, MoveInfo move, Domino prevD){
        if (prevD.getChain() == ' ') {
            if (y == BOARDWIDTH/2 + 1) {
                move.setChain('R');
            } else if (y == BOARDWIDTH/2 - 1) {
                move.setChain('L');
            }
        } else {
            move.setChain(prevD.getChain());
        }

        if (x == BOARDHEIGHT/2 + 1 && prevD.isSpinner()) {
            move.setChain('D');
            move.setOrientation(2);
        } else if (x == BOARDHEIGHT/2 - 1 && prevD.isSpinner()) {
            move.setChain('U');
            move.setOrientation(4);
        }
    }

    public void setChainEnd(char c, int x, int y){
        switch (c){
            case 'U':
                chainEnds[4] = x;
                chainEnds[5] = y;
                break;
            case 'D':
                chainEnds[6] = x;
                chainEnds[7] = y;
                break;
            case 'L':
                chainEnds[0] = x;
                chainEnds[1] = y;
                break;
            case 'R':
                chainEnds[2] = x;
                chainEnds[3] = y;
                break;
        }
    }

    public void setInvalidSpots(int x, int y, Domino playedDomino){
        if (!playedDomino.isSpinner()){
            switch (playedDomino.getOrientation()){
                case 1:
                case 3:
                    // Emergency bounds checking just to make sure we don't attempt to access outside board.
                    if (x > 0) {
                        if (board[x - 1][y] == null) {
                            board[x - 1][y] = new Domino(-1, -1, -1, -1);
                        }
                    }
                    if (x < BOARDHEIGHT - 1) {
                        if (board[x + 1][y] == null) {
                            board[x + 1][y] = new Domino(-1, -1, -1, -1);
                        }
                    }
                    break;
                case 2:
                case 4:
                    // Emergency bounds checking just to make sure we don't attempt to access outside board.
                    if (y > 0) {
                        if (board[x][y - 1] == null) {
                            board[x][y - 1] = new Domino(-1, -1, -1, -1);
                        }
                    }
                    if (y < BOARDWIDTH - 1) {
                        if (board[x][y + 1] == null) {
                            board[x][y + 1] = new Domino(-1, -1, -1, -1);
                        }
                    }
                    break;
            }
        }
    }

    public void calculateScoredPoints(int playerID) {
        // If to right of center of board, check placed domino's rightPips vs end of chain's
        // leftPips. If they are divisible by three, award player sum of pips.

        int leftPips = board[chainEnds[0]][chainEnds[1]].getLeftPipCount();
        int rightPips = board[chainEnds[2]][chainEnds[3]].getRightPipCount();
        // If left or right end are center of board, they are not counted.
        if (chainEnds[0] == BOARDHEIGHT/2 && chainEnds[1] == BOARDWIDTH/2){
            leftPips = 0;
        }
        if (chainEnds[2] == BOARDHEIGHT/2 && chainEnds[3] == BOARDWIDTH/2){
            rightPips = 0;
        }

        int bottomPips = board[chainEnds[6]][chainEnds[7]].getRightPipCount();
        int topPips = board[chainEnds[4]][chainEnds[5]].getLeftPipCount();

        // If the the sum of pips is a multiple of 3, award user that amount of points.
        if ((leftPips + rightPips + bottomPips + topPips) % 3 == 0){
            players.get(playerID).addPoints(leftPips + rightPips + bottomPips + topPips);
        }
    }

    public boolean drawPiece(int playerID){
        // If boneyard is empty, player cannot draw piece. Return false.
        if (boneyard.isEmpty()){
            return false;
        }

        players.get(playerID).getHand().add(boneyard.get(0));
        boneyard.remove(0);
        return true;
    }

    public boolean quitGame(int playerID){
        // Set the player who pressed "Quit" to -1 to indicate that they pressed Quit.
        players.get(playerID).setScore(-1);
        return true;
    }

    public boolean newGame(int playerID){
        // Set the player who pressed "New Game" to -2 to indicate that they have forfeited.
        players.get(playerID).setScore(-2);
        return true;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("\nPlayer Count: " + playerCount + "\n");
        s.append(dominoSet.toString());

        s.append("\n" + "Leftmost Domino Coords: " + chainEnds[0] + " " + chainEnds[1]);
        s.append("\n" + "Rightmost Domino Coords: " + chainEnds[2] + " " + chainEnds[3]);
        s.append("\n" + "Top Spinner Domino Coords: " + chainEnds[4] + " " + chainEnds[5]);
        s.append("\n" + "Bottom Spinner Domino Coords: " + chainEnds[6] + " " + chainEnds[7]);
        s.append("\n");

        s.append("\nBoard: \n");
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == null){
                    s.append("[        ]");
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

        for (int i = 0; i < players.size(); i++){
            s.append(players.get(i).toString());
            s.append("\n");
        }

        return s.toString();
    }
}
