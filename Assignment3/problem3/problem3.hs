--Keenan Johnstone
--Cmpt340 Assignment 3
--Mar 7, 2016

--From assignment 1 problem 3
luhnDouble :: Int -> Int
luhnDouble n = if (n*2 > 9) then (2*n) - 9
               else              2*n

--From Problem 2
altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
altMap f h []           = []
altMap f h [x]          = [f x]
altMap f h (x : y : xs) = f x : h y : altMap f h xs 

luhn :: [Int] -> Bool
luhn [] = False
luhn x = (((sum(altMap (luhnDouble) (*1) x)) `mod`10) == 0)