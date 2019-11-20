validate_input([Head]) :-
    % If leading character is not a digit, output error and terminate.
    (
        not(char_type(Head, 'digit')) -> 
            write("Invalid Input! Only numbers allowed."), 
            fail
        ;
        write("")
    ).
    

validate_input([Head,Next|Tail]) :-
    (
        % If leading character is not a digit, output error and terminate.
        (not(char_type(Head, 'digit')) ; not(char_type(Next, 'digit'))) -> 
            write("Invalid Input! Only numbers allowed."), 
            fail
        ;
        % Convert leading two characters to integer
        atom_number(Head, Num1),
        atom_number(Next, Num2),

        % If a zero is preceded by any number except for 1 or 2, output 0 number of ways and terminate.
        (
            (Num1 > 2 ; Num1 =:= 0), Num2 =:= 0 -> write("Number of ways :- 0"), fail; write("")
        ),

        % Recursively validate remaining input.
        validate_input([Next|Tail])
    ).


% Return 0 number of ways in case of an empty string input.
decode("") :- 
    write("Number of ways :- 0").


decode(String) :-
    % Convert string to list of characters.
    atom_chars(String, List), 
    % Check if input is valid before processing.
    validate_input(List),
    % Initializing
    Opt1 is 1, 
    Opt2 is 1, 

    % Convert leading character to an Integer
    [Head|_] = List,
    atom_number(Head, Num),
    (
        % Return 0 number of ways if leading digit of input is 0.
        Num =:= 0 ->
            write("Number of ways :- 0"),
            fail
        ;
        % Else go forth with processing.
        dp(Opt1, Opt2, List)
    ).


% If we reach end of the output, return the result stored in Opt2.
dp(_, Opt2, [_]) :- 
    format('Number of ways :- ~d', Opt2).


dp(Opt1, Opt2, [Head,Next|Tail]) :-
    % Convert leading two characters to integer
    atom_number(Head, Num1),
    atom_number(Next, Num2),
    (
        % If pattern is one of [11, 12 ... 19, 21, 22, ... 26] then -
        % dp(i) = dp(i-1) + dp(i-2)
        (Num1 =:= 1 ; (Num1 =:=2, Num2 < 7)), Num2 =\= 0 -> 
            Temp = Opt2,
            UpdatedOpt2 is Opt1+Opt2,
            UpdatedOpt1 is Temp
        ;
        % If pattern is 10 or 20 then -
        % dp(i) = dp(i-2)
        (Num1 =:= 1 ; Num1 =:= 2), Num2 =:= 0 -> 
            Temp = Opt2,
            UpdatedOpt2 = Opt1,
            UpdatedOpt1 = Temp
        ;
        % In all other cases - 
        % dp(i) = dp(i-1)
        UpdatedOpt2 = Opt2,
        UpdatedOpt1 = Opt2
    ),
    % Recursively process remaining input.
    dp(UpdatedOpt1, UpdatedOpt2, [Next|Tail]).



