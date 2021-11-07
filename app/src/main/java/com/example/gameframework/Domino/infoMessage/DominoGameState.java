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
    private PlayerInfo[] players;
    private int turnID;
    private String message;
    public boolean boardEmpty;


    public DominoGameState() {

        message = "";

        chainEnds = new int[8];
        Arrays.fill(chainEnds, -1);
        doesBoardHaveSpinner = false;

        board = new Domino[BOARDHEIGHT][BOARDWIDTH];

        boneyard = new ArrayList<>();
        // Start the first round.
        startRound();
    }

    public DominoGameState(DominoGameState other) {

        message = other.message;
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
        startRound();
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
     *
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

    public void findLegalMoves(int playerID){
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
                        /*
                         * Check if the leftPips of placed domino matches rightPips of domino already on board.
                         * If there is a match, place the domino there.
                         */
                        if (playedDomino.getLeftPipCount() == prevDomino.getRightPipCount()) {
                            // If we reach the leftmost end of board, make domino's orientation vertical down.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            if (0 == y) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If we reach the rightmost end of board, make domino's orientation vertical up.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we are placing to left of center of board, rotate piece 180 degrees.
                            else if (y < BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }

                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            } else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        /*
                         * Check if the rightPips of placed domino matches leftPips of domino already on board.
                         * If there is a match, place the domino there.
                         */
                        if (playedDomino.getRightPipCount() == prevDomino.getLeftPipCount()) {
                            // If we reach the leftmost end of board, make domino's orientation vertical down.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If we reach the rightmost end of board, make domino's orientation vertical up.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If domino is placed to right of center, rotate 180 degrees.
                            else if (y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }

                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            } else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        /*
                         * Check if the leftPips of placed domino matches leftPips of domino already on board.
                         * If there is a match, place the domino there.
                         */
                        if (playedDomino.getLeftPipCount() == prevDomino.getLeftPipCount()) {
                            // If we reach the leftmost end of board, make domino's orientation vertical up.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we reach the rightmost end of board, make domino's orientation vertical down.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If left pips matches left pips, domino MUST be rotated 180 degrees.
                            else {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }

                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            } else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                            continue;
                        }
                        /*
                         * Check if the rightPips of placed domino matches rightPips of domino already on board.
                         * If there is a match, place the domino there.
                         */
                        if (playedDomino.getRightPipCount() == prevDomino.getRightPipCount()) {
                            // If we reach the leftmost end of board, make domino's orientation vertical up.
                            if (board[x][y] == null) {
                                playerLegalMoves.add(new MoveInfo(x, y, 1, index));
                            }
                            else{
                                continue;
                            }
                            if (y == 0) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(2);
                            }
                            // If we reach the rightmost end of board, make domino's orientation vertical down.
                            else if (y == BOARDWIDTH - 1) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(4);
                            }
                            // If right pips matches right pips, domino MUST be rotated 180 degrees.
                            else {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }

                            if (x == 0 && y <= BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(1);
                            } else if (x == 0 && y > BOARDWIDTH / 2) {
                                playerLegalMoves.get(playerLegalMoves.size() - 1).setOrientation(3);
                            }
                            setDominoChain(x, y, playerLegalMoves.get(playerLegalMoves.size() - 1), prevDomino);
                        }
                    }
                }
            }
            index++;
        }
    }

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
        return true;
    }

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

    private void setInvalidSpots(int x, int y, Domino playedDomino){
        if (!playedDomino.isSpinner()){
            switch (playedDomino.getOrientation()){
                // If domino is horizontal and not a spinner, spaces above and below are invalid.
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

    public boolean drawPiece(int playerID){
        // If boneyard is empty, player cannot draw piece. Return false.
        if (boneyard.isEmpty()){
            return false;
        }
        // Add the piece to the player's hand, remove from boneyard, and recalculate legal moves.
        players[playerID].getHand().add(boneyard.get(0));
        boneyard.remove(0);
        findLegalMoves(playerID);

        return true;
    }

    public boolean quitGame(int playerID){
        // Set the player who pressed "Quit" to -1 to indicate that they pressed Quit.
        players[playerID].setScore(-1);
        return true;
    }

    public boolean newGame(int playerID){
        // Set the player who pressed "New Game" to -2 to indicate that they have forfeited.
        players[playerID].setScore(-2);
        return true;
    }

    public int getBOARDHEIGHT(){
        return this.BOARDHEIGHT;
    }

    public int getBOARDWIDTH(){
        return this.BOARDWIDTH;
    }

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

    public boolean isGameBlocked(){
        int i = 0;
        for (PlayerInfo p : players){
            if (p.getLegalMoves().size() == 0) {
                i++;
            }
        }
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

    public void startRound(){
        // Set (or reset) board to empty.
        for (int i = 0; i < BOARDHEIGHT; i++){
            for(int j = 0; j < BOARDWIDTH; j++){
                board[i][j] = null;
            }
        }

        //create player objects equal to the amount of players playing.
        players = new PlayerInfo[playerCount];
        for(int i=0; i <playerCount;i++) {
            players[i] = new PlayerInfo(i,true);
        }

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

        int size = dominoSet.dominos.size();
        // Fill the boneyard with the leftover dominoes in set. Empty dominoSet after.
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
        for (int i = 0; i < playerCount; i++){
            for (Domino d : players[i].getHand()){
                playerPipSum[i] += d.getLeftPipCount() + d.getRightPipCount();
            }
        }
        int lowestPipCount = 9999;
        for (int i = 0; i < playerCount; i++){
            if (playerPipSum[i] < lowestPipCount){
                lowestPipCount = i;
            }
        }

        players[lowestPipCount].addPoints((int)(3.0*Math.round(playerPipSum[lowestPipCount]/3.0)));
    }
}
