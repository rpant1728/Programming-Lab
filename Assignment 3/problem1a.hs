m :: [[Integer]] -> Integer

-- If list is empty, output error
m [] = error "Empty List!"

-- else apply in-built function to the list sums 
--(sum of each list computed using in-built function sum for each item of list)
m list = product $ map sum list