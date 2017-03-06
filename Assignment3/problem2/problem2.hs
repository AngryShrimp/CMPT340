--Keenan Johnstone
--Cmpt340 Assignment 3
--Mar 7, 2016

altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
altMap f h []           = []
altMap f h [x]          = [f x]
altMap f h (x : y : xs) = f x : h y : altMap f h xs 