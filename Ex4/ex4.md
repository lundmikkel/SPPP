Exercise 4.1
============

Question 1
----------
The results of the measurements of the mark* methods are placed in the
'benchmarks' folder together with the system info.

Our benchmarks are almost identical to the ones made by Peter, so we conclude
that they are very plausible.

In the Ex4_1_6 benchmarks we experienced a large standard deviation which might be
because of concurrent programs running on the computer and maybe because of
the garbage collector as it is similar to Peter's results where it does the
same.

Question 2
----------
Our results are stored in the benchmarks folder. Our benchmarks of mark7 shows
that the stronger computer (Ex4_2_1) is faster than (Ex4_2_2) as expected. It is
a little strange that the deviation of Ex4_2_2 is consistently larger than the
other. This could be because the computer running Ex4_2_2 having more programs
running and thus each of the benchmark runs doesn't have the same basis.

Exercise 4.2
============

Question 1
----------
We think that there are quite a few outbursts in standard deviation during the
different benchmarking runs. It is definitely not a steady move towards a robust
result.

Question 2
----------
The means of our benchmarking results are almost the same as in the lecture
notes with just a very small constant factor larger (~10% slower).
However, the standard deviations on the last results are very large compared to
those of the lectures notes.

Exercise 4.3
============

Question 1
----------


Question 2
----------
The visual graphs of the result is saved as Ex4_3_2.

Question 3
----------
Our benchmarking results shows that the 8-thread benchmark is the fastest. This
makes sense as the computer used have 4 cores with hyperthreading, thus, 8
processors.

Question 4
----------
There is no significant difference in the performance between AtomicLong and
LongCounter - if anything, AtmoicLong is a little bit faster.
When adequate built-in classes are available one should use them. They are most
likely optimized and thoroughly tested.

Question 5
----------


Exercise 4.4
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


Question 7
----------


Question 8
----------

