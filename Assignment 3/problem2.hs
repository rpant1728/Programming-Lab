import Data.List

-- Generate all substrings starting from some index
substringFromIndex [] str list = list ++ [str]
substringFromIndex (x:xs) str list = substringFromIndex xs (str ++ [x]) (list ++ [str])

-- Generate all substrings
substrings [] list = list
substrings (x:xs) list = substrings xs (list ++ map (x:) (substringFromIndex xs "" []))

-- Sort each individual substring
sort_substrings [] = []
sort_substrings (x:xs) = (sort x) : sort_substrings(xs)

sorted_substrings a = sort_substrings(substrings a [])

-- Compare a sorted substring with all other possible sorted substrings
compare_strings str [] count = count
compare_strings str (x:xs) count = 
    if str == x
        then compare_strings str xs count+1
        else compare_strings str xs count

-- Calculate answer
answer [] count = count 
answer (x:xs) count = answer xs count+(compare_strings x xs 0)

-- Returns answer. If more than two lists passed, output error
anagram_pairs (a:b:[]) = answer (sorted_substrings (a++b)) 0
anagram_pairs _ = error "Exactly 2 lists expected!"






