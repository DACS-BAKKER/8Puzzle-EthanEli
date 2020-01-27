/*  Node Class
    Name: Ethan Chen and Eli Ji
    Date Started: October 10, 2019

*/

// Class contains a value and a pointer to another node. This is necessary for Linked List data structures

public class Node<Item> {
    public Item data;
    public Node next;

    public Node(Item data) {
        this.data = data;
    }

    public void setNext(Node n) {
        next = n;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
