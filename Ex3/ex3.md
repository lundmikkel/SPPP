Exercise 3.1
============

Question 1
----------
- The <code>counts</code> field must be final to ensure that it will not get
modified.
- The <code>increment</code> method must be synchronized to ensure atomicity.
- The <code>getCount</code> method must be synchronized to ensure that we do not
read stale values.
- The <code>getSpan</code> method does not need to be synchronized as the 
<code>counts</code> field is final and thus cannot be modified ensuring that no 
stale lenght value is ever read.


Question 3
----------
We can remove synchronized from the <code>increment</code> and 
<code>getCount</code> methods as the AtomicInteger class ensures that no threads
will read stale values.


Question 5
----------

Histogram2:
We make the <code>getBuckets</code> method synchronized and thus
retrieves a fixed snapshot of the histogram as no other operations can be done
concurrently.

Histogram3: 
It is not possible to give a fixed snapshot without locking the operations in
the <code>increment</code> method.

Histogram4:
While copying the AtomicIntegerArray we use the lock of the 
AtomicIntegerArray to ensure that we return a fixed snapshot.