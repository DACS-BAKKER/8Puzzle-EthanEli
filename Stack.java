/*  Stack API
    Name: Ethan Chen and Eli Ji
    Date Started: October 10, 2019

*/

import edu.princeton.cs.algs4.*;
import java.util.Iterator;

public class Stack<Item> { //LIFO (Last-In First-Out) Data Structure

    Node top;

    public Stack() { // instantiates an empty stack

    }

    public Stack(Node initialNode) { // instantiates a stack with a starting node
        top = initialNode;
    }

    public Stack(int item) { // instantiates a stack given a data value for the starting node
        top = new Node(item);
    }

    public boolean isEmpty() { // returns true if stack is empty, false otherwise
        if (top == null) {
            return true;
        } else {
            return false;
        }
    }

    public void push(Item item) { // inserts a node at the beginning of the stack.
        Node newNode = new Node(item);

        if (!isEmpty()) {
            Node tempNode = top;
            top = newNode;
            top.setNext(tempNode);
        } else {
            top = newNode;
        }
    }

    public Item pop() { // removes the top node and returns its value, given that it exists
        Node temp;

        if(!isEmpty()) {
            if(top.next != null) {
                temp = top;
                top = top.next;
                return (Item) temp.data;
            } else {
                temp = top;
                top = null;
                return (Item) temp.data;
            }
        } else {
            return null;
        }
    }

    public int size() { // returns the number of nodes in the stack
        if (!isEmpty()) {
            int sizeCounter = 1;
            Node currentNode = top;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                sizeCounter++;
            }
            return sizeCounter;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() { // returns the list as a string reading from the top of the stack to the bottom

        if(!isEmpty()) {
            Node currentNode = top;
            String stackString = "TOP: " + top.data.toString();
            while(currentNode.next != null) {
                currentNode = currentNode.next;
                stackString = stackString + " => " + currentNode.data.toString();
            }
            return stackString;
        } else {
            return "Empty Stack";
        }
    }

    //Class Runner

    public static void main(String[] args) { // Interactive Console User Interface to test Stack methods
        Stack firstStack = new Stack();

        StdOut.println("Welcome to the Stack Console Tester");
        StdOut.println();
        StdOut.println("Type 1 to add push to the stack; 2 to pop the top item of the stack, 3 to show the length, or 4 to ask if it is empty, or 0 to exit");
        int userInput = StdIn.readInt();
        while(userInput != 0) {
            if(userInput == 1) {
                StdOut.println("What would you like to add to the Stack?");
                String input = StdIn.readString();
                firstStack.push(input);
                StdOut.println("You have added " + input + " to your Stack");
            } else if(userInput == 2) {
                Object popped = firstStack.pop();
                if(popped == null) {
                    StdOut.println("We can't pop something that isn't there. Good try.");
                } else {
                    StdOut.println("You have removed " + popped + " from the Stack");
                }
            } else if(userInput == 3) {
                StdOut.println("Your stack is " + firstStack.size() + " items long");
            } else if(userInput == 4) {
                StdOut.println("Is your stack empty? " + firstStack.isEmpty());
            }
            StdOut.println();
            StdOut.println("Your current Stack is as follows:");
            StdOut.println(firstStack.toString());
            StdOut.println();
            StdOut.println("Type 1 to add push to the stack; 2 to pop the top item of the stack, 3 to show the length, 4 to ask if it is empty, or 0 to exit");
            userInput = StdIn.readInt();

        }

        StdOut.println("Thank you for using the stack Console Tester");
        StdOut.println("Your final stack was: " + firstStack.toString());

    }
}
