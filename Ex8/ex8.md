Exercise 7.1
============

Question 1
----------
We have implemented <code>V get(K k)</code> similarly to SynchronizedMap where
we only lock on the relevant stripe.

Question 2
----------
We iterate through each stripe to get its count. We lock the specific stripe
when adding its count to the total count. We do this to ensure visibility.

Question 3
----------
This is implemented in the same way as <code>put</code> just without replacing
any previous values.

Question 4
----------
This is implemented as in <code>SynchronizedMap</code>, but where we only lock
the relevant stripe.

Question 5
----------
We have implemented the <code>forEach</code> by first making a local reference
to the array stored in <code>buckets</code> to avoid iterating over the same 
<code>ItemNode</code> several times if <code>reallocateBuckets</code> is called
simultaneously. Then we iterate through the stripes and lock each of them and
iterate its buckets. This will acquire each lock only once.

Question 6
----------


Question 7
----------


Question 8
----------


Question 9
----------


Question 10
----------


Exercise 7.2
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


Question 6
----------


Exercise 8.3
============

Question 1
----------


Question 2
----------


Question 3
----------

