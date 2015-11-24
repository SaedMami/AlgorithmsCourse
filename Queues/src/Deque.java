import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 * Compilation: javac Deque.java
 * 
 * Dependencies: StdDraw.java
 * 
 * 
 * An implementation for a generic double ended queue where item can be inserted
 * and removed either from the front or the end of the deque. This data
 * structure is implemented using doubly linked lists to guarantee constant
 * worst time in inserting and removing elements at the front and end of the
 * deque
 * 
 * @author Saed Mami
 ******************************************************************************/

public class Deque<Item> implements Iterable<Item> {

    // helper linked list class
    private class Node {
        private Item item = null;
        private Node next = null;
        private Node previous = null;
    }

    // data
    private Node _first; // first node in the deque
    private Node _last; // last node in the deque
    private int _N; // number of nodes

    /**
     * Constructs an empty deque
     */
    public Deque() {
        _first = null;
        _last = null;
        _N = 0;
    }

    /**
     * @return whether the deque is empty or not
     */
    public boolean isEmpty() {
        return _N == 0;
    }

    /**
     * @return the number of item in the deque
     */
    public int size() {
        return _N;
    }

    /**
     * Adds a new item to the front of the queue.
     *
     * @param item
     *            A generic item to be added at the top of the queue
     * @throws NullPointerException
     *             if item is NULL
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();

        _N++;
        Node newNode = new Node();
        newNode.item = item;

        // if we're adding the first node then first and last must point to
        // this node (boundary condition)
        if (_N == 1) {
            _first = newNode;
            _last = newNode;
            return;
        }

        Node oldFirst = _first;
        _first = newNode;
        _first.next = oldFirst;
        _first.previous = null;
        oldFirst.previous = _first;
    }

    /**
     * Adds a new item to the end of the queue.
     *
     * @param item
     *            A generic item to be added at the top of the queue
     * @throws NullPointerException
     *             if item is NULL
     */
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();

        Node newNode = new Node();
        newNode.item = item;
        ++_N;

        // if we're adding the first node then first and last must point to
        // this node (boundary condition)
        if (_N == 1) {
            _first = newNode;
            _last = newNode;
            return;
        }

        Node oldLast = _last;
        _last = newNode;
        oldLast.next = _last;
        _last.previous = oldLast;
        _last.next = null;
    }

    /**
     * Removes and returns the item at the front of the queue, the number of
     * items in the queue will be decremented by one.
     *
     * @return The generic item at the front of the queue
     * @throws NoSuchElementException
     *             if attempted to remove from an empty queue
     */
    public Item removeFirst() {
        if (_N == 0) {
            throw new java.util.NoSuchElementException();
        }

        Node removedNode = _first;
        Item removedItem = removedNode.item;
        _first = _first.next;
        if (_first != null) {
            _first.previous = null;
        }

        else {
            _last = null;
        }

        removedNode = null;
        _N--;
        return removedItem;
    }

    /**
     * Removes and returns the item at the end of the queue, the number of items
     * in the queue will be decremented by one.
     *
     * @return The generic item at the end of the queue
     * @throws NoSuchElementException
     *             if attempted to remove from an empty queue
     */
    public Item removeLast() {
        if (_N == 0) {
            throw new java.util.NoSuchElementException();
        }

        Node removedNode = _last;
        Item removedItem = removedNode.item;

        _last = _last.previous;
        if (_last != null) {
            _last.next = null;
        }
        
        else {
            _first = null;
        }

        removedNode = null;
        _N--;
        return removedItem;
    }

    /**
     * @returns a forward iterator over the items in the Deque at the end of the
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // internal iterator class
    private class DequeIterator implements Iterator<Item> {

        private Node _currentNode = null;

        public DequeIterator() {
            _currentNode = _first;
        }

        public boolean hasNext() {
            return _currentNode != null;
        }
        
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (_currentNode == null) {
                throw new java.util.NoSuchElementException();
            }

            Item returnedItem = _currentNode.item;
            _currentNode = _currentNode.next;
            return returnedItem;
        }
    }

    public static void main(String[] args) {
        // unit testing
    }
}
