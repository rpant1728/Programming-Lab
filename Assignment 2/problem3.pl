% Initializing graph edges and there respective weights.
edge(g1,g5,4). 
edge(g2,g5,6).
edge(g3,g5,8). 
edge(g4,g5,9). 
edge(g1,g6,10). 
edge(g2,g6,9). 
edge(g3,g6,3).  
edge(g4,g6,5). 
edge(g5,g7,3). 
edge(g5,g10,4). 
edge(g5,g11,6). 
edge(g5,g12,7). 
edge(g5,g6,7). 
edge(g5,g8,9). 
edge(g6,g8,2). 
edge(g6,g12,3). 
edge(g6,g11,5). 
edge(g6,g10,9). 
edge(g6,g7,10). 
edge(g7,g10,2). 
edge(g7,g11,5). 
edge(g7,g12,7). 
edge(g7,g8,10). 
edge(g8,g9,3). 
edge(g8,g12,3). 
edge(g8,g11,4). 
edge(g8,g10,8). 
edge(g10,g15,5). 
edge(g10,g11,2). 
edge(g10,g12,5). 
edge(g11,g15,4). 
edge(g11,g13,5). 
edge(g11,g12,4). 
edge(g12,g13,7). 
edge(g12,g14,8). 
edge(g15,g13,3). 
edge(g13,g14,4). 
edge(g14,g17,5). 
edge(g14,g18,4).
edge(g17,g18,8).


% Initializing start gates
start(g1).
start(g2).
start(g3).
start(g4).


% If Depth First Search reaches exit gate
dfs(g17, Path, _) :- 
    % Add last gate to current path.
    append(Path, [g17], UpdatedPath), 
    % Convert path list to concatenated string
    atomic_list_concat(UpdatedPath, ' -> ', Atom), atom_string(Atom, String),
    % Write string to terminal and file output.txt
    format('~w~n', String),
    open('output.txt', append, Stream),
    write(Stream, String),
    nl(Stream),
    close(Stream).


dfs(Gate, Path, TotalDistance) :-
    (
        % If gate has not been visited yet.
        % Note that if gate had been already visited, exploring it would create a cycle and infinite paths would be possible.
        not(member(Gate, Path)) -> 
            % Add gate to current path
            append(Path, [Gate], UpdatedPath),
            % For all possible neighbours of the gate, apply Depth First Search recursively
            forall(
                edge(Gate, NextGate, Distance); edge(NextGate, Gate, Distance), 
                (
                    % Update total distance at each step
                    UpdatedDistance is TotalDistance + Distance, 
                    dfs(NextGate, UpdatedPath, UpdatedDistance)
                )
            )
        ;
            write("")
    ).


% If Depth First Search reaches exit gate
optimal_dfs(g17, Path, TotalDistance) :- 
    % Add gate to current path
    append(Path, [g17], UpdatedPath), 
    % Compute and update minimum path distance value
    nb_getval(minimumDist, MinimumDist),
    UpdatedMinimumDist is min(MinimumDist, TotalDistance),
    nb_setval(minimumDist, UpdatedMinimumDist),
    (
        % If current path has the minimum distance value (till now), update minimum path list
        UpdatedMinimumDist =:= TotalDistance -> nb_setval(minimumPath, UpdatedPath) 
        ; 
        write("")
    ).


% Optimal DFS differs from the DFS implemented above only in the fact that it does not consider the reverse edges
% of the edge list provided. As we want to find the optimal path, and the optimal path is guaranteed to have no such 
% 'backwards' edge, we can safely ignore them, for faster path calculation.
optimal_dfs(Gate, Path, TotalDistance) :-
    (
        not(member(Gate, Path)) -> 
            % Add gate to current path
            append(Path, [Gate], UpdatedPath),
            % For all possible neighbours of the gate, apply Depth First Search recursively
            forall(
                edge(Gate, NextGate, Distance), 
                (
                    % Update total distance at each step
                    UpdatedDistance is TotalDistance + Distance, 
                    optimal_dfs(NextGate, UpdatedPath, UpdatedDistance)
                )
            )
        ;
            write("")
    ).


% Prints all possible paths using which the prisoner can escape
paths() :-
    forall(start(X), dfs(X, [], 0)).


% Prints the optimal path (least distance) using which the prisoner can escape
optimal() :-
    % Initialize Global variables to store the optimal distance and path
    nb_setval(minimumDist, 1000000),
    nb_setval(minimumPath, []),

    % Apply DFS from all start gates
    forall(start(Gate), optimal_dfs(Gate, [], 0)),

    % Get and output the optimal distance and path values.
    nb_getval(minimumDist, OptimalDistance),
    format('Optimal Distance :- ~d~n', OptimalDistance),
    nb_getval(minimumPath, Path),
    atomic_list_concat(Path, ' -> ', Atom), atom_string(Atom, String),
    format('Optimal Path :- ~w~n', String).


% Checks that last gate on path is the exit gate (g17).
check_edges([g17]).


% Checks if two consecutive gates of a given path are part of an edge
check_edges([Gate, NextGate|Tail]) :-
    (edge(Gate, NextGate, _) ; edge(NextGate, Gate, _)),
    % Check remaining path recursively
    check_edges([NextGate|Tail]).


% Checks if given path is valid
valid([Gate|Tail]) :-
    % Checks that given path starts with one of g1, g, g3, g4 (start gates).
    start(Gate),
    check_edges([Gate|Tail]).

