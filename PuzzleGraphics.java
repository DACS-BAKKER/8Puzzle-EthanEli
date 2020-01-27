/* *****************************************************************************
 *  Name: Eli Ji and Ethan Chen
 *  Date: Completed on 1-26-20
 *
 *  Description: Graphics for the 8 puzzle solver. Takes in starting board as
 *               a parameter. Run graphics from this class.
 **************************************************************************** */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PuzzleGraphics extends JPanel implements ActionListener {

    private int xyOffset = 100;
    private int[][][] solution;
    private int solutionIndex = 0;
    private Timer tm;
    private int velocity = 1;
    // x coordinate of block in motion
    private int movingX = 0;
    // y coordinate of block in motion
    private int movingY = 0;
    private boolean inMotion = false;
    private boolean isSolvable;

    public static void main(String[] args) {

        //CHANGE STARTING BOARD HERE!

        // user input for board for 3x3
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

        // preset board, uncomment to use and comment out section above
        //int[][] startingBoard = {{8, 7, 5}, {6, 4, 2}, {3, 1, 0}};

        Board b = new Board(startingBoard);
        PuzzleGraphics p = new PuzzleGraphics(b);

        JFrame f = new JFrame();
        f.setTitle("8 Puzzle Graphics");
        f.setSize(600, 650);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.add(p);
        f.setVisible(true);
    }

    public PuzzleGraphics(Board startingBoard) {
        Solver s = new Solver(startingBoard);

        // set up solution array
        int solutionLength = 0;
        for (Board b : s.solution()) {
            solutionLength++;
        }
        solution = new int[solutionLength][startingBoard.dimension()][startingBoard.dimension()];

        // fill in solution array
        int counter = 0;
        for (Board b : s.solution()) {
            for (int i = 0; i < startingBoard.dimension(); i++) {
                for (int j = 0; j < startingBoard.dimension(); j++) {
                    solution[counter][i][j] = b.currBoard[i][j];
                }
            }
            counter++;
        }

        isSolvable = s.isSolvable();

        tm = new Timer(10, this);
        tm.setInitialDelay(6000);
        tm.start();
    }

    public void paintComponent(Graphics g) {
        // change graphics settings
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        float thickness = 4;
        g2.setStroke(new BasicStroke(thickness));

        int rectLength = (getWidth() - 2 * xyOffset) / solution[0].length;

        int[][] currentBoard = solution[solutionIndex];
        int[][] nextBoard = null;
        if (solution.length > 1) {
            nextBoard = solution[solutionIndex + 1];
        }
        // The I and J of the block that needs to be moved
        int previousI = 0;
        int previousJ = 0;
        // The I and J of where the block needs to be moved to
        int newI = 0;
        int newJ = 0;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                if (currentBoard[i][j] == 0) {
                    newI = i;
                    newJ = j;
                }
                if (nextBoard != null && nextBoard[i][j] == 0) {
                    previousI = i;
                    previousJ = j;
                }
            }
        }

        // The x and y of where the block needs to moved to
        int targetX = xyOffset + newJ * rectLength;
        int targetY = xyOffset + newI * rectLength;

        // Next board reset
        if (!inMotion) {
            movingX = xyOffset + previousJ * rectLength;
            movingY = xyOffset + previousI * rectLength;
        }
        // Outer rectangle
        g.drawRect(xyOffset, xyOffset, rectLength * solution[0].length, rectLength * solution[0].length);
        // Inner part
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                // Moving block
                if (i == previousI && j == previousJ) {
                    g.drawRect(movingX, movingY, rectLength, rectLength);
                    String number = Integer.toString(currentBoard[i][j]);
                    drawCenteredString(g, number, new Rectangle(movingX, movingY, rectLength, rectLength),
                            new Font("Courier", Font.PLAIN, rectLength / 3));
                    if (isSolvable) {
                        inMotion = true;
                    }
                }
                // Other blocks
                else if (!(i == newI && j == newJ)) {
                    g.drawRect(xyOffset + j * rectLength, xyOffset + i * rectLength, rectLength, rectLength);
                    String number = Integer.toString(currentBoard[i][j]);
                    drawCenteredString(g, number, new Rectangle(xyOffset + j * rectLength, xyOffset + i * rectLength, rectLength, rectLength),
                            new Font("Courier", Font.PLAIN, rectLength / 3));
                }
            }
        }

        // Not solvable label
        if (!isSolvable) {
            drawCenteredString(g, "This is not solvable.", new Rectangle(10, 500, getWidth(), 100),
                    new Font("Courier", Font.PLAIN, rectLength / 5));
        }

        // When reached target move to next board
        if (movingX == targetX && movingY == targetY) {
            inMotion = false;
            solutionIndex++;
            // Completed label
            if (solutionIndex == solution.length - 1) {
                String finishedLabel = "Completed in " + (solution.length - 1) + " steps.";
                drawCenteredString(g, finishedLabel, new Rectangle(10, 500, getWidth(), 100),
                        new Font("Courier", Font.PLAIN, rectLength / 5));
            }
        }

        // Move the block
        if (inMotion) {
            if (newJ < previousJ) {
                //move it left
                movingX = movingX - velocity;
            } else if (newJ > previousJ) {
                //move it right
                movingX = movingX + velocity;
            } else if (newI < previousI) {
                //move it up
                movingY = movingY - velocity;
            } else if (newI > previousI) {
                //move it down
                movingY = movingY + velocity;
            }
        }
    }

    //Source: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if finished
        if (solutionIndex != solution.length - 1) {
            repaint();
        }
    }
}