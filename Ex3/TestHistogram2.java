import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class TestHistogram2 {
    final static int range = 5_000_000;
    final static int threads = 8;
    final static int span = 25;

    public static void main(String[] args) {
        //Histogram histogram = new Histogram2(span);
        //Histogram histogram = new Histogram3(span);
        Histogram histogram = new Histogram4(span);

        countInParallel(histogram, threads);
        dump(histogram);
    }

    private static void dump(Histogram histogram) {
        int totalCount = 0;
        for (int item = 0; item < histogram.getSpan(); ++item) {
            System.out.printf("%4d: %9d%n", item, histogram.getCount(item));
            totalCount += histogram.getCount(item);
        }
        System.out.printf("      %9d%n", totalCount);
    }

    private static void countInParallel(final Histogram histogram, final int threadCount) {
        final int perThread = range / threadCount;
        final Thread[] threads = new Thread[threadCount];

        for (int t = 0; t < threadCount; ++t) {
            final int from = perThread * t;
            final int to = (t + 1 == threadCount) ? range : perThread * (t + 1);
            
            threads[t] = new Thread(() -> {
                for (int i = from; i < to; ++i) {
                    histogram.increment(countPrimeFactors(i));
                }
            });
        }

        for (int t = 0; t < threadCount; ++t)
            threads[t].start();

        try {
            for (int t = 0; t < threadCount; ++t)
                threads[t].join();
        } catch (InterruptedException exn) {}
    }

    public static int countPrimeFactors(int p) {
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

interface Histogram {
    public void increment(int item);
    public int getCount(int item);
    public int[] getBuckets();
    public int getSpan();
}

class Histogram2 implements Histogram {
    private final int[] counts;

    public Histogram2(int span) {
        counts = new int[span];
    }

    public synchronized void increment(int item) {
        ++counts[item];
    }

    public synchronized int getCount(int item) {
        return counts[item];
    }

    public synchronized int[] getBuckets() {
        return Arrays.copyOf(counts, counts.length);
    }
    
    public int getSpan() {
        return counts.length;
    }
}

class Histogram3 implements Histogram {
    private final AtomicInteger[] counts;

    public Histogram3(int span) {
        counts = new AtomicInteger[span];

        for (int i = 0; i < span; ++i)
            counts[i] = new AtomicInteger();
    }

    public void increment(int item) {
        counts[item].getAndIncrement();
    }

    public int getCount(int item) {
        return counts[item].get();
    }

    public int[] getBuckets() {
        int length = counts.length;
        int[] result = new int[length];

        synchronized (counts) {
            for (int i = 0; i < length; ++i) {
                result[i] = counts[i].get();
            }
        }
        return result;
    }
    
    public int getSpan() {
        return counts.length;
    }
}

class Histogram4 implements Histogram {
    private final AtomicIntegerArray counts;

    public Histogram4(int span) {
        counts = new AtomicIntegerArray(span);
    }

    public void increment(int item) {
        counts.getAndIncrement(item);
    }

    public int getCount(int item) {
        return counts.get(item);
    }

    public int[] getBuckets() {
        int length = counts.length();
        int[] result = new int[length];

        synchronized (counts) {
            for (int i = 0; i < length; ++i) {
                result[i] = counts.get(i);
            }
        }
        return result;
    }
    
    public int getSpan() {
        return counts.length();
    }
}