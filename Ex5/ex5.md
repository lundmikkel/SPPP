Exercise 5.1
============

Question 1
----------
    # OS:   Mac OS X; 10.9.5; x86_64
    # JVM:  Oracle Corporation; 1.8.0_20
    # CPU:  2,3 GHz Intel Core i7
    # RAM:  16 GB 1600 MHz DDR3
    # Date: 2014-09-26T10:20:13+0200
    countSequential                   12682,3 us     574,48         32
    9592.0
    countParTask1     32               5685,7 us     733,98         64
    9592.0
    countParTask2     32               5015,9 us     239,13         64
    9592.0
    countParTask3     32               4339,1 us     247,44         64
    9592.0


Question 2
----------
    # OS:   Mac OS X; 10.9.5; x86_64
    # JVM:  Oracle Corporation; 1.8.0_20
    # CPU:  2,3 GHz Intel Core i7
    # RAM:  16 GB 1600 MHz DDR3
    # Date: 2014-09-26T10:18:00+0200
    countSequential                   12536,9 us     797,52         32
    9592.0
    countParTask1     32               3887,8 us      89,64        128
    9592.0
    countParTask2     32               4243,8 us     293,69         64
    9592.0
    countParTask3     32               4520,2 us     164,92         64
    9592.0


Question 3
----------

Interactive graphs:

- [CachedThreadPool - countParTask1](http://goo.gl/KOOXIL)
- [CachedThreadPool - countParTask2](http://goo.gl/V7sPpY)
- [WorkStealingPool - countParTask1](http://goo.gl/SAOIoj)
- [WorkStealingPool - countParTask2](http://goo.gl/De5WKx)

(Static images and data points can be found in the <code>benchmarks</code> 
folder)

Question 4
----------


The execution times are almost the same on our machine. It seems the thread 
version runs the fastest with a count up to 8 (the machine has 8 cores). After 8 
it seems like the numbers are almost the same. The small variations are too 
small to give a proper answer.
[Comparison](http://goo.gl/olRMZN)

Question 5
----------


Exercise 5.2
============

Question 1
----------


Question 2
----------


Question 3
----------


Question 4
----------


Question 5
----------


Exercise 5.3
============

Question 1
----------
Runs:
1. 12.933446358
2. 11.08861516
3.  8.806998804
4. 11.117534792
5.  7.94627111


Question 4
----------
A parallel run of tasks is only as past as the slowest executed task. This means
if 22 of the urls take 1 second to execute and the last takes 3 seconds, then
the total execution time will be 3 seconds.

Runs:
1. 2.886404326
2. 1.68489287
3. 1.764366912
4. 1.797240872
5. 1.731433001


Exercise 5.4
============

Question 1
----------


Question 2
----------


Question 3
----------


Question 4
----------


Question 5
----------
