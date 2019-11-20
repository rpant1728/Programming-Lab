import Data.List
import Data.IORef
import System.IO.Unsafe
import System.Random
--set the teams list in global variable gteam
gteam = unsafePerformIO shuffleteam 
--shuffles the team array
shuffle list = 
    if length list < 2 then 
    return list 
    else do
        i <- System.Random.randomRIO (0, length(list)-1)
        r <- shuffle (take i list ++ drop (i+1) list)
        return (list!!i : r)
-- data structure to store information about fixtures
data Fixture = Fixture {
                         team1 :: String,
                         team2 :: String,
                         day :: String,
                         time :: String
                        } deriving (Show, Eq)

--generate fixture for all teams
generate_fixtures teams 12 = []
generate_fixtures teams index = do
                                    let day1 = (index `div` 4)+1
                                    Fixture {
                                        team1=teams !! index, 
                                        team2=teams !! (index+1),
                                        day=(show day1) ++ "-11",
                                        time="9:30 AM"
                                    } : Fixture {
                                        team1=teams !! (index+2), 
                                        team2=teams !! (index+3),
                                        day=(show day1) ++ "-11",
                                        time="7:30 PM"
                                    } : generate_fixtures teams (index+4)

--return index of first member in match 
getindex index = if index `mod` 2==1
                    then index-1
                    else index
getfixture teams 12 = [] 
--returns match schedule according to input index1 
getfixture teams index1 = do
                            let index=getindex index1
                            let day1 = (index `div` 4)+1
                            let match_num = (max index index1) `div` 2
                            let time1 = gettime match_num
                            return Fixture {
                                    team1=teams !! index, 
                                    team2=teams !! (index+1),
                                    day=(show day1) ++ "-11",
                                    time=time1
                                }
--print match schedule btn all teams
print_fixture (Fixture {team1 = t1, team2 = t2, day = d, time = t}) = print(t1 ++ " vs " ++ t2 ++ "    " ++ d ++ "    " ++ t)

get_index [] team index = -1
get_index (x:xs) team index = if x == team
                                    then index
                                    else get_index xs team index+1


--print match schedule for teams
print_fixtures [x] = print_fixture x 
print_fixtures (x:xs) = do
                            print_fixture x
                            print_fixtures xs
--returns next match schedule after current time
nextMatch date time = do
                        if (date >= 1 && date <= 3 && time >= 0 && time <= 24)
                            then do
                                let teams = gteam
                                let fixtures = generate_fixtures teams 0
                                let index = getindex1 time date
                                 -- gets fixture from index
                                if (index >= 12) 
                                    then print("All matches over!")
                                    else print $ getfixture teams index
                            else error "Invalid date or time!"
--shuffle the input array
shuffleteam = shuffle ["BS", "CM", "CH", "CV", "CS", "DS", "EE", "HU", "MA", "ME", "PH", "ST"]
--Gets all the fixture if input str is "all"
fixtures str = do
        let teams = gteam
        let fixtures = generate_fixtures teams 0
        if str == "all"
            then print_fixtures fixtures
            else if elem str teams
                then do
                    let l = get_index teams str 0         
                    let x=getfixture teams l
                    print(x)
            else print "Invalid Input! Not a team."                       
--return time of match according to index
gettime index = do
    if ((index `mod` 2) == 0)
        then "9:30 AM"
        else "7:30 PM"

getindex1 time date = do
    if time > 19.50 
        then 4*(date-1)+4
        else 
        if time > 9.50
            then 4*(date-1)+2
        else
            4*(date-1)