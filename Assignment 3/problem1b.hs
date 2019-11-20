greatest :: (a -> Int) -> [a] -> a
greatest_index :: (a -> Int) -> [a] -> Int -> Int -> Int -> Int

-- Returns index of maximum element
greatest_index func [x] maxi maxIndex currIndex = 
    -- Base case for recursion
    if func x > maxi
        then currIndex
        else maxIndex

greatest_index func (x:xs) maxi maxIndex currIndex = 
    -- If new maximum, update maximum, else continue iterating list
    if func x > maxi
        then greatest_index func xs (func x) currIndex (currIndex+1)
        else greatest_index func xs maxi maxIndex (currIndex+1)

-- Returns maximum element using the index returned by 'greatest_index'
greatest func l = l !! (greatest_index func l 0 0 0)

-- main = print(greatest sum [[-1,3,4], [2,5], [2]])
-- main = print(greatest length ["the", "quick", "brown", "fox"])
main = print(greatest id [51,32,3])
