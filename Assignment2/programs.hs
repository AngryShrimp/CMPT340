--Keenan Johnstone
--Cmpt340 Assignment 2
--Feb 14, 2016


--Problem 2

--Problem 3



--Problem 4

shuffle :: [a] -> [a] -> [a]
shuffle []     ys = ys
shuffle (x:xs) ys = x:(merge ys xs)

shuffleTest1 :: [Int]
shuffleTest1 = [1,2,3,4,5]

shuffleTest2 :: [Int]
shuffleTest2 = [51, 52, 53, 54, 55]

shuffleTest3 :: [Char]
shuffleTest3 = ['a', 'b', 'c']

shuffleTest4 :: [Char]
shuffleTest4 = ['m', 'n', 'o']

--Problem 5

--Problem 6

--Problem 7