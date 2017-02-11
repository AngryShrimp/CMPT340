--Keenan Johnstone
--Cmpt340 Assignment 1
--Jan 31, 2016

--Conditional Expression
fastExp1 :: Integer -> Integer -> Integer
fastExp1 n k = if (k `mod` 2 == 0) then (n^(k `div` 2))^2
               else                     n*n^(k-1)

--Guarded Equations
fastExp2 :: Integer -> Integer -> Integer
fastExp2 n k 
    | (k `mod` 2 == 0) = (n^(k `div` 2))^2
    | otherwise        = n*n^(k-1)

--PAttern MAtching
fastExp3 :: Integer -> Integer -> Integer
fastExp3 n k = oddOrEven(k `mod` 2)
    where oddOrEven 0 = (n^(k `div` 2))^2
          oddOrEven 1 = n*n^(k-1)