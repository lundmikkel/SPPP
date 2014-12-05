// For week 10 -- this version includes solutions to exercises
// sestoft@itu.dk * 2014-11-05

// Compile and run like this:
//   javac -cp ~/lib/multiverse-core-0.7.0.jar IntToDouble.java Timer.java TestStmMap.java
//   java -cp ~/lib/multiverse-core-0.7.0.jar:. TestStmMap

// For the Multiverse library:
import org.multiverse.api.LockMode;
import org.multiverse.api.Txn;
import org.multiverse.api.references.*;
import org.multiverse.api.StmUtils;
import org.multiverse.api.callables.TxnVoidCallable;
import static org.multiverse.api.StmUtils.*;

import java.util.Random;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


public class TestStmMap {
    public static void main(String[] args) {
        Benchmark.SystemInfo();
        //testAllMaps();
        exerciseAllMaps();
    }

    private static double exerciseMap(int threadCount, int perThread, int range, final OurMap<Integer, String> map) {
        Thread[] threads = new Thread[threadCount];
        for (int t=0; t<threadCount; t++) {
            final int myThread = t;
            threads[t] = new Thread(new Runnable() { public void run() {
                Random random = new Random(37 * myThread + 78);
                for (int i=0; i<perThread; i++) {
                    Integer key = random.nextInt(range);
                    if (!map.containsKey(key)) {
                        // Add key with probability 60%
                        if (random.nextDouble() < 0.60) 
                            map.put(key, Integer.toString(key));
                    } 
                    else // Remove key with probability 2% and reinsert
                        if (random.nextDouble() < 0.02) {
                            map.remove(key);
                            map.putIfAbsent(key, Integer.toString(key));
                        }
                }
                final AtomicInteger ai = new AtomicInteger();
                map.forEach(new Consumer<Integer,String>() { 
                        public void accept(Integer k, String v) {
                            ai.getAndIncrement();
                }});
                // System.out.println(ai.intValue() + " " + map.size());
            }});
        }
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
        // map.reallocateBuckets();
        try {
            for (int t=0; t<threadCount; t++) 
                threads[t].join();
        } catch (InterruptedException exn) { }
        return map.size();
    }

    private static void exerciseAllMaps() {
        final int bucketCount = 100_000, threadCount = 16;
        final int iterations = 10_600_000, perThread = iterations / threadCount;
        final int range = 100_000;
        System.out.println(
            Mark7(
                String.format("%-21s %d", "StmMap", threadCount),
                new IntToDouble() { public double call(int i) {
                    return exerciseMap(threadCount, perThread, range, new StmMap<Integer,String>(bucketCount));
                }}
            )
        );
    }

    // Very basic sequential functional test of a hash map.  You must
    // run with assertions enabled for this to work, as in 
    //   java -ea TestStmMap
    private static void testMap(final OurMap<Integer, String> map) {
        System.out.printf("%n%s%n", map.getClass());
        assert map.size() == 0;
        assert !map.containsKey(117);
        assert !map.containsKey(-2);
        assert map.get(117) == null;
        assert map.put(117, "A") == null;
        assert map.containsKey(117);
        assert map.get(117).equals("A");
        assert map.put(17, "B") == null;
        assert map.size() == 2;
        assert map.containsKey(17);
        assert map.get(117).equals("A");
        assert map.get(17).equals("B");
        assert map.put(117, "C").equals("A");
        assert map.containsKey(117);
        assert map.get(117).equals("C");
        assert map.size() == 2;
        map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));
        assert map.remove(117).equals("C");
        assert !map.containsKey(117);
        assert map.get(117) == null;
        assert map.size() == 1;
        assert map.putIfAbsent(17, "D").equals("B");
        assert map.get(17).equals("B");
        assert map.size() == 1;
        assert map.containsKey(17);
        assert map.putIfAbsent(217, "E") == null;
        assert map.get(217).equals("E");
        assert map.size() == 2;
        assert map.containsKey(217);
        assert map.putIfAbsent(34, "F") == null;
        map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));
        // map.reallocateBuckets();
        assert map.size() == 3;
        assert map.get(17).equals("B") && map.containsKey(17);
        assert map.get(217).equals("E") && map.containsKey(217);
        assert map.get(34).equals("F") && map.containsKey(34);
        map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));    
        // map.reallocateBuckets();
        assert map.size() == 3;
        assert map.get(17).equals("B") && map.containsKey(17);
        assert map.get(217).equals("E") && map.containsKey(217);
        assert map.get(34).equals("F") && map.containsKey(34);
        map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));    
    }

    private static void testAllMaps() {
        testMap(new StmMap<Integer,String>(25));
    }

    // --- Benchmarking infrastructure ---

    // NB: Modified to show microseconds instead of nanoseconds

    public static double Mark7(String msg, IntToDouble f) {
        int n = 10, count = 1, totalCount = 0;
        double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
        do { 
            count *= 2;
            st = sst = 0.0;
            for (int j=0; j<n; j++) {
                Timer t = new Timer();
                for (int i=0; i<count; i++) 
                    dummy += f.call(i);
                runningTime = t.check();
                double time = runningTime * 1e6 / count; // microseconds
                st += time; 
                sst += time * time;
                totalCount += count;
            }
        } while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
        double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
        System.out.printf("%-25s %15.1f us %10.2f %10d%n", msg, mean, sdev, count);
        return dummy / totalCount;
    }
}

interface Consumer<K,V> {
    void accept(K k, V v);
}

interface OurMap<K,V> {
    boolean containsKey(K k);
    V get(K k);
    V put(K k, V v);
    V putIfAbsent(K k, V v);
    V remove(K k);
    int size();
    void forEach(Consumer<K,V> consumer);
    // void reallocateBuckets();
}

// ----------------------------------------------------------------------
// A hashmap that permits thread-safe concurrent operations, based on
// software transactional memory.

class StmMap<K,V> implements OurMap<K,V> {
    private final TxnRef<TxnRef<ItemNode<K,V>>[]> buckets;
    private final TxnInteger size;

    public StmMap(int bucketCount) {
        final TxnRef<ItemNode<K,V>>[] buckets = makeBuckets(bucketCount);
        this.buckets = StmUtils.<TxnRef<ItemNode<K,V>>[]>newTxnRef(buckets);
        this.size = newTxnInteger(0);
    }

    @SuppressWarnings("unchecked") 
    private static <K,V> TxnRef<ItemNode<K,V>>[] makeBuckets(int size) {
        // Java's @$#@?!! type system requires "unsafe" cast here:
        final TxnRef<ItemNode<K,V>>[] buckets = (TxnRef<ItemNode<K,V>>[])new TxnRef[size];
        for (int hash=0; hash<buckets.length; hash++)
            buckets[hash] = StmUtils.<ItemNode<K,V>>newTxnRef();
        return buckets;
    }

    // Protect against poor hash functions and make non-negative
    private static <K> int getHash(K k) {
        final int kh = k.hashCode();
        return (kh ^ (kh >>> 16)) & 0x7FFFFFFF;  
    }

    // Return true if key k is in map, else false
    public boolean containsKey(K k) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final int hash =   getHash(k),
                  bucket = hash % bs.length;
        return ItemNode.search(bs[bucket].atomicWeakGet(), k, null);
    }

    // Return value v associated with key k, or null
    public V get(K k) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final int hash =   k.hashCode(),
                  bucket = hash % bs.length;
        final Holder<V> holder = new Holder<V>();
        return ItemNode.search(bs[bucket].atomicWeakGet(), k, holder) ? holder.get() : null;
    }

    public int size() {
        return size.atomicWeakGet();
    }

    // Put v at key k, or update if already present.  
    public V put(K k, V v) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final int hash =   k.hashCode(),
                  bucket = hash % bs.length;
        final Holder<V> old = new Holder<V>();

        atomic(() -> {
            final ItemNode<K,V> node = bs[bucket].get(),
                                newNode = ItemNode.delete(node, k, old);
            bs[bucket].set(new ItemNode<K,V>(k, v, newNode));
            if (newNode == node)
                size.increment();
        });

        return old.get();
    }

    // Put v at key k only if absent.  
    public V putIfAbsent(final K k, final V v) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final int hash =   k.hashCode(),
                  bucket = hash % bs.length;
        final Holder<V> old = new Holder<V>();

        atomic(() -> {
            if (!ItemNode.search(bs[bucket].get(), k, old)) {
                bs[bucket].set(new ItemNode<K,V>(k, v, bs[bucket].get()));   
                size.increment(); 
            }
        });

        return old.get();
    }

    // Remove and return the value at key k if any, else return null
    public V remove(K k) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final int hash =   k.hashCode(),
                  bucket = hash % bs.length;
        final Holder<V> old = new Holder<V>();

        atomic(() -> {
            final ItemNode<K,V> node = bs[bucket].get();
            bs[bucket].set(ItemNode.delete(node, k, old));
            if (node != bs[bucket])
                size.decrement();
        });
        
        return old.get();
    }

    // Iterate over the hashmap's entries one bucket at a time.  Since a
    // reallocate does not affect the old buckets table, and item node
    // lists are immutable, only visibility is needed, no transactions.
    // This is good, because calling a consumer inside an atomic seems
    // suspicious.
    public void forEach(Consumer<K,V> consumer) {
        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        for (int hash = 0; hash < bs.length; ++hash) {
            ItemNode<K,V> node = bs[hash].atomicWeakGet();
            while (node != null) {
                consumer.accept(node.k, node.v);
                node = node.next;
            }
        }
    }

    // CONCEPT CODE FOR 10.3.5

    private final TxnBoolean isReallocating = newTxnBoolean(false);

    public V blocksWhileReallocating(K k) {
        return atomic(() -> {
            if (isReallocating.get())
                retry();
        
            // DO SOME WORK HERE...
            return null; // RETURN THE PROPER VALUE HERE
        });
    }

    public void reallocateBuckets() { 
        atomic(() -> {
            if (isReallocating.get())
                return;
            else
                isReallocating.set(true);
        });

        final TxnRef<ItemNode<K,V>>[] bs = buckets.atomicWeakGet();
        final TxnRef<ItemNode<K,V>>[] newBuckets = makeBuckets(bs.length * 2);
        // DO THE REALLOCATION HERE...
        buckets.atomicSet(newBuckets);
        
        isReallocating.atomicSet(false);
    }

    // CONCEPT CODE END

    static class ItemNode<K,V> {
        private final K k;
        private final V v;
        private final ItemNode<K,V> next;
        
        public ItemNode(K k, V v, ItemNode<K,V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        // These work on immutable data only, no synchronization or transaction needed.

        public static <K,V> boolean search(ItemNode<K,V> node, K k, Holder<V> old) {
            while (node != null) 
                if (k.equals(node.k)) {
                    if (old != null) 
                        old.set(node.v);
                    return true;
                } else 
                    node = node.next;
            return false;
        }
        
        public static <K,V> ItemNode<K,V> delete(ItemNode<K,V> node, K k, Holder<V> old) {
            if (node == null) 
                return null; 
            else if (k.equals(node.k)) {
                old.set(node.v);
                return node.next;
            } else {
                final ItemNode<K,V> newNode = delete(node.next, k, old);
                if (newNode == node.next) 
                    return node;
                else 
                    return new ItemNode<K,V>(node.k, node.v, newNode);
            }
        }
    }
    
    // Object to hold a "by reference" parameter.  For use only on a
    // single thread, so no need for "volatile" or synchronization or
    // transactions.

    static class Holder<V> {
        private V value;
        public V get() { 
            return value; 
        }
        public void set(V value) { 
            this.value = value;
        }
    }
}