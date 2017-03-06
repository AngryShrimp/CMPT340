--Keenan Johnstone
--Cmpt340 Assignment 3
--Mar 7, 2016

perfects n = [x | x <- [1..n], factors x == x]
    where factors n = sum [x | x <- [1..n], n `mod` x == 0, x /= n]