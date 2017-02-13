--Keenan Johnstone
--Cmpt340 Assignment 2
--Feb 14, 2016


--Problem 2

--Problem 3



--Problem 4

shuffle :: [a] -> [a] -> [a]
shuffle []     ys = ys
shuffle (x:xs) ys = x:(shuffle ys xs)


--Problem 5

split :: [a] -> Int -> ([a], [a])
split [] _ = ([], [])
split xs 0 = ([], xs)
split (x:xs) n = (x:xs1, xs2)
    where (xs1, xs2) = split xs (n - 1)

--Problem 6

nshuffle :: Int -> Int -> [Char]
nshuffle c n = shuffle (listOfR c) (listOfB c)
    where
        listOfB c = take c (repeat 'b')
        listOfR c = take c (repeat 'r')

--Problem 7

consecutive :: [Char] -> Int
consecutive