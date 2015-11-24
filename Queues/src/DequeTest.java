
public class DequeTest {

    public static void main(String[] args) {
        Deque<Integer> myQue = new Deque<Integer>();
        myQue.isEmpty();
        myQue.isEmpty();
        myQue.addFirst(2);
        myQue.removeLast();  //      ==> 2
        myQue.addFirst(4);
        myQue.addFirst(5);
        myQue.addFirst(6);
        myQue.isEmpty();
        myQue.addFirst(8);
        myQue.removeLast();  //    ==> 4
        myQue.addFirst(10);
        myQue.removeLast();
        
        PrintQueue(myQue);
        int last = myQue.removeLast();
        System.out.println("removed LAST item:" + last);
        PrintQueue(myQue);
        int first = myQue.removeFirst();
        PrintQueue(myQue);
        System.out.println("removed FIRST item:" + first);
        
        for (int i = 0; i < 8; i++) {
            myQue.removeFirst();
        }
        
        if (myQue.size() == 0) {
            System.out.println("the que is empty");
        }
    }

    /**
     * Alternately push number 0-9 at the front and at the back of the Deque eg:
     * first 0 will be pushed on front, 1 pushed on back, 2 pushed on font and
     * so on. And then print the results on the screen. result should be 
     * 0 2 4 6 8 9 7 5 3 1
     */
    public static Deque<Integer> alternatePushTest() {
        Deque<Integer> myDeque = new Deque<Integer>();
        for (int i = 0; i < 10; ++i) {
            if (i % 2 == 0) {
                myDeque.addFirst(i);
            }
            
            else {
                myDeque.addLast(i);
            }
        }
        
        return myDeque;
    }
    
    public static void PrintQueue(Deque<Integer> q) {
        for (Integer i : q) {
            System.out.print(i);
        }
        System.out.println();
    }

}
