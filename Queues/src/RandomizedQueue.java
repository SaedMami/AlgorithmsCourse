import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // data
    private Item[] _array; // an internal array to represent the queue, it will be
                   // resized as needed
    private int _N; // number of elements in the queue
    private int _nullCount;

    /**
     * Initializes an empty randomized queue.
     */
    public RandomizedQueue() {
        _array = (Item[]) new Object[2];
        _N = 0;
        _nullCount = 0;
        
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
     * Adds a new item to the queue
     * 
     * @param item
     *            the item you want to add to the queue
     * @throws java.lang.NullPointerException
     *             if you try to add a null item
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();
        if (_array.length == _N + _nullCount) {
            resize(2 * _array.length);
        }
        _array[_nullCount + _N++] = item;
    }

    /**
     * @return a random item from the queue after it removes it from the queue
     * @throws java.util.NoSuchelementException
     *             if we attempt to remove from an empty queue
     */
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        Item removed = null;
        int randomIndex = 0;
        do {
            randomIndex = StdRandom.uniform(_N + _nullCount);
            removed = _array[randomIndex];
        } while (removed == null);

        _array[randomIndex] = null; // avoid loitering
        _nullCount++;
        --_N;
        

        // resize the array of necessary
        if (_N > 0 && _N == _array.length / 4) {
            resize(_array.length / 2);
        }
        return removed;
    }

    /**
     * @return a random item from the queue without removing it from the queue
     */
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item sample = null;
        int randomIndex = 0;
        do {
            randomIndex = StdRandom.uniform(_N + _nullCount);
            sample = _array[randomIndex];
        } while (sample == null);
        
        return sample;
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private Item[] _iterArray; // each iterator keeps an independent copy of
                                   // the original
                                   // array but in random order
        private int _currentIndex;

        RandomQueueIterator() {
            _iterArray = (Item[]) new Object[_N];
            int j = 0;
            for (int i = 0; i < _N + _nullCount; i++) {
                if (_array[i] == null) {
                    continue;
                }
                _iterArray[j++] = _array[i];
            }
            StdRandom.shuffle(_iterArray);
            _currentIndex = 0;
        }

        public boolean hasNext() {
            return _currentIndex < _iterArray.length;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return _iterArray[_currentIndex++];
        }
        
        public void remove() { throw new UnsupportedOperationException(); }
    }

    /**
     * @return an iterator over the items in the queue in random order
     */
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    public static void main(String[] args) {
        // unit testing
    }

    // utility function to resize the array and copy the old array to the new
    // one
    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        int j = 0;
        for (int i = 0; i < _N + _nullCount; ++i) {
            if (_array[i] == null) {
                continue;
            }
            newArray[j++] = _array[i];
            _array[i] = null; // avoid loitering
        }
        _array = newArray;
        _nullCount = 0;
    }
}
