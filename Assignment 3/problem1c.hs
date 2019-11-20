-- Define appropriate data structure
data List a = Empty | Cons a (List a)
  deriving (Show)

toList::[a] -> List a 
-- Convert data type recursively
toList [] = Empty
toList (x:xs) = Cons x (toList xs)

toHaskellList::List a -> [a]
toHaskellList Empty = []
toHaskellList (Cons x a) = x : toHaskellList a




