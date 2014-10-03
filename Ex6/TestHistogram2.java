import javax.annotation.concurrent.GuardedBy;
import java.util.Arrays;
import java.util.stream.IntStream;

interface Histogram {
    public void increment(int item);
    public int getCount(int item);
    public int[] getBuckets();
    public int getSpan();
    public void addAll(Histogram hist);
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

    /**
     * Avoids deadlocks by never having the lock on two objects at the same time
     */
    public void addAll(Histogram that) {
        if (this.getSpan() != that.getSpan())
            throw new RuntimeException("The histograms must be of equal length.");

        // Locks on that while getting the buckets
        final int[] buckets = that.getBuckets();
        // No longer holds the lock on that

        // Locks on this while setting the buckets
        synchronized (this) {
            for (int i = 0; i < counts.length; ++i)
                counts[i] += buckets[i];
        }
        // No longer holds the lock on this
    }

    public void madAll(Histogram2 that) {
        if (this.getSpan() != that.getSpan())
            throw new RuntimeException("The histograms must be of equal length.");

        // Locks on this while setting the buckets
        synchronized (this) {
            for (int i = 0; i < counts.length; ++i)
                this.counts[i] += that.counts[i];
        }
        // No longer holds the lock on this
    }
}