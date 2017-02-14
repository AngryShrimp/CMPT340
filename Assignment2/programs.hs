--Keenan Johnstone
--Cmpt340 Assignment 2
--Feb 14, 2016

--Import for problem 7
import Data.List
import Data.Ord

--Problem 2

--pairAdd was created to use as an example for testing (I didnt know any common functions that took pairs)
pairAdd :: (Int, Int) -> Int
pairAdd (a,b) = a+b

curry :: ((a,b) -> c) -> (a -> b -> c)
curry f a b = f(a,b)

uncurry :: (a -> b -> c) -> ((a,b) -> c)
uncurry f (a,b) = f a b

--Problem 3

type Mantissa = Integer
type Exponent = Integer

data MyFloat = MyFloat (Mantissa, Exponent)

test1 :: MyFloat
test1 = MyFloat(329, 2)

test2 :: MyFloat
test2 = MyFloat(670, 2)

test3 :: MyFloat
test3 = MyFloat(400, 3)

instance Show MyFloat where
    show (MyFloat (m, e)) = if (m >= 0) then "0." ++ (show m) ++ " * 10^" ++ (show e)
                            else             "-0." ++ (show (-m)) ++ " * 10^" ++ (show e)

instance Eq MyFloat where
    (==) = eqMyFloat

eqMyFloat :: MyFloat -> MyFloat -> Bool
eqMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = ((m1*(10^e1)) == (m2*(10^e2)))

instance Num MyFloat where
    (+) = addMyFloat
    (-) = subMyFloat
    (*) = mulMyFloat
    negate = negMyFloat

addMyFloat :: MyFloat -> MyFloat -> MyFloat
addMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = if (e1 == e2)     then MyFloat((m1+m2), e1)
                                                 else if (e1 > e2) then MyFloat(((m1*10^(e1-e2))+m2), e1)
                                                 else                   MyFloat(((m2*10^(e2-e1))+m1), e2)

subMyFloat :: MyFloat -> MyFloat -> MyFloat
subMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = if (e1 == e2)     then MyFloat((m1-m2), e1)
                                                 else if (e1 > e2) then MyFloat(((m1*10^(e1-e2))+m2), e1)
                                                 else                   MyFloat(((m2*10^(e2-e1))+m1), e2)

mulMyFloat :: MyFloat -> MyFloat -> MyFloat
mulMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = MyFloat((m1*m2), (e1+e2))

negMyFloat :: MyFloat -> MyFloat
negMyFloat (MyFloat(m, e)) = MyFloat((-m), e)

instance Fractional MyFloat where
    (/) = divMyFloat 

divMyFloat :: MyFloat -> MyFloat -> MyFloat
divMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = MyFloat((m1`div`m2), (e1-e2+1))

instance Ord MyFloat where
    (<=) = lteMyFloat
    (<)  = ltMyFloat
    (>=) = gteMyFloat
    (>)  = gtMyFloat

lteMyFloat :: MyFloat -> MyFloat -> Bool
lteMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = (m1 <= m2 && e1 <= e2) 

ltMyFloat :: MyFloat -> MyFloat -> Bool
ltMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = if (e1 < e2)       then True
                                                else if (e1 == e2) then (m1 < m2)
                                                else                    False

gteMyFloat :: MyFloat -> MyFloat -> Bool
gteMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = (m1 >= m2 && e1 >= e2) 

gtMyFloat :: MyFloat -> MyFloat -> Bool
gtMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = if (e1 > e2)       then True
                                                else if (e1 == e2) then (m1 > m2)
                                                else                    False

whole :: MyFloat -> Integer
whole (MyFloat(m,e)) = m `div` 10^((digitCount m)-e)

fraction :: MyFloat -> Float
fraction (MyFloat(m,e)) = (fromInteger (m `mod` 10^((digitCount m)-e))) * (10^^(negate (digitCount m) + e))


--For counting the number of digits in a number
digitCount :: Integer -> Integer
digitCount = go 1 . abs
    where
        go ds n = if n >= 10 then go (ds + 1) (n `div` 10) else ds

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
nshuffle c n = tempShuf c n ((listOfR c) ++ (listOfB c))
    where
        listOfB c = take c (repeat 'b')
        listOfR c = take c (repeat 'r')
        firstElem (xs,_) = xs
        secondElem (_,xs) = xs        
        tempShuf c n xs = if (n > 0) then tempShuf c (n-1) (shuffle (firstElem(split xs c)) (secondElem(split xs c)))
                          else            xs

--Problem 7

consecutive :: [Char] -> Int
consecutive s = length (maximumBy (comparing length) (group s))