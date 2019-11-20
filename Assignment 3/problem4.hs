import Data.Array
import Data.List.Split

type Board = Array (Int, Int) Int

-- Checks array elements are valid and no repetitions exist
validate_array [] currList = True
validate_array (x:xs) currList = if ((x >= 0) && (x <=4) && ((x > 0 && not (elem x currList)) || (x == 0)))
                                  then validate_array xs (x: currList)
                                  else False

validate_input [] = True
validate_input (x:xs) = do
                          if (validate_array x [])
                            then validate_input xs
                            else False

-- sudoku :: [[Int]] -> IO()
sudoku puzzle = do
    if (validate_input puzzle)
      then do
        let solution = solve (array ((0, 0), (3, 3)) $ puzzleAssign puzzle)
        printSudoku solution
      else print "Wrong Input!"

-- Solves sudoku, and checks if solution is valid
solve :: Board -> Maybe Board
solve = isValid . solutions

-- Generate all possible sudoku combinations
possibleSymbolCombinations (row, col) board = [symbol | symbol <- [1..4], checkBoard symbol (row, col) board]
possibleBoards (row, col) board = map (\m -> (board // [((row, col), m)])) $ possibleSymbolCombinations (row, col) board

answers :: [(Int, Int)] -> Board -> [Board]
answers [] board = [board]
answers ((row, col):xs) board = concatMap (answers xs) $ possibleBoards (row, col) board

-- Finds solutions replacing each empty location with numbers from 1-4 recursively
solutions :: Board -> [Board]
solutions b = answers (emptyLocations b) b    

-- Returns coordinates of all empty locations
emptyLocations :: Board -> [(Int, Int)]
emptyLocations b = [(row, col) | row <- [0..3], col <- [0..3], b ! (row, col) == 0]

-- Checks if the board is valid after placing the new symbol
checkBoard :: Int -> (Int, Int) -> Board -> Bool
checkBoard m (row, col) b = (checkRow m b (row, col))
                                && (checkColumn m b (row, col)) 
                                && (checkSquare m b (row, col))

-- Checks the row, column and 3X3 box to which the newly placed symbol belongs for validity
checkRow m b (row, col) = notElem m (rowSymbols b row 3)
checkColumn m b (row, col) = notElem m (columnSymbols b col 3)                          
checkSquare m b (row, col) = notElem m (squareSymbols b (row, col)) 

-- Returns a list of symbols belonging to a row
rowSymbols :: Board -> Int -> Int -> [Int]
rowSymbols b row 0 = [b ! (row, 0)]
rowSymbols b row index = (b ! (row, index)) : (rowSymbols b row (index-1))

-- Returns a list of symbols belonging to a column
columnSymbols :: Board -> Int -> Int -> [Int]
columnSymbols b col 0 = [b ! (0, col)]
columnSymbols b col index = (b ! (index, col)) : (columnSymbols b col (index-1))

-- Returns coordinates of positions belonging to a 3X3 box whose top-left corner is (row, col)
squareLocations :: (Int, Int) -> [(Int, Int)]
squareLocations (row, col) = [(row, col), (row, col+1), (row+1, col), (row+1, col+1)]

-- Returns a list of symbols belonging to a 3X3 box whose top-left corner is (row, col)
squareSymbols :: Board -> (Int, Int) -> [Int]
squareSymbols b (row, col) = [b ! loc | loc <- squareLocations (row - (row `mod` 2), col - (col `mod` 2))]

-- Assign values to a list of rows of symbols
puzzleAssign :: [[Int]] -> [((Int, Int), Int)]
puzzleAssign = concatMap rowAssign . zip [0..3]

rowAssign :: (Int, [Int]) -> [((Int, Int), Int)]
rowAssign (row, symbols) = colAssign row $ zip [0..3] symbols

colAssign :: Int -> [(Int, Int)] -> [((Int, Int), Int)]
colAssign row cols = map (\(col, m) -> ((row, col), m)) cols

-- Validity of Sudoku
isValid :: [a] -> Maybe a
isValid [] = Nothing
isValid (x:xs) = Just x

-- Output fully solved Sudoku
printSudoku :: Maybe Board -> IO ()
printSudoku Nothing  = putStrLn "No solution"
printSudoku (Just b) = mapM_ putStrLn [show $ rowSymbols b row 3 | row <- [0..3]]