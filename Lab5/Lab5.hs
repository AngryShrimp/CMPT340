--Lab5
shuffle :: [a] -> [a] -> [a]
shuffle []     ys = ys
shuffle (x:xs) ys = x:(shuffle ys xs)