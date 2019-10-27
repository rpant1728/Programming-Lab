% Initializing facts to store menu details
item(starter, "Corn Tikki", 30, 1).
item(starter, "Tomato Soup", 20, 2).
item(starter, "Chilli Paneer", 40, 3).
item(starter, "Crispy Chicken", 40, 4).
item(starter, "Papdi Chaat", 20, 5).
item(starter, "Cold Drink", 20, 6).

item(main_dish, "Kadhai Paneer with Butter / Plain Naan", 50, 1).
item(main_dish, "Veg Korma with Butter / Plain Naan", 40, 2).
item(main_dish, "Murgh Lababdaar with Butter / Plain Naan", 50, 3).
item(main_dish, "Veg Dum Biryani with Dal Tadka", 50, 4).
item(main_dish, "Steam Rice with Dal Tadka", 40, 5).

item(dessert, "Icecream", 20, 1).
item(dessert, "Malai Sandwich", 30, 2).
item(dessert, "Rasmalai", 10, 3).

% Valid combinations of menu for different status'
menu(hungry, 1, 1, 1).
menu(not_so_hungry, 1, 1, 0).
menu(not_so_hungry, 0, 1, 1).
menu(diet, 1, 0, 0).
menu(diet, 0, 1, 0).
menu(diet, 0, 0, 1).
menu(_, _, _, _) :- write("Invalid Menu Choice!"), fail.

% Returns possible menu combinations if a person is hungry
get_items_hungry(StarterId, MainDishId, DessertId) :-
    (
        % Here we cycle through all combinations of X, Y, Z recursively, where X varies between 1 to 6, Y varies between 1 to 5,
        % and Z varies between 1 to 3. These numbers are the ranges of the number of starters, main dishes and desserts respectively.
        DessertId =:= 4 -> 
            UpdatedMainDishId is MainDishId+1,
            get_items_hungry(StarterId, UpdatedMainDishId, 1)
        ;
        MainDishId =:= 6 -> 
            UpdatedStarterId is StarterId+1,
            get_items_hungry(UpdatedStarterId, 1, 1)
        ;
        StarterId =:= 7 -> write("Done")
        ;
        write("Items :- "),
        % Get a possible combination of {Starter, Main Dish, Dessert} and print it
        item(starter, Starter, _, StarterId), 
        item(main_dish, MainDish, _, MainDishId), 
        item(dessert, Dessert, _, DessertId),
        format('~w, ~w, ~w~n', [Starter, MainDish, Dessert]),
        UpdatedDessertId is DessertId+1,
        get_items_hungry(StarterId, MainDishId, UpdatedDessertId)
    ).

% Returns possible menu combinations if a person is not so hungry and chooses to eat a starter and a main dish.
get_items_not_hungry_with_starter(StarterId, MainDishId) :- 
    (
        % Here we cycle through all combinations of Starter and Main Dish ids recursively, where starter ids varies between 1 to 6, 
        % main dish ids varies between 1 to 5. These numbers are the ranges of the number of starters and main dishes respectively.
        MainDishId =:= 6 -> 
            UpdatedStarterId is StarterId+1,
            get_items_not_hungry_with_starter(UpdatedStarterId, 1)
        ;
        StarterId =:= 7 -> write("Done")
        ;
        % Get a possible combination of {Starter, Main Dish}
        item(starter, Starter, Cost1, StarterId), 
        item(main_dish, MainDish, Cost2, MainDishId), 
        % Output it only if their combined cost is less than equal to 80.
        Cost is Cost1+Cost2,
        (
            Cost =< 80 -> format('Items:- ~w, ~w~n', [Starter, MainDish])
            ;
            write("")
        ),
                
        UpdatedMainDishId is MainDishId+1,
        get_items_not_hungry_with_starter(StarterId, UpdatedMainDishId)
    ).

% Returns possible menu combinations if a person is not so hungry and chooses to eat a main dish and a dessert.
get_items_not_hungry_with_dessert(MainDishId, DessertId) :- 
    (
        % Here we cycle through all combinations of Main Dish and Dessert ids recursively, where main dish ids varies between 1 to 5, 
        % dessert ids varies between 1 to 3. These numbers are the ranges of the number of starters and main dishes respectively.
        DessertId =:= 4 -> 
            UpdatedMainDishId is MainDishId+1,
            get_items_not_hungry_with_dessert(UpdatedMainDishId, 1)
        ;
        MainDishId =:= 6 -> write("Done")
        ;
        % Get a possible combination of {Main Dish, Dessert}
        item(main_dish, MainDish, Cost1, MainDishId), 
        item(dessert, Dessert, Cost2, DessertId), 
        % Output it only if their combined cost is less than equal to 80.
        Cost is Cost1+Cost2,
        (
            Cost =< 80 -> format('Items:- ~w, ~w~n', [MainDish, Dessert])
            ;
            write("")
        ),
                
        UpdatedDessertId is DessertId+1,
        get_items_not_hungry_with_dessert(MainDishId, UpdatedDessertId)
    ).

% Returns possible menu combinations if a person is on a diet. The course that he chooses to eat is an argument.
get_items_diet(Course, Item, Nutrients, TotalNutrients, Id, CurrentList) :- 
    % Add nutrient amount of current item to total nutrients
    UpdatedTotalNutrients is TotalNutrients + Nutrients,
    (
        % If total nutrients is still less than 40 (permisiible limit)
        UpdatedTotalNutrients =< 40 -> 
            % Add item to current menu list
            append(CurrentList, [Item], UpdatedList),
            % Convert list to comma separated string
            atomic_list_concat(CurrentList, ', ', Atom), atom_string(Atom, String),
            % Get the global list of strings storing all the possible combinations that have been found till now.
            nb_getval(uniqueList, List),
            (
                % If the current combination is a new one, write it out and add it to the set
                not(member(String, List)), String \= "" ->
                    format('Items:- ~w~n', String),
                    append(List, [String], UpdatedUniqueList),
                    nb_setval(uniqueList, UpdatedUniqueList)
                ;
                write("")
            ),
            % Try all possible additions to current menu combination, in lexicographical order
            forall(
                (item(Course, NextItem, NextNutrients, NextId), NextId >= Id), 
                get_items_diet(Course, NextItem, NextNutrients, UpdatedTotalNutrients, NextId, UpdatedList)
            )
        ;
        % Convert List to string and output and maintain set as done above.
        atomic_list_concat(CurrentList, ', ', Atom), atom_string(Atom, String),
        nb_getval(uniqueList, List),
        (
            not(member(String, List)) ->
                format('Items:- ~w~n', String),
                append(List, [String], UpdatedUniqueList),
                nb_setval(uniqueList, UpdatedUniqueList)
            ;
            write("")
        )    
    ).

% Returns possible items given status and course preferences
find_items(Status, X, Y, Z) :-
    % Check if course preferences conform to question's constraints
    menu(Status, X, Y, Z),
    % Initialize global variable uniqueList, will act as set for menu combinations created already to prevent repetitions
    nb_setval(uniqueList, []),
    (
        % If person is hungry
        Status = hungry -> get_items_hungry(1, 1, 1) ;

        % If person is not so hungry
        Status = not_so_hungry, Z =:= 0 -> get_items_not_hungry_with_starter(1, 1) ;
        Status = not_so_hungry, Z =:= 1 -> get_items_not_hungry_with_dessert(1, 1) ;

        % If person is on a diet
        % If he eats only starters
        Status = diet, X =:= 1 -> 
            forall(item(starter, Item, Nutrient, Id), 
            get_items_diet(starter, Item, Nutrient, 0, Id, [])) ;
        % If he eats only main dishes
        Status = diet, Y =:= 1 -> 
            forall(item(main_dish, Item, Nutrient, Id), 
            get_items_diet(main_dish, Item, Nutrient, 0, Id, [])) ;
        % If he eats only desserts
        Status = diet, Z =:= 1 -> 
            forall(item(dessert, Item, Nutrient, Id), 
            get_items_diet(dessert, Item, Nutrient, 0, Id, [])) ;

        % For all other cases output error
        write("Invalid Input!")
    ).






