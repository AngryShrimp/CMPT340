--Keenan Johnstone
--Cmpt340 Assignment 3
--Mar 7, 2016

--Unfold defined by the assignment
unfold :: (t -> Bool) -> (t -> a) -> (t -> t) -> t -> [a]
unfold p h t x | p x       = []
               | otherwise = h x : unfold p h t (t x)


--Part A
map :: (a -> b) -> [a] -> [b]
map f = unfold null (f . head) tail

--Part B
iterate :: (a -> a) -> a -> [a]
iterate f = unfold (const False) id f