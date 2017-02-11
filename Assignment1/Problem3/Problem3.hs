--Keenan Johnstone
--Cmpt340 Assignment 1
--Jan 31, 2016

--luhnDouble
luhnDouble :: Integer -> Integer
luhnDouble n = if (n*2 > 9) then (2*n) - 9
               else              2*n

--luhn
luhn :: Integer -> Integer -> Integer -> Integer -> Bool
luhn a b c d = if ((luhn4Sum `mod`10) == 0) then True
               else                              False
    where luhn4Sum = (luhnDouble a)+b+(luhnDouble c)+d