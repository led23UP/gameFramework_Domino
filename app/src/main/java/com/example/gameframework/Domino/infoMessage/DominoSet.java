package com.example.gameframework.Domino.infoMessage;

import static java.lang.Integer.parseInt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
/*
    External Citation:
    10/4/2021
    Took some code from here and ported it from C# to Java because I liked the way they did it.
    https://codereview.stackexchange.com/questions/58464/basic-domino-game-architecture
 */

/** DominoSet class that creates a set of dominoes for the game.
 * @author Connor Burk
 * @author David Le
 * @author Paul Kenstler
 * @author Pranav Rajan
 */
public class DominoSet implements Serializable {

    private static final long serialVersionUID = -5109179064363136954L;

    public ArrayList<Domino> dominos;

    public DominoSet() {
        // Doubles have more weight than non-doubles!
        final String[] tiles = {"0,1", "0,2", "0,3", "0,4", "0,5", "0,6",
                "1,2", "1,3", "1,4", "1,5", "1,6",
                "2,3", "2,4", "2,5", "2,6",
                "3,4", "3,5", "3,6",
                "4,5", "4,6",
                "5,6",
                "0,0","1,1","2,2","3,3","4,4","5,5","6,6"};
        dominos = new ArrayList<Domino>(28);

        for(int i = 0; i < 28; i++){
            String[] word = tiles[i].split(",");
            dominos.add(new Domino(parseInt(word[0]), parseInt(word[1]),1, i));

        }
    }

    public DominoSet(DominoSet other){
        this.dominos = new ArrayList<Domino>(other.dominos.size());
        for (int i = 0; i < other.dominos.size(); i++){
            this.dominos.add(new Domino(other.dominos.get(i)));
        }
    }

    public void shuffleSet(){
        Collections.shuffle(this.dominos);
    }

}

