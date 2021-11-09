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

    private int playerCount;
    private int BOARDHEIGHT = 9;
    private int BOARDWIDTH = 9;
    private DominoSet dominoSet;
    private ArrayList<ArrayList<Domino>> board;
    private int[] chainEnds;
    private boolean doesBoardHaveSpinner;
    private ArrayList<Domino> boneyard;
    private PlayerInfo[] players;
    private int turnID;
    private String message;
    private String boneyardMsg;
    public boolean boardEmpty;


    public DominoGameState() {

        message = "";
        boneyardMsg="";
        chainEnds = new int[8];
        Arrays.fill(chainEnds, -99);
        doesBoardHaveSpinner = false;

        board = new ArrayList<ArrayList<Domino>>(BOARDHEIGHT);
        for (int i = 0; i < BOARDHEIGHT; i++){
            board.add(new ArrayList<Domino>(BOARDWIDTH));
            for (int j = 0; j < BOARDWIDTH; j++) {
                board.get(i).add(new Domino(-1,-1,-1,-1));
            }
        }
    }

    public DominoGameState(DominoGameState other) {

        this.message = other.message;
        this.boneyardMsg = other.boneyardMsg;
        this.playerCount = other.playerCount;

        this.BOARDWIDTH = other.BOARDWIDTH;
        this.BOARDHEIGHT = other.BOARDHEIGHT;

        this.doesBoardHaveSpinner = other.doesBoardHaveSpinner;

        chainEnds = new int[8];
        System.arraycopy(other.chainEnds, 0, this.chainEnds, 0, 8);

        this.players = new PlayerInfo[other.players.length];

        for (int i = 0; i < playerCount; i++){
            this.players[i] = new PlayerInfo(other.players[i]);
        }

        this.board = new ArrayList<ArrayList<Domino>>(other.board.size());
        this.boneyard = new ArrayList<>(other.boneyard.size());
        this.dominoSet = new DominoSet(other.dominoSet);

        for (int i = 0; i < other.board.size(); i++){
            this.board.add(new ArrayList<Domino>(other.board.get(i).size()));
            for (Domino d : other.board.get(i)){
                this.board.get(i).add(new Domino(d));
            }
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
        // Set all of these to -1 initially.
        int highestMax = -1;
        int playerHighestMax = -1;
        int dominoHighestWeight = -1;
        // Loop through each player's hand. Update i to the player who has the highest max.
        // Update j to the index of highest weight.
        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < 5; j++) {
                if (players[i].getHand().get(j).getWeight() > highestMax) {
                    highestMax = players[i].getHand().get(j).getWeight();
                    playerHighestMax = i;
                    dominoHighestWeight = j;
                }
            }
        }
        // Store  the player who has highest max and the domino of height weight in an int array
        // and return.
        int[] goesFirstInfo = new int[2];
        goesFirstInfo[0] = playerHighestMax;
        goesFirstInfo[1] = dominoHighestWeight;
        return goesFirstInfo;
    }

    /**
     * findLegalMoves exists to make it easier to call the other 4 findLegalMoves functions.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMoves(int playerID){
        findLegalMovesLeft(playerID);
        findLegalMovesRight(playerID);
        findLegalMovesTop(playerID);
        findLegalMovesBottom(playerID);
    }

    /**
     * findLegalMovesLeft finds all of a player's legal moves on the left side of board.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMovesLeft(int playerID){
        int leftEndX = chainEnds[0];
        int leftEndY = chainEnds[1];
        // Index represents the dominoIndex.
        int index = 0;
        int x,y;
        ArrayList<MoveInfo> playerMoves = players[playerID].getLegalMoves();
        // Loop through the player's entire hand.
        for (Domino d: players[playerID].getHand()){
            // If the right pips on played domino match the left pips on board, add a new legal move
            // at x,y with orientation 1.
            if (d.getRightPipCount() == board.get(leftEndX).get(leftEndY).getLeftPipCount()){
                x = leftEndX;
                y = leftEndY - 1;
                playerMoves.add(new MoveInfo(x,y,1,index));
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(leftEndX).get(leftEndY));
            }
            // If the right pips on played domino match the left pips on board, add a new legal move
            // at x,y with orientation 3.
            else if (d.getLeftPipCount() == board.get(leftEndX).get(leftEndY).getLeftPipCount()){
                x = leftEndX;
                y = leftEndY - 1;
                playerMoves.add(new MoveInfo(x,y,3,index));
                // Set the legal move's chain.
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(leftEndX).get(leftEndY));
            }
            // Increment index as we are moving to next domino in hand.
            index++;
        }
    }

    /**
     * findLegalMovesRight finds all of a player's legal moves on the right side of board.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMovesRight(int playerID){
        int rightEndX = chainEnds[2];
        int rightEndY = chainEnds[3];
        // Index represents the dominoIndex.
        int index = 0;
        int x,y;
        ArrayList<MoveInfo> playerMoves = players[playerID].getLegalMoves();
        // Loop through the player's entire hand.
        for (Domino d: players[playerID].getHand()){
            // If the left pips on played domino match the right pips on board, add a new legal move
            // at x,y with orientation 1.
            if (d.getLeftPipCount() == board.get(rightEndX).get(rightEndY).getRightPipCount()){
                x = rightEndX;
                y = rightEndY + 1;
                playerMoves.add(new MoveInfo(x,y,1,index));
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(rightEndX).get(rightEndY));
            }
            // If the right pips on played domino match the right pips on board, add a new legal move
            // at x,y with orientation 3.
            else if (d.getRightPipCount() == board.get(rightEndX).get(rightEndY).getRightPipCount()){
                x = rightEndX;
                y = rightEndY + 1;
                playerMoves.add(new MoveInfo(x,y,3,index));
                // Set the legal move's chain.
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(rightEndX).get(rightEndY));
            }
            // Increment index as we are moving to next domino in hand.
            index++;
        }
    }

    /**
     * findLegalMovesTop finds all of a player's legal moves on the top of board.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMovesTop(int playerID){
        // If spinner has not been placed yet, return.
        if (chainEnds[4] == -99){
            return;
        }
        int topEndX = chainEnds[4];
        int topEndY = chainEnds[5];
        // Index represents the dominoIndex.
        int index = 0;
        int x,y;
        ArrayList<MoveInfo> playerMoves = players[playerID].getLegalMoves();
        for (Domino d: players[playerID].getHand()){
            // If the left pips on played domino match the left pips on board, add a new legal move
            // at x,y with orientation 2.
            if (d.getLeftPipCount() == board.get(topEndX).get(topEndY).getLeftPipCount()){
                x = topEndX - 1;
                y = topEndY;
                playerMoves.add(new MoveInfo(x,y,4,index));
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(topEndX).get(topEndY));
            }
            // If the right pips on played domino match the left pips on board, add a new legal move
            // at x,y with orientation 2.
            else if (d.getRightPipCount() == board.get(topEndX).get(topEndY).getLeftPipCount()){
                x = topEndX - 1;
                y = topEndY;
                playerMoves.add(new MoveInfo(x,y,2,index));
                // Set the legal move's chain.
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(topEndX).get(topEndY));
            }
            // Increment index as we are moving to next domino in hand.
            index++;
        }
    }

    /**
     * findLegalMovesBottom finds all of a player's legal moves on the bottom of board.
     * @param playerID The player who is calling the function.
     */
    public void findLegalMovesBottom(int playerID){
        // If spinner has not been placed yet, return.
        if (chainEnds[6] == -99){
            return;
        }
        int bottomEndX = chainEnds[6];
        int bottomEndY = chainEnds[7];
        // Index represents the dominoIndex.
        int index = 0;
        int x,y;
        ArrayList<MoveInfo> playerMoves = players[playerID].getLegalMoves();
        for (Domino d: players[playerID].getHand()){
            // If the left pips on played domino match the right pips on board, add a new legal move
            // at x,y with orientation 2.
            if (d.getLeftPipCount() == board.get(bottomEndX).get(bottomEndY).getRightPipCount()){
                x = bottomEndX + 1;
                y = bottomEndY;
                playerMoves.add(new MoveInfo(x,y,2,index));
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(bottomEndX).get(bottomEndY));
            }
            // If the right pips on played domino match the right pips on board, add a new legal move
            // at x,y with orientation 4.
            else if (d.getRightPipCount() == board.get(bottomEndX).get(bottomEndY).getRightPipCount()){
                x = bottomEndX + 1;
                y = bottomEndY;
                playerMoves.add(new MoveInfo(x,y,4,index));
                // Set the legal move's chain.
                setDominoChain(x, y, playerMoves.get(playerMoves.size() - 1),
                        board.get(bottomEndX).get(bottomEndY));
            }
            // Increment index as we are moving to next domino in hand.
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
        // Use this to keep track if the player is attempting to place from a legal move.
        int orientation = -1;
        MoveInfo move = null;
        // Loop through player's moves to check for a match.
        for (int i = 0; i < players[playerID].getLegalMoves().size(); i++){
            move = players[playerID].getLegalMoves().get(i);
            if (x == move.getRow() && y == move.getCol() && dominoIndex == move.getDominoIndex()) {
                orientation = move.getOrientation();
                break;
            }
        }

        // If no matching legal move was found, return false.
        if (orientation == -1){
            return false;
        }

        Domino playedDomino = players[playerID].getHand().get(dominoIndex);

        if (boardEmpty){
            // If the board is empty, does not have spinner, and playedDomino is a double,
            // set the playedDomino to as spinner and update appropriate chainEnds.
            if (!doesBoardHaveSpinner &&
                    playedDomino.getLeftPipCount() == playedDomino.getRightPipCount()){
                playedDomino.setSpinner();
                doesBoardHaveSpinner = true;
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
            boardEmpty = false;
        }

        if (!doesBoardHaveSpinner &&
                playedDomino.getLeftPipCount() == playedDomino.getRightPipCount()){
            playedDomino.setSpinner();
            doesBoardHaveSpinner = true;
            chainEnds[4] = x;
            //chainEnds[5] is spinnerTopEndY;
            chainEnds[5] = y;
            //chainEnds[6] is spinnerBottomEndX.
            chainEnds[6]= x;
            //chainEnds[7] is spinnerBottomEndY.
            chainEnds[7] = y;
        }

        // Set the played domino's orientation to match the legal move's orientation.
        playedDomino.setOrientation(orientation);
        // Set the chain to match the move's chain.
        playedDomino.setChain(move.getChain());

        // If we are below zero, add a new row to board.
        if (x < 0){
            // Add a new row to arraylist and fill each col with a placeholder.
            board.add(0,new ArrayList<Domino>(BOARDWIDTH));
            for (int i = 0; i < board.size(); i++){
                board.get(0).add(new Domino(-1,-1,-1,-1));
            }
            // Because we are adding a new row, increment every row chainEnd by one.
            chainEnds[0]++;
            chainEnds[2]++;
            chainEnds[4]++;
            chainEnds[6]++;
        }
        else if (x == board.size()){
            // Add a new row to arraylist and fill each col with a placeholder.
            board.add(new ArrayList<Domino>(BOARDWIDTH));
            for (int i = 0; i < 9; i++){
                board.get(x).add(new Domino(-1,-1,-1,-1));
            }
        }
        // If we are not at beginning or end of  board and NOT at center, overwrite placeholder
        // with played domino.
        else if (x != board.size()/2){
            board.get(x).set(y,playedDomino);
        }
        // We only place to left or right if X is the center of the board.
        if (x == board.size()/2) {
            // If we are below zero, add a new col to board.
            if (y < 0) {
                board.get(x).add(0, playedDomino);
                for (int i = 0; i < board.size(); i++) {
                    // Do not add a placeholder to spot we are placing domino in.
                    if (i == x) {
                        continue;
                    }
                    board.get(i).add(0, new Domino(-1, -1, -1, -1));
                }
                // Because we are adding a new col, increment every col chainEnd by one.
                chainEnds[1]++;
                chainEnds[3]++;
                chainEnds[5]++;
                chainEnds[7]++;
            }
            // If we are at the last col, add a new domino to end.
            else if (y == board.get(x).size()) {
                board.get(x).add(playedDomino);
            }
            // Otherwise, overwrite placeholder to be current domino.
            else {
                board.get(x).set(y, playedDomino);
            }
        }

        // Set chain end from new move.
        setChainEnd(x,y);
        // Calculate scored points.
        calculateScoredPoints(playerID);
        // Remove domino from player's hand and return true.
        players[playerID].getHand().remove(dominoIndex);
        players[playerID].getHand().trimToSize();
        // Update all player's legal moves.
        for (int i = 0; i < playerCount; i++){
            players[i].getLegalMoves().clear();
            findLegalMoves(i);
        }
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
            if (y > board.get(BOARDHEIGHT/2).size()/2) {
                move.setChain('R');
            }
            // If we are placing to left of center, assign chain L.
            else if (y < board.get(BOARDHEIGHT/2).size()/2) {
                move.setChain('L');
            }
        }
        // Otherwise, use the prevDomino's chain.
        else {
            move.setChain(prevD.getChain());
        }
        // If we are below spinner, set chain to D.
        if (x > board.size()/2 && prevD.isSpinner() || prevD.getChain() == 'D') {
            move.setChain('D');
        }
        // If we are above spinner, set chain to U.
        else if (x < board.size()/2 && prevD.isSpinner() || prevD.getChain() == 'U') {
            move.setChain('U');
        }
    }

    /**
     * setChainEnd updates the appropriate chainEnd to row x and col y depending on the char c inputted.
     * @param x row of chainEnd
     * @param y col of chainEnd
     */
    private void setChainEnd(int x, int y) {
        // If we are adding to 0 in arraylist, reset chainEnd to zero.
        if (x < 0) {
            x = 0;
        }
        // If we are adding to 0 in arraylist, reset chainEnd to zero.
        if (y < 0) {
            y = 0;
        }

        if (x < board.size() / 2) {
            chainEnds[4] = x;
            chainEnds[5] = y;
            return;
        } else if (x > board.size() / 2) {
            chainEnds[6] = x;
            chainEnds[7] = y;
            return;
        }
        if (y < board.get(board.size() / 2).size() / 2) {
            chainEnds[0] = x;
            chainEnds[1] = y;
        } else if (y > board.get(board.size() / 2).size() / 2){
            chainEnds[2] = x;
            chainEnds[3] = y;
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
        if(chainEnds[0]==-99){
            leftPips=0;
        }
        else{
            leftPips = board.get(chainEnds[0]).get(chainEnds[1]).getLeftPipCount();
        }
        // If there is no right chain end, assign to default value of 0.
        if(chainEnds[2]==-99){
            rightPips =0;
        }
        else{
            rightPips= board.get(chainEnds[2]).get(chainEnds[3]).getRightPipCount();
        }
        int topPips = 0;
        int bottomPips = 0;
        // Only check bottomChain if it exists.
        if (chainEnds[6] != -99) {
             bottomPips = board.get(chainEnds[6]).get(chainEnds[7]).getRightPipCount();
        }
        if (chainEnds[6] == BOARDHEIGHT/2 && chainEnds[7] == BOARDWIDTH/2){
            bottomPips = 0;
        }
        // Only check topChain if it exists.
        if (chainEnds[4] != -99) {
              topPips = board.get(chainEnds[4]).get(chainEnds[5]).getLeftPipCount();
        }
        if (chainEnds[4] == BOARDHEIGHT/2 && chainEnds[5] == BOARDWIDTH/2){
            topPips = 0;
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
        // Update the legal moves of player who drew a piece.
        players[playerID].getLegalMoves().clear();
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
     * @param row row of board to get domino from.
     * @param col col of board to get domino from.
     * @return domino in current row and col.
     */
    public Domino getDomino(int row, int col){
        return board.get(row).get(col);
    }

    /**
     *
     * @return The player info of all players.
     */
    public PlayerInfo[] getPlayerInfo(){
        return this.players;
    }

    /**
     *
     * @return Current turn ID.
     */
    public int getTurnID(){
        return this.turnID;
    }

    /**
     *
     * @return The boneyard.
     */
    public ArrayList<Domino> getBoneyard(){
        return this.boneyard;
    }

    /**
     *
     * @return The current message of DominoGameState.
     */
    public String getMessage(){return this.message;}

    /**
     *
     * @param msg The message to set msg to.
     */
    public void setMessage(String msg) { this.message = msg;}

    /**
     *
     * @return boneyardMsg.
     */
    public String getBoneyardMsg(){return this.boneyardMsg;}

    /**
     *
     * @param msg The message to set boneyardMsg to.
     */
    public void setBoneyardMsg(String msg){this.boneyardMsg=msg;}

    /**
     *
     * @return True if no players have legal moves, otherwise false.
     */
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

    /**
     * Update the turnID.
     */
    public void setTurnID(){
        if (turnID + 1 < playerCount){
            turnID++;
        }
        else{
            turnID = 0;
        }
    }

    /**
     *
     * @param firstRound Whether the round is the first round or not.
     */
    public void startRound(boolean firstRound){
        // Set (or reset) board to empty.
        board = new ArrayList<ArrayList<Domino>>(BOARDHEIGHT);
        for (int i = 0; i < BOARDHEIGHT; i++){
            board.add(new ArrayList<Domino>(BOARDWIDTH));
            for (int j = 0; j < BOARDWIDTH; j++) {
                board.get(i).add(new Domino(-1,-1,-1,-1));
            }
        }
        boardEmpty = true;

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
        players[firstMoveInfo[0]].getLegalMoves().add(new MoveInfo(BOARDHEIGHT/2,BOARDWIDTH/2, 1,
                firstMoveInfo[1]));
    }

    /**
     * If the game is blocked, ends round and determines the round's winner.
     */
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

    /**
     *  Awards the player who placed all of their pieces points equal to the pips on opponent's
     *  dominos summed and rounded to the nearest three.
     * @param playerID The player who placed all their pieces.
     */
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

    /**
     *
     * @return Number of rows in board.
     */
    public int getBoardXSize(){
        return this.board.size();
    }

    /**
     *
     * @param x The row to get col size from.
     * @return The number of cols in row x.
     */
    public int getBoardYSize(int x){
        return this.board.get(x).size();
    }
}
