import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Subset {

    public static void main(String[] args) {
        // read the size of the subset from standard input
        int k = 0;
        if (args.length > 0) {
            k = Integer.parseInt(args[0]);
        }
        
        // create a randomized queue
        RandomizedQueue<String> queue = new RandomizedQueue<String>(); 
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        
        int printedStrings = 0;
        // random iterating 
        for (String s : queue) {
            if (printedStrings >= k)
                break;
            StdOut.println(s);
            printedStrings++;
        }
    }

}
