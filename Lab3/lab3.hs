--Lab3
--Lambda expressions in haskell

f :: Double -> Double -> Double -> Double
f = ( \a -> ( \b -> ( \c -> ((4*a*c)/(2*b)) )))