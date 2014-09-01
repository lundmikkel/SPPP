package dk.itu.sppp.ex1;
// For week 1
// sestoft@itu.dk * 2014-08-21

/**
 * @author Hyllekilde
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
 * No, the different ways of incrementing the count results in the same error. All the statements perform several
 * statements to increment count and thus are open to thread interference.
 * 
 * Question 4:
 * The final count should be 0 as both methods are synchronized and shares uses the same intrinsic lock of the
 * LongCounter object. Thus, it is not possible for them to execute the method calls at the same time as they have a
 * happen-before relationship.
 * 
 * Question 5:
 * I think the explanation for the incorrect final values of each of the four examples is given above.
 * i)	The methods will be executed simultaneously and thus will cause thread interferences.
 * 		15280, 20441, -3364
 * ii)	If only one method is synchronized the other method can be executed one or several times 
 * 		causing thread interference and a wrong final count value.
 * 		4701, 38383, 25257
 * iii)	Same as above.
 * iv)	Atomic executions. The synchonization makes them share the object's intrinsic lock
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
