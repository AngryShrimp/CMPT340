--Sequence.hs



--
length1 :: Integer -> Integer
length1 n = if (n == 1)         then 1
            else if (n `mod` 2 == 0) then 1 + (length1 (n `div` 2))
            else                     1 + (length1 (3*n+1))

--length2 :: Integer -> Integer
--length2 n | (n == 1)         = 1
--          | (n `mod` 2 == 0) = 1 + (length2 (n `div` 2))
--          | (n `mod` 2 == 1) = 1 + (length2 (3*n+1))
--

length3 :: Integer -> Integer
length3 1 = 1
length3 n = oddOrEven (n `mod` 2)
    where oddOrEven 0 = 1 + (length1 (n `div` 2))
          oddOrEven 1 = 1 + (length1 (3*n+1))