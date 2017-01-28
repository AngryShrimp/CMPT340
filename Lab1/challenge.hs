--challenge.hs

--sqrtPairs
sqrtPairs :: Float -> Float -> (Float, Float)
sqrtPairs x y = (sqrt x, sqrt y)

--keepDoubling
keepDoubling :: Integer -> Integer
keepDoubling x = until (>100) (*2) x
