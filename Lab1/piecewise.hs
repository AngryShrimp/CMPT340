--Piecewise.hs

--Piecewise1
p1 :: Integer -> Integer
p1 x | x < 1                = x + 4
     | x >= 1 && x < 4      = 2
     | otherwise            = x - 5