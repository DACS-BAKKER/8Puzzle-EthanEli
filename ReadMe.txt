Project: 8 Puzzle
Names: Eli Ji and Ethan Chen
Date Completed: January 26, 2020

PROJECT FILES:
PuzzleGraphics - Graphical representation of how to solve the 8 puzzle given an inputted array.

Solver - Takes a nxn dimensioned Board and solves the board using the A* search algorithm and Min Priority Queues.
         Contains nested class SearchNode and a test client to test the functionality.


EXTRA FILES:
Board - Takes a 2-dimensional array of size nxn and formulates into a "board" that can compare boards, find "neighboring"
        boards, and solve for the manhattan and hamming distance for the 8 puzzle. This file is also runnable.

Stack - taken from the Stack and Queue project, a LIFO data structure implemented using a linked list.

Node - a class used for a linked list, contains some generic value (in this case a SearchNode) and a pointer to another node.


NOTES:
The 8 Puzzle Solver class has been tested multiple times and can consistently solve (or find not solvable) any 3x3 array.
However, when we go to 4x4 or to higher dimensions, it is still possible to solve, but it requires far too much time.
We have tested the minimum number of steps vs. time for a 4x4, and it looks as follows:

5 steps: 0.023 seconds (no idea why this is greater than the latter, maybe just lag)
10 steps: 0.011 seconds
15 steps: 0.011 seconds
20 steps: 0.015 seconds
25 steps: 0.046 seconds
27 steps: 0.139 seconds
31 steps: 0.301 seconds
35 moves: 0.517 seconds
41 moves: 5.197 seconds (prepare for large jump)
43 moves: 39.532 seconds

To attain these values, we hand manipulated a 4x4 array into several different configurations, one step at a time

As you can see, it is possible to solve these boards, but as there are more moves required to get to the solution board,
the time required to complete the operations grows exponentially. This is why all the test clients are built around 3x3 boards.

Also, a common snippet of code from the Board class is:
      for (int i = 0; i < dimension; i++) {
          for (int j = 0; j < dimension; j++) {
              newBoard[i][j] = currBoard[i][j];
           }
      }

The reason we did this instead of simply saying newBoard = currBoard is because the equivalence would entail the objects
being one and the same, and therefore being unable to make alterations to one without altering the other. Copying values
into a new object instead allows us to alter one without altering the main board, which is important for the neighbors
method.
