/* *****************************************************************************
 *  Name: Eli Ji and Ethan Chen
 *  Date: Completed on 1-26-20
 *
 *  Description: Board class for the 8 Puzzle. Takes in a starting board
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Board {

    // class variables

    int[][] currBoard;
    private int[][] solvedBoard;
    private int dimension;

    // constructor

    public Board(int[][] currBoard) {
        this.currBoard = currBoard;
        dimension = currBoard.length;

        // create the solution board based on dimension
        solvedBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                solvedBoard[i][j] = (i * dimension + j + 1) % (dimension * dimension); // will always provide correct value for its position
            }
        }
    }

    // class methods

    public int dimension() {
        return dimension;
    }

    // calculates the hamming distance, the number of values that are not in their correct location
    public int hamming() {
        int outOfPlace = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (currBoard[i][j] == solvedBoard[i][j] && currBoard[i][j] != 0) { // checks if at each index the board value is the same as the solution board's
                    outOfPlace++;
                }
            }
        }
        return outOfPlace;
    }

    // calculates the manhattan distance, the total distance each number is from its spot (vertical + horizontal)
    public int manhattan() {
        int totalDistances = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if(currBoard[i][j] != 0) {
                    int targetI = (currBoard[i][j] - 1) / dimension; // finds i-value number should be at in solved set
                    int targetJ = (currBoard[i][j] - 1) % dimension; // finds j-value number should be at in solved set

                    int rowDistance = Math.abs(targetI - i);
                    int colDistance = Math.abs(targetJ - j);

                    totalDistances += (rowDistance + colDistance); // adds total distances
                }
            }
        }
        return totalDistances;
    }

    // checks to see if the board is equal to the solution board
    public boolean isGoal() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (currBoard[i][j] != solvedBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // returns a "twin" board, any board with 2 non-0 values switched from the original board
    public Board twin() {
        int[][] newBoard = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) { // copies over values of original board to new board
            for (int j = 0; j < dimension; j++) {
                newBoard[i][j] = currBoard[i][j];
            }
        }
        if (newBoard[0][0] == 0 || newBoard[0][1] == 0) { // if either 1st and 2nd in row 1 are 0
            newBoard[1][0] = currBoard[1][1]; // swap 1st and 2nd in row 2
            newBoard[1][1] = currBoard[1][0];
        } else {
            newBoard[0][0] = currBoard[0][1]; // swap 1st and 2nd in row 1
            newBoard[0][1] = currBoard[0][0];
        }
        return new Board(newBoard);
    }

    // checks to see if the int arrays hold the same values
    public boolean equals(Board y) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.currBoard[i][j] != y.currBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // iterable method for (for : each) loop that returns all the neighboring boards
    public Iterable<Board> neighbors() {

        // iterator class
        class BoardIterator implements Iterator<Board> {

            int direction = 1; // represents which side of the 0 to check for neighbors

            @Override
            public boolean hasNext() {
                return direction <= 4;
            } // checks all 4 directions

            @Override
            public Board next() {
                int iVal = 0;
                int jVal = 0;
                int[][] nextBoardArray = new int[dimension][dimension]; // board we will return

                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (currBoard[i][j] == 0) {
                            iVal = i; // saves i and j coordinates of the 0
                            jVal = j;
                        }
                        nextBoardArray[i][j] = currBoard[i][j]; // copies values of main board to nextBoardArray
                    }
                }

                // *** each else if statment is an optimization to reduce the number of nulls

                if (direction == 1 && iVal > 0) { // moves 0 up
                    nextBoardArray[iVal][jVal] = nextBoardArray[iVal - 1][jVal]; // swap 0 up
                    nextBoardArray[iVal-1][jVal] = 0;
                    direction++;
                    return new Board(nextBoardArray);
                } else if (direction == 1) { // if it doesn't exist, move straight to next neighbor
                    direction++;
                }

                if (direction == 2 && jVal > 0) { // moves 0 left
                    nextBoardArray[iVal][jVal] = nextBoardArray[iVal][jVal - 1]; // swap 0 left
                    nextBoardArray[iVal][jVal-1] = 0;
                    direction++;
                    return new Board(nextBoardArray);
                } else if (direction == 2) { // if it doesn't exist, move straight to next neighbor
                    direction++;
                }

                if (direction == 3 && iVal < dimension - 1) { // moves 0 down
                    nextBoardArray[iVal][jVal] = nextBoardArray[iVal + 1][jVal]; // swap 0 down
                    nextBoardArray[iVal+1][jVal] = 0;
                    direction++;
                    return new Board(nextBoardArray);
                } else if (direction == 3) { // if it doesn't exist, move straight to next neighbor
                    direction++;
                }

                if (direction == 4 && jVal < dimension - 1) { // moves 0 right
                    nextBoardArray[iVal][jVal] = nextBoardArray[iVal][jVal + 1]; // swap 0 right
                    nextBoardArray[iVal][jVal+1] = 0;
                    direction++;
                    return new Board(nextBoardArray);
                }

                direction++;
                return null; // only ever returns null if the 0 is against the right hand side
            }

        }

        // iterable class
        class BoardIterable implements Iterable<Board> {
            @Override
            public Iterator<Board> iterator() {
                return new BoardIterator();
            }
        }

        return new BoardIterable();
    }

    @Override
    public String toString() {
        String cumulativeString = "";
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cumulativeString += (currBoard[i][j] + " ");
            }
            cumulativeString += "\n";
        }
        return cumulativeString;
    }

    // test client
    public static void main(String[] args) {
        //user input board for 3x3
        int[][] startingBoard = new int[3][3];
        String numsRemaining = "123456780";
        int i = 0;
        int j = 0;
        while(!numsRemaining.isEmpty()) {
            StdOut.println("Please type a number from this list: " + numsRemaining);
            String x = StdIn.readString();
            if(numsRemaining.contains(x)) {
                int index = numsRemaining.indexOf(x);
                numsRemaining = numsRemaining.substring(0, index) + numsRemaining.substring(index+1);
                startingBoard[i][j] = Integer.valueOf(x);
                j++;
            }
            if(j == 3) {
                j = 0;
                i += 1;
            }
        }

        Board firstBoard = new Board(startingBoard);

        for(Board board : firstBoard.neighbors()) {
            StdOut.print(board);
            StdOut.println();
        }
    }


}
