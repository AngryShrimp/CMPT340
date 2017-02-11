--Keenan Johnstone
--Cmpt340 Assignment 1
--Jan 31, 2016
import Data.List

--averageThree
averageThree :: Int -> Int -> Int -> Float
averageThree a b c = (fromIntegral a + fromIntegral b + fromIntegral c)/3

--howManyAboveAverage
howManyAboveAverage :: Int -> Int -> Int -> Int
howManyAboveAverage a b c = length $ filter (> avg3) fromInts
    where avg3 = averageThree a b c
          fromInts = [fromIntegral a,fromIntegral b,fromIntegral c]

--averageThreeInOne
averageThreeInOne :: (Int, Int, Int) -> Float
averageThreeInOne (a, b, c) = averageThree a b c

--orderTriple
orderTriple :: (Int, Int, Int) -> (Int, Int, Int)
orderTriple (a, b, c) = tripleSort(a, b, c)
    where tripleSort (a, b, c) = tuplify3(sort [a, b, c])
          tuplify3 [a, b, c] = (a, b, c)