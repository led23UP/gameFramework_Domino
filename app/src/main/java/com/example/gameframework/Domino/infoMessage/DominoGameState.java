package com.example.gameframework.Domino.infoMessage;

import com.example.gameframework.game.GameFramework.infoMessage.GameState;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * DominoGameState class for Dominoes.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoGameState extends GameState {

    private int playerCount = 4;
    private int BOARDHEIGHT = 5;
    private int BOARDWIDTH = 11;
    private DominoSet dominoSet;
    private Domino[][] board;
    private int[] chainEnds;
    private boolean doesBoardHaveSpinner;
    private ArrayList<Domino> boneyard;
    //private ArrayList<ArrayList<Domino>> board;
    private PlayerInfo[] players;
    private int turnID;
    private String message;
    private String boneyardMsg;
    public boolean boardEmpty;


    public DominoGameState() {

        message = "";
        boneyardMsg="";
        chainEnds = new int[8];
        Arrays.fill(chainEnds, -1);
        doesBoardHaveSpinner = false;

        board = new Domino[BOARDHEIGHT][BOARDWIDTH];

        boneyard = new ArrayList<>();
        // Start the first round.
        //startRound();
    }

    public DominoGameState(DominoGameState other) {

        message = other.message;
        boneyardMsg = other.boneyardMsg;
        this.playerCount = other.playerCount;

        this.BOARDWIDTH = other.BOARDWIDTH;
        this.BOARDHEIGHT = other.BOARDHEIGHT;

        this.doesBoardHaveSpinner = other.doesBoardHaveSpinner;

        chainEnds = new int[8];
        System.arraycopy(other.chainEnds,0,this.chainEnds,0,other.chainEnds.length);

        this.players = new PlayerInfo[other.players.length];

        if (playerCount >= 0) System.arraycopy(other.players, 0, this.players, 0, playerCount);

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
        this.turnID = other.turnID;

        this.message = other.message;
        this.boardEmpty = other.boardEmpty;
    }

    public void setNumPlayersStart(int players){
        playerCount = players;
        startRound(true);
    }

    /**
     *  firstMove determines who moves first, and what domino they will place.
     * @return An array of ints containing the first move info. index 0 is the playerId. index 1 is dominoIndex,
     */
    public int[] firstMove() {
        int lowestMax = -1;
        int playerLowestMax = -1;
        int dominoHighestWeight = -1;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < 5; j++) {
                if (players[i].getHand().get(j).getWeight() > lowestMax) {
                    lowestMax = players[i].getHand().get(j).getWeight();
                    playerLowestMax = i;
                    dominoHighestWeight = j;
                }
            }
        }
        turnID = playerLowestMax;
        int[] goesFirstInfo = new int[2];
        goesFirstInfo[0] = playerLowestMax;
        goesFirstInfo[1] = dominoHighestWeight;
        return goesFirstInfo;
    }

    /**
     *  placeFirstPiece places first piece in center of board.
     * @param playerID  ID of player calling function.
     * @param dominoIndex Position of domino in player's hand.
     */
    public void placeFirstPiece(int playerID, int dominoIndex){

        Domino playedDomino = players[playerID].getHand().get(dominoIndex);
        // Initialize the end variables at center of board to keep track for scoring purposes.
        // If the first domino is a spinner, spinnerChainEnds.
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
            doesBoardHaveSpinner = true;
        }
        //chainEnds[0] is leftEndX.
        chainEnds[0] = BOARDHEIGHT/2;
        //chainEnds[1] is leftEndY.
        chainEnds[1] = BOARDWIDTH/2;
        //chainEnds[2] is rightEndX.
        chainEnds[2] = BOARDHEIGHT/2;
        //chainEnds[3] is rightEndY.
        chainEnds[3] = BOARDWIDTH/2;

        // Place the domino in center of board.
        board[BOARDHEIGHT/2][BOARDWIDTH/2] = players[playerID].getHand().get(dominoIndex);
        // Calculate if player scored any points.
        calculateScoredPoints(playerID);
        // Mark now invalid spaces on board.
        setInvalidSpots(BOARDHEIGHT/2,BOARDWIDTH/2,playedDomino);
        // Remove domino from players's hand.
        players[playerID].getHand().remove(dominoIndex);
        // Clear legal moves, then re-calculate.
        players[playerID].getLegalMoves().clear();
        findLegalMoves(playerID);
    }

    /**
     * findLegalMoves finds all of a player's legal moves and stores it in the player's
     * legalMoves arrayList.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMoves(int playerID){
        ArrayList<Integer> moveCoords = new ArrayList<>();
        // Add each chain end into moveCoords so we can check them for matches.
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
        int index = 0;
        // Loop through the entire player's hand to find all of their legal moves.
        // Add each legal move to their legalMoves array.
        for(Domino playedDomino : players[playerID].getHand()) {
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[x].length; y++) {
                    for (int i = 0; i < moveCoords.size(); i += 2) {
                        int r = moveCoords.get(i);
                        int c = moveCoords.get(i + 1);
                        Domino prevDomino = board[r][c];
                        // If prevDomino is NOT adjacent to board[x][y], skip.
                        if (!((x - 1 == r && y == c) || (x + 1 == r && y == c) || (x == r && y - 1 == c) || (x == r && y + 1 == c))) {
                            continue;
                        }
                        ArrayList<MoveInfo> playerLegalMoves = players[playerID].getLegalMoves();
                        // Check if the leftPips of placed domino matches rightPips of domino already on board.
                        // For this match, we CANNOT be on left side of board.
                        if (playedDomino.getLeftPipCount() == prevDomino.getRightPipCount()
                            && !(y < BOARDWIDTH/2)) {
                            // If the spot is empty, add new legal move, otherwise skip.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            // If we are at the leftMost end of board, orientation = 4.
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If we reach the rightmost end of board, orientation = 2.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we are placing to left of center of board, rotate piece 180 degrees.
                            else if (y < BOARDWIDTH/2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            /*else if (x < BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            else if (x > BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }*/
                            // If we are at top of board and at middle/left, make orientation 3.
                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            // If we are at top of board and right, make orientation 1.
                            else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        //Check if the rightPips of placed domino matches leftPips of domino already on board.
                        // For this match, we CANNOT be on right side of board.
                        if (playedDomino.getRightPipCount() == prevDomino.getLeftPipCount()
                            && ! (y > BOARDWIDTH/2)) {
                            // If the spot is empty, add new legal move, otherwise skip.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            // If we are at leftmost end of board, make orientation - 4.
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If we reach the rightmost end of board, make domino's orientation 2.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If domino is placed to right of center, rotate 180 degrees.
                            else if (y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            /*else if (x < BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            else if (x > BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }*/
                            // If we are at top of board and at middle/left, make orientation 1.
                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            }
                            // If we are at top of board and at right, make orientation 3.
                            else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        // Check if the leftPips of placed domino matches leftPips of domino already on board.
                        // For this match, we CANNOT be on right side of board.
                        if (playedDomino.getLeftPipCount() == prevDomino.getLeftPipCount()
                            && !(y > BOARDWIDTH/2)) {
                            // If the spot is empty, add new legal move, otherwise skip.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 3, index));
                            }
                            else{
                                continue;
                            }
                            // If we are at leftmost end of board, make orientation 2.
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we reach the rightmost end of board, make domino's orientation 4.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            /*else if (x < BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            else if (x > BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }*/
                            // If we are at top of board and at middle/left, make orientation 3.
                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            // If we are at top of board and at right, make orientation 1.
                            else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        // Check if the rightPips of placed domino matches rightPips of domino already on board.
                        // For this match, we CANNOT be on right side of board.
                        if (playedDomino.getRightPipCount() == prevDomino.getRightPipCount()
                            && !(y < BOARDWIDTH/2)) {
                            // If the spot is empty, add new legal move, otherwise skip.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 3, index));
                            }
                            else{
                                continue;
                            }
                            //If we are at leftmost end of board, make orientation 2.
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we reach the rightmost end of board, make domino's orientation 4.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            /*else if (x < BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size()-1).setOrientation(2);
                            }
                            else if (x > BOARDHEIGHT/2 && y == BOARDWIDTH/2){
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }*/
                            // If we are at top of board and at middle/left, make orientation 1.
                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            }
                            // If we are at top of board and at right, make orientation 3.
                            else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                        }
                    }
                }
            }
            // Increment dominoIndex at end of checks for one domino.
            index++;
        }
    }

    /**
     * placePiece places a domino on the board.
     * @param x row the piece will be placed in.
     * @param y col the piece will be placed in.
     * @param playerID the player placing a piece.
     * @param dominoIndex the position of the domino in player's hand.
     * @return Whether placing piece was succesful.
     */
    public boolean placePiece(int x, int y, int playerID, int dominoIndex){
        // If spot you are trying to place is empty, return false.
        if (board[x][y] != null){
            return false;
        }
        // Use this to keep track if the player is attempting to place from a legal move.
        int orientation = -1;
        MoveInfo move = null;
        for (int i = 0; i < players[playerID].getLegalMoves().size(); i++){
            move = players[playerID].getLegalMoves().get(i);
            if (x == move.getRow() && y == move.getCol()) {
                orientation = move.getOrientation();
                break;
            }
        }

        Domino playedDomino = players[playerID].getHand().get(dominoIndex);
        if (!doesBoardHaveSpinner &&
                playedDomino.getLeftPipCount() == playedDomino.getRightPipCount()){
            doesBoardHaveSpinner = true;
        }

        // If no matching legal move was found, return false.
        if (orientation == -1){
            return false;
        }

        // Set the played domino's orientation to match the legal move's orientation.
        playedDomino.setOrientation(orientation);
        // Set the chain to match the move's chain.
        playedDomino.setChain(move.getChain());

        board[x][y] = playedDomino;
        // Set invalid spots from new move.
        setInvalidSpots(x,y,playedDomino);
        // Set chain end from new move.
        setChainEnd(playedDomino.getChain(),x,y);
        // Calculate scored points.
        calculateScoredPoints(playerID);
        // Remove domino from player's hand and return true.
        players[playerID].getHand().remove(dominoIndex);
        players[playerID].getHand().trimToSize();
        return true;
    }

    /**
     * setDominoChain sets the chain of the MoveInfo inputted.
     * @param x row of move
     * @param y col of move
     * @param move the legalMove having its chain set.
     * @param prevD the previousDomino move is being compared to.
     */
    private void setDominoChain(int x, int y, MoveInfo move, Domino prevD){
        // If the prevDomino is in center of board.
        if (prevD.getChain() == ' ') {
            // If we are placing to right of center, assign chain R.
            if (y == BOARDWIDTH/2 + 1) {
                move.setChain('R');
            }
            // If we are placing to left of center, assign chain L.
            else if (y == BOARDWIDTH/2 - 1) {
                move.setChain('L');
            }
        }
        // Otherwise, use the prevDomino's chain.
        else {
            move.setChain(prevD.getChain());
        }
        // If we are below spinner, set chain to D.
        if (x == BOARDHEIGHT/2 + 1 && prevD.isSpinner()) {
            move.setChain('D');
            move.setOrientation(2);
        }
        // If we are above spinner, set chain to U.
        else if (x == BOARDHEIGHT/2 - 1 && prevD.isSpinner()) {
            move.setChain('U');
            move.setOrientation(4);
        }
    }

    /**
     * setChainEnd updates the appropriate chainEnd to row x and col y depending on the char c inputted.
     * @param c character representing chain.
     * @param x row of chainEnd
     * @param y col of chainEnd
     */
    private void setChainEnd(char c, int x, int y){
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

    /**
     * setInvalidSpots looks at the inputted domino and sets invalid spots depending on its orientation.
     * @param x row of domino
     * @param y col of domino
     * @param playedDomino the inputted domino
     */
    private void setInvalidSpots(int x, int y, Domino playedDomino){
        if (!playedDomino.isSpinner()){
            switch (playedDomino.getOrientation()){
                // If domino is horizontal and not a spinner, spaces above and below are invalid.
                case 1:
                case 3:
                    // You can only play to the left at this position because of space constraints.
                    if (x == 0 && y == BOARDWIDTH/2){
                        board[x][y+1] = new Domino(-1,-1,-1,-1);
                    }
                    // You can only play to the right of this position because of space constraints.
                    if (x == BOARDHEIGHT && y == BOARDWIDTH/2){
                        board[x][y-1] = new Domino(-1,-1,-1,-1);
                    }
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
                // If domino is vertical and not a spinner, spaces to left and right are invalid.
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

    /**
     * calculateScoredPoints calculates the points a player scores when they place a piece.
     * @param playerID id of player who called function.
     */
    public void calculateScoredPoints(int playerID) {
        // If to right of center of board, check placed domino's rightPips vs end of
        // chain's
        // leftPips. If they are divisible by three, award player sum of pips.
        int leftPips;
        int rightPips;

        // If there is no left chain end, assign to default value of 0.
        if(chainEnds[0]==-1){
            leftPips=0;
        }
        else{
            leftPips = board[chainEnds[0]][chainEnds[1]].getLeftPipCount();
        }
        // If there is no right chain end, assign to default value of 0.
        if(chainEnds[2]==-1){
            rightPips =0;
        }
        else{
            rightPips=board[chainEnds[2]][chainEnds[3]].getRightPipCount();
        }
        int topPips = 0;
        int bottomPips = 0;
        // If left chainEnd is center of board, do not count.
        if (chainEnds[0] == BOARDHEIGHT/2 && chainEnds[1] == BOARDWIDTH/2){
            leftPips = 0;
        }
        // If right chainEnd is center of board, do not count.
        if (chainEnds[2] == BOARDHEIGHT/2 && chainEnds[3] == BOARDWIDTH/2){
            rightPips = 0;
        }
        // Only check bottomChain if it exists.
        if (chainEnds[6] != -1) {
             bottomPips = board[chainEnds[6]][chainEnds[7]].getRightPipCount();
        }
        // Only check topChain if it exists.
        if (chainEnds[4] != -1) {
              topPips = board[chainEnds[4]][chainEnds[5]].getLeftPipCount();
        }

        // If the the sum of pips is a multiple of 3, award user that amount of points.
        if ((leftPips + rightPips + bottomPips + topPips) % 3 == 0){
            players[playerID].addPoints(leftPips + rightPips + bottomPips + topPips);
        }
    }

    /**
     * drawPiece lets a player draw a domino from the boneyard.
     * @param playerID id of player who is drawing piece.
     * @return whether drawing piece was successful.
     */
    public boolean drawPiece(int playerID){
        // If boneyard is empty, player cannot draw piece. Return false.
        if (boneyard.isEmpty()){
            return false;
        }
        // Add the piece to the player's hand, remove from boneyard, and recalculate legal moves.
        players[playerID].getHand().add(boneyard.get(0));
        boneyard.remove(0);
        boneyard.trimToSize();

        findLegalMoves(playerID);

        return true;
    }

    /**
     * @param playerID player who wants to quit game.
     * @return Whether quitting was successful.
     */
    public boolean quitGame(int playerID){
        // Set the player who pressed "Quit" to -1 to indicate that they pressed Quit.
        players[playerID].setScore(-1);
        return true;
    }

    /**
     * @param playerID player who wants to start new game.
     * @return Whether starting a new game was succesful.
     */
    public boolean newGame(int playerID){
        // Set the player who pressed "New Game" to -2 to indicate that they have forfeited.
        players[playerID].setScore(-2);
        return true;
    }

    /**
     * @return height of board.
     */
    public int getBOARDHEIGHT(){
        return this.BOARDHEIGHT;
    }

    /**
     * @return width of board.
     */
    public int getBOARDWIDTH(){
        return this.BOARDWIDTH;
    }

    /**
     * @param row row of board to get domino from.
     * @param col col of board to get domino from.
     * @return domino in current row and col.
     */
    public Domino getDomino(int row, int col){
        return board[row][col];
    }

    public PlayerInfo[] getPlayerInfo(){
        return this.players;
    }

    public int getTurnID(){
        return this.turnID;
    }

    public ArrayList<Domino> getBoneyard(){
        return this.boneyard;
    }

    public String getMessage(){return this.message;}

    public void setMessage(String msg) { this.message = msg;}

    public String getBoneyardMsg(){return this.boneyardMsg;}

    public void setBoneyardMsg(String msg){this.boneyardMsg=msg;}

    public boolean isGameBlocked(){
        int i = 0;
        // Increment i if a player has no moves and the boneyard is empty.
        for (PlayerInfo p : players){
            if (p.getLegalMoves().size() == 0 && boneyard.size() == 0) {
                i++;
            }
        }
        // If all players have no moves and boneyard is empty, return true.
        return i == players.length;
    }

    public void setTurnID(){
        if (turnID + 1 < playerCount){
            turnID++;
        }
        else{
            turnID = 0;
        }
    }

    public void startRound(boolean firstRound){
        // Set (or reset) board to empty.
        //board = new ArrayList<ArrayList<Domino>>(BOARDHEIGHT);
        board = new Domino[BOARDHEIGHT][BOARDWIDTH];
        /*
        for (int i = 0; i < BOARDHEIGHT; i++){
            //board.add(new ArrayList<Domino>(BOARDWIDTH));
            for (int j = 0; j < BOARDWIDTH; j++) {
                //board.get(i).add(new Domino(-1,-1,-1,-1));
            }
        }

         */
        if (firstRound) {
            //create player objects equal to the amount of players playing.
            players = new PlayerInfo[playerCount];
            for (int i = 0; i < playerCount; i++) {
                players[i] = new PlayerInfo(i, true);
            }
        }
        // Board does not have spinner initially.
        doesBoardHaveSpinner = false;
        // Create a new DominoSet and shuffle it.
        dominoSet = new DominoSet();
        dominoSet.shuffleSet();
        // Deal out the dominoes.
        for (int i = 0; i < playerCount; i++){
            for (int j = 0; j < 5; j++) {
                players[i].getHand().add(dominoSet.dominos.get(0));
                dominoSet.dominos.remove(0);
            }
        }
        dominoSet.dominos.trimToSize();
        int size = dominoSet.dominos.size();
        // Fill the boneyard with the leftover dominoes in set. Empty dominoSet after.
        boneyard = new ArrayList<>();
        for (int i = 0; i < size; i++){
            // Fill each piece of boneyard with remaining dominoes in set. Then remove from dominoSet.
            boneyard.add(dominoSet.dominos.get(0));
            dominoSet.dominos.remove(0);
        }

        // Calculate firstMove info and automatically play piece.
        int[] firstMoveInfo = firstMove();
        turnID = firstMoveInfo[0];
        placeFirstPiece(firstMoveInfo[0],firstMoveInfo[1]);
        for (int i = 0; i < playerCount; i++){
            findLegalMoves(i);
        }
        setTurnID();

    }

    public void endRound(){
        int[] playerPipSum = new int[playerCount];
        // Sum up the pips in all player's hands.
        for (int i = 0; i < playerCount; i++){
            for (Domino d : players[i].getHand()){
                playerPipSum[i] += d.getLeftPipCount() + d.getRightPipCount();
            }
        }
        int lowestPipCount = 9999;
        // Whoever has the least amount of summed pips is the round's winner.
        for (int i = 0; i < playerCount; i++){
            if (playerPipSum[i] < lowestPipCount){
                lowestPipCount = i;
            }
        }
        // Round's winner gets the sum of the pips of all other players rounded to the nearest 3
        // added to their score.
        players[lowestPipCount].addPoints((int)(3.0*Math.round(playerPipSum[lowestPipCount]/3.0)));
    }

    public void placedAllPieces(int playerID){
        int[] playerPipSum = new int[playerCount];
        // Sum up the pips in all player's hands.
        for (int i = 0; i < playerCount; i++){
            for (Domino d : players[i].getHand()){
                playerPipSum[i] += d.getLeftPipCount() + d.getRightPipCount();
            }
        }
        int total = 0;
        for (int i = 0; i < playerPipSum.length; i++){
            if (i == playerID){
                continue;
            }
            total += playerPipSum[i];
        }

        // Round's winner gets the sum of the pips of all other players rounded to the nearest 3
        // added to their score.
        players[playerID].addPoints((int)(3.0*Math.round(total/3.0)));
        for (int i = 0; i < playerCount; i++){
            players[i].getLegalMoves().clear();
        }

    }
}
