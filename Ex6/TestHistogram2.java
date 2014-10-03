import javax.annotation.concurrent.GuardedBy;
import java.util.Arrays;

interface Histogram {
    public void increment(int item);
    public int getCount(int item);
    public int[] getBuckets();
    public int getSpan();
}

class Histogram2 implements Histogram {
    @GuardedBy("this")
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