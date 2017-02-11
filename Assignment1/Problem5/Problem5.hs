--Keenan Johnstone
--Cmpt340 Assignment 1
--Jan 31, 2016

compose3 :: (Double -> Double) -> (Double -> Double) -> (Double -> Double) -> (Double -> Double)
compose3 f g h x = f(g(h(x)))

