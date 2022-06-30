init(N) :- init_rec(N, 2).

init_rec(N, C) :- N =< C*C, !.
init_rec(N, C) :-
	Next is C+1,
	step(N, C, C, C),
	init_rec(N, Next).

step(N, C, S, Min) :- Next is C + S, N =< Next, !. 
step(N, C, S, Min) :-
	Next is C + S,
	assert(composite(Next)),
	setMinPrime(Next, Min),
	step(N, Next, S, Min).

setMinPrime(Num, Min) :- (
	\+min_prime(Num, R),
	assert(min_prime(Num, Min))
) ; true.

prime(N) :- \+composite(N).

search(Num, R) :- min_prime(Num, R) ; (prime(Num), R = Num).

prime_divisors(1, []) :- !.

erase([_], []) :- !.
erase([H | T], [H | T1]) :- erase(T, T1).

prime_divisors(N, [H | T]) :-
    prime_divisors(N, 2, [H | T1]),
    erase(T1, T2),
    T = T2.
prime_divisors(1, _, [H]) :-  !.
prime_divisors(Curr, Div, [H | T]) :-
    search(Curr, Div),
    H is Div, 
    Next is Curr / Div,
    prime_divisors(Next, Div, T), !.
prime_divisors(Curr, Div, [H | T]) :-
    \+ search(Curr, Div),
    Next is Div + 1,
    prime_divisors(Curr, Next, [H | T]), !.

nth_prime(Nth, K) :- nth_prime(Nth, K, 2).
nth_prime(0, K, Curr) :- K is Curr-1, !. 
nth_prime(Nth, K, Curr) :-
	Nth > 0,
	prime(Curr),
	NewNth is Nth - 1,
	NewCurr is Curr + 1,
	nth_prime(NewNth, K, NewCurr).
nth_prime(Nth, K, Curr) :-
	Nth > 0, 
	\+prime(Curr),
	NewCurr is Curr + 1,
	nth_prime(Nth, K, NewCurr).
	
