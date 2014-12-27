// For week 1
// sestoft@itu.dk * 2014-08-21

/**
 * Question 1:
 * If the increment() method is not synchronized then you cannot be sure of what the final value of count will be.
 * Two threads will be able to call the method simultaneously where one of the calls overwrites the other and thus
 * causes thread interference.
 * 
 * Question 2:
 * The lower the amount of increments the less is the risk of thread interference. The software will still be
 * wrong as it will have the risk of performing an inaccurate number of increments of count.
 * 
 * Question 3:
 * No, the different ways of incrementing the count results in the same error. The statements are all compound
 * statements (check-modify-write sequences) that must execute atomatically to be thread-safe.
 * 
 * Question 4:
 * The final count is expected to be 0. If not both methods are synchronized and share the same intrinsic lock of the
 * LongCounter object, the operations could get interleved. Though the two threads call different methods, they still
 * act on the same mutable shared state.
 * 
 * Question 5:
 * I think the explanation for the incorrect final values of each of the four examples is given above.
 * i)	The methods will be executed simultaneously and thus will cause thread interferences.
 * 		15280, 20441, -3364
 * ii)	If only one method is synchronized it is not possible to control the lock and thus the count value is not predictable.
 * 		4701, 38383, 25257
 * iii)	Same as above.
 * iv)	The synchonization makes them share the object's intrinsic lock which ensures atomic executions.
 * 		0, 0, 0 and 0
 */
public class TestLongCounterExperiments {
	public static void main(String[] args) {
		final LongCounter lc = new LongCounter();
		final int counts = 10_000_000;
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < counts; i++)
					lc.decrement();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < counts; i++)
					lc.increment();
			}
		});
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException exn) {
			System.out.println("Some thread was interrupted");
		}
		System.out.println("Count is " + lc.get() + " and should be " + 2
				* counts);
	}
}

class LongCounter {
	private long count = 0;

	public synchronized void increment() {
		count++;
	}
	
	public void decrement() {
		count--;
	}

	public long get() {
		return count;
	}
}
