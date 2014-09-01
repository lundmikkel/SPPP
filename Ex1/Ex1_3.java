package dk.itu.sppp.ex1;

/**
 * @author Hyllekilde
 * Exercise 1.3
 * 
 * Question 1:
 * 1. t1.print()
 * 2. t2.print()
 * 3. t1 sleeps after printing '-'
 * 4. t2 print '-' while t1 sleeps
 * 5. t1 print '|' after catching an interruptedException
 * 6. t2 print '-' after sleep or when catching an interruptedException
 * This will print ||--
 * 
 * Question 2:
 * Making print() synchronized prevents thread interference so the print method will be atomic.
 * The intrinsic lock of the Printer object can only be held by one thread and thus only one thread
 * can execute the method at a given time.
 */
public class Ex1_3 {
	public static void main(String[] args) {
		final Thread t1 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					synchronized (this) {
						Printer.print();
					}
				}
			}
		});
		final Thread t2 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					synchronized (this) {
						Printer.print();
					}
				}
			}
		});
		t1.run();
		t2.run();
	}
}

class Printer {
	public static void print() {
		System.out.print("-");
		try {
			Thread.sleep(50);
		} catch (InterruptedException exn) {
			
		}
		System.out.print("|");
		}
}
