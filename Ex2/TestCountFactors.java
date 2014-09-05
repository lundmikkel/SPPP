// For week 2
// sestoft@itu.dk * 2014-08-29
import java.util.concurrent.atomic.AtomicInteger;

class TestCountFactors {
    public static void main(String[] args) {
        int range = 50_000_000;
        // sequential(range);
        //countParallelN(range, 10);
        countParallelNAtomicInteger(range, 10);
    }

    private static void countSequential(int range) {
        int count = 0;
        for (int p=0; p<range; p++)
            count += countFactors(p);
        System.out.printf("Total number of factors is %9d%n", count);
    }

    private static void countParallelN(int range, int threadCount) {
        final int perThread = range / threadCount;
        final MyAtomicInteger mai = new MyAtomicInteger();

        Thread[] threads = new Thread[threadCount];
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            threads[t] = new Thread(
                () -> {
                    for (int i=from; i<to; i++)
                        mai.addAndGet(countFactors(i));
                }
            );
        }
        for (int t=0; t<threadCount; t++) 
            threads[t].start();

        try {
            for (int t=0; t<threadCount; t++) 
                threads[t].join();
        }
        catch (InterruptedException exn)
        {}
        System.out.printf("Total number of factors is %9d%n", mai.get());
     }

    private static void countParallelNAtomicInteger(int range, int threadCount) {
        final int perThread = range / threadCount;
        final AtomicInteger ai = new AtomicInteger();

        Thread[] threads = new Thread[threadCount];
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            threads[t] = new Thread(
                () -> {
                    for (int i=from; i<to; i++)
                        ai.getAndAdd(countFactors(i));
                }
            );
        }
        for (int t=0; t<threadCount; t++) 
            threads[t].start();

        try {
            for (int t=0; t<threadCount; t++) 
                threads[t].join();
        }
        catch (InterruptedException exn)
        {}
        System.out.printf("Total number of factors is %9d%n", ai.get());
     }

    public static int countFactors(int p) {
        if (p < 2) 
            return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
            if (p % k == 0) {
                factorCount++;
                p /= k;
            }
            else
                k++;
        }
        return factorCount;
    }
}

class MyAtomicInteger {
    int value;
    public synchronized int addAndGet(int amount) {
        return value += amount;
    }

    public synchronized int get() {
        return value;
    }
}