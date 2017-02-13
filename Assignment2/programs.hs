--Keenan Johnstone
--Cmpt340 Assignment 2
--Feb 14, 2016


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
test3 = MyFloat(400 , 3)

instance Show MyFloat where
    show (MyFloat (m, e)) = "0." ++ (show m) ++ " * 10^" ++ (show e)

instance Eq MyFloat where
    (==) = eqMyFloat

eqMyFloat :: MyFloat -> MyFloat -> Bool
eqMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = ((m1*(10^e1)) == (m2*(10^e2)))

instance Num MyFloat where
    (+) = addMyFloat

addMyFloat :: MyFloat -> MyFloat -> MyFloat
addMyFloat (MyFloat(m1, e1)) (MyFloat(m2, e2)) = if (e1 == e2) then MyFloat((m1+m2), e1)
                                                 else if (e1 > e2) then MyFloat(((m1*10^(e1-e2))+m2), e1)
                                                 else MyFloat(((m2*10^(e2-e1))+m1), e2)

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