/* *****************************************************************************
 *  Name: Eli Ji and Ethan Chen
 *  Date: Completed on 1-26-20
 *
 *  Description: Graphics for the 8 puzzle solver. Takes in starting board as
 *               a parameter. Run graphics from this class.
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Iterator;

public class Solver {

    // instance variables
    private int moves;
    private boolean isSolvable;
    private Board startingBoard;
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> twinPQ;
    private SearchNode endNode;
    private Stack<Board> solutionStack;

    // constructor
    public Solver(Board initial) {
        startingBoard = initial;
        pq = new MinPQ<SearchNode>();
        twinPQ = new MinPQ<SearchNode>();
        isSolvable = solve(startingBoard);
        moves = moves();
    }

    // class methods
    public boolean isSolvable() {
        return isSolvable;
    }

    // solves by taking SearchNodes from the top of the priority queue
    // if that SearchNode does not contain the solution board, it finds all the neighbors and adds it
    // to the priority queue, and repeats the process
    // this happens simultaneously while running on a twin board

    private boolean solve(Board currBoard) {
        Board twin = currBoard.twin();
        SearchNode first = new SearchNode(currBoard, null);
        SearchNode firstTwin = new SearchNode(twin, null); // twin
        pq.insert(first);
        twinPQ.insert(firstTwin);
        while (!pq.isEmpty() || !twinPQ.isEmpty()) {

            SearchNode current = pq.delMin();
            SearchNode currentTwin = twinPQ.delMin(); // twin

            if (current.board.isGoal()) {
                endNode = current;
                return true;
            }
            if(currentTwin.board.isGoal()) { // twin
                return false;
            }

            addNeighborstoPQ(current, false);
            addNeighborstoPQ(currentTwin, true); // twin

        }
        return false; // this should never occur
    }

    // adds the neighbors of the SearchNode to the priority queue
    // uses boolean twin to know if this method should access the twin priority queue or not
    private void addNeighborstoPQ(SearchNode node, boolean twin) {
        for (Board neighborB : node.board.neighbors()) {
            if (neighborB != null) { // makes sure the neighbor exists
                if (node.lastNode != null) {
                    if (!neighborB.equals(node.lastNode.board)) { // checks to see if the neighbor is not the board it came from
                        SearchNode childNode = new SearchNode(neighborB, node);

                        if(twin) {
                           twinPQ.insert(childNode);
                        } else {
                            pq.insert(childNode);
                        }

                    }
                } else { // if there is no last node (top node) then add neighbors if it exists
                    SearchNode childNode = new SearchNode(neighborB, node);

                    if(twin) {
                        twinPQ.insert(childNode);
                    } else {
                        pq.insert(childNode);
                    }
                }
            }

        }
    }

    public int moves() {
        if (isSolvable) {
            return endNode.movesMade;
        } else {
            return -1;
        }
    }

    // iterable method
    // returns the Boards in the solution set, run by a (for : each) loop.
    public Iterable<Board> solution() {

        // if it is not solvable, use only the initial board
        if (!isSolvable) {
            StdOut.println("This is not solvable.");
            endNode = new SearchNode(startingBoard, null);
        }

        // adds all boards in solution set to stack, from solution to initial
        solutionStack = new Stack<Board>();
        SearchNode currNode = endNode;
        solutionStack.push(currNode.board);
        while (currNode.lastNode != null) {
            currNode = currNode.lastNode;
            solutionStack.push(currNode.board);
        }

        // iterator class
        class BoardIterator implements Iterator<Board> {

            @Override
            public boolean hasNext() {
                if (solutionStack.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }

            // pops off from stack from initial to solution, effectively reversing order
            @Override
            public Board next() {
                return solutionStack.pop();
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

    // search node class
    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int movesMade;
        int priority; // equal to the manhattan distance + movesMade
        SearchNode lastNode;

        // constructor for SearchNode, if lastBoard is null, it initiates as first board and sets movesMade to 0
        private SearchNode(Board board, SearchNode lastBoard) {
            this.board = board;

            if (lastBoard == null) {
                this.movesMade = 0;
            } else {
                this.lastNode = lastBoard;
                this.movesMade = this.lastNode.movesMade + 1;
            }

            this.priority = board.manhattan() + this.movesMade;
        }

        // implements Comparable into SearchNode, allows them to be compared for priority queue
        // sets values with higher priority as greater than search nodes with lower
        @Override
        public int compareTo(SearchNode o) {
            if (priority > o.priority) {
                return 1;
            } else if (priority == o.priority) {
                return 0;
            } else {
                return -1;
            }
        }
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

        // int[][] startingBoard = {{14, 0, 4, 8}, {3, 2, 11, 12}, {6, 5, 13, 15}, {1, 9, 7, 10}}; // if you would like to test a 4x4

        Board firstBoard = new Board(startingBoard);

        // times the process
        Stopwatch t = new Stopwatch();
        Solver solver = new Solver(firstBoard);
        StdOut.println("This took " + t.elapsedTime() + " seconds to complete.");
        StdOut.println();

        // prints out the steps to complete
        for (Board b : solver.solution()) {
            StdOut.println(b);
            StdOut.println();
        }
        StdOut.println("This process took " + solver.moves() + " moves.");
    }

}
