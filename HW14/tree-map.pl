map_build([], T) :- T = null, !.
map_build([(K, V) | R], T) :-
	map_build(R, New),
    map_put(New, K, V, T).

split(null, Vsp, null, null).
split(tree((K, V), P, L, R), Vsp, NewL, NewR) :-
    (Vsp =< K,
    split(L, Vsp, NewL, Rsp),
    NewR = tree((K, V), P, Rsp, R));
    (Vsp > K,
    split(R, Vsp, Lsp, NewR),
    NewL = tree((K, V), P, L, Lsp)).

merge(T1, null, T1).
merge(null, T2, T2) :- T2 \= null.
merge(tree((K1, V1), P1, L1, R1),tree((K2, V2), P2, L2, R2), Res) :-
    (P1 > P2,
    merge(R1, tree((K2, V2), P2, L2, R2), New), 
    Res = tree((K1, V1), P1, L1, New));
    (P1 =< P2,
    merge(tree((K1, V1), P1, L1, R1), L2, New),
    Res = tree((K2, V2), P2, New, R2)).

node((K, V), tree((K, V), P, null, null)) :- rand_int(10000000, P).

map_insert(null, K, V, R) :- node((K, V), R), !.
map_insert(tree((Kt, Vt),P, L, R), K, V, Ret) :-
    number(Kt),
    split(tree((Kt, Vt), P, L, R), K, NewL, NewR),
    node((K,V), New),
    merge(NewL, New, NewIns),
    merge(NewIns, NewR, Ret), !.

map_put(T, K, V, R) :- 
    map_remove(T, K, TNew),
    map_insert(TNew, K, V, R).

map_replace(T, K, V, R) :- 
    (map_get(T, K, Ret),
    map_remove(T, K, TNew),
    map_insert(TNew, K, V, R));
    (\+map_get(T, K, Ret),
    R = T).

map_remove(T, K, R) :- 
    split(T, K, NewL, NewR),
    split(NewR, K + 1, NewNewL, NewNewR),
    merge(NewL, NewNewR, R).

map_get(tree((K, V), _, null, null), _, _, _) :- false. 
map_get(tree((K, V), _, _, _), K, V, _) :- !.
map_get(tree((K, V), _, _, R), Key, Val, Ret) :- 
    (K < Key,
	map_get(R, Key, Val, Ret));
    (K >= Key, false, !).
map_get(tree((K, V), _, L, _), Key, Val, Ret) :-
    (K >= Key,
	map_get(L, Key, Val, Ret));
    (K < Key, false, !).
map_get(T, K, V) :- map_get(T, K, V, R).

map_floorKey(T, K, R) :-
    split(T, K+1, NewT, _),
    traverse(NewT, R).

traverse(tree((K, V), P, L, R), Ret) :-
    (R \= null,
    traverse(R, Ret));
    (R = null,
    Ret = K, !).

