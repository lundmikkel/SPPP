// For week 6
// sestoft@itu.dk * 2014-09-29

// The Dining Philosophers problem, due to E.W. Dijkstra 1965.  Five
// philosophers (threads) sit at a round table on which there are five
// forks (shared resources), placed between the philosophers.  A
// philosopher alternatingly thinks and eats spaghetti.  To eat, the
// philosopher needs exclusive use of the two forks placed to his left
// and right, so he tries to lock them.  

// Both the places and the forks are numbered 0 to 5.  The fork to the
// left of place p has number p, and the fork to the right has number
// (p+1)%5.

// This solution is wrong; it will deadlock after a while.
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class TestPhilosophers {
    public static void main(String[] args) {
        final int count = Integer.parseInt(args[0]);
        
        final AtomicInteger[] counters = new AtomicInteger[count];
        final Fork[] forks = new Fork[count];
        final Thread[] threads = new Thread[count];

        for (int place = 0; place < count; ++place) {
            counters[place] = new AtomicInteger();
            forks[place]    = new Fork();
            threads[place]  = new Thread(new Philosopher(forks, place, counters[place]));
        }

        for (int place = 0; place < forks.length; ++place)
            threads[place].start();

        while (true) {
            try { Thread.sleep(10_000); } catch (InterruptedException exn) {}
            // Locking both accounts is necessary to avoid reading the
            // balance in the middle of a transfer.
            for (int place = 0; place < count; ++place)
                System.out.printf("%10d ", counters[place].get());
            System.out.println();
        }
    }
}

class Philosopher implements Runnable {
    private final Fork[] forks;
    private final int place, left, right;
    private AtomicInteger counter;

    public Philosopher(Fork[] forks, int place, AtomicInteger counter) {
        this.counter = counter;
        this.forks = forks;
        this.place = place;
        this.left = place;
        this.right = (place + 1) % forks.length;
    }

    public void run() {
        //*
        while (true) {
            // Avoid deadlock by assigning the lowest indexed fork to left
            if (left < right) {
                synchronized (forks[left]) {
                    synchronized (forks[right]) {
                        // Eat
                        //System.out.print(place + " ");
                        counter.getAndIncrement();
                    }
                }
            }
            else {
                synchronized (forks[right]) {
                    synchronized (forks[left]) {
                        // Eat
                        //System.out.print(place + " ");
                        counter.getAndIncrement();
                    }
                }
            }
            // Think
            try { Thread.sleep(10); }
            catch (InterruptedException exn) { }
        }
        /*/
        Random random = new Random();
        while (true) {
            // Try to get the locks
            while (true) {
                if (forks[left].tryLock()) {
                    try {
                        if (forks[right].tryLock()) {
                            try {
                                // Eat
                                //System.out.print(place + " ");
                                counter.getAndIncrement();
                                break;
                            }
                            finally {
                                forks[right].unlock();
                            }
                        }
                    }
                    finally {
                        forks[left].unlock();
                    }
                }
                try { Thread.sleep(0, random.nextInt(500)); }
                catch (InterruptedException exn) { }
            }
            // Think
            try { Thread.sleep(10); }
            catch (InterruptedException exn) { }
        }
        //*/
    }
}

class Fork extends ReentrantLock {}