% Keenan Johnstone
% kbj182 11119412
% cmpt340 Assignment 5

% List of all males
male(phillip).
male(charles).
male(william).
male(harry).
male(george).

% List of Females
female(elizabeth).
female(diana).
female(camilla).
female(catherine).
female(charlotte).

% marriages HAVE TO 
marriedTo(elizabeth, phillip).
marriedTo(phillip, elizabeth).
marriedTo(diana, charles).
marriedTo(charles, diana).
marriedTo(camilla, charles).
marriedTo(charles, camilla).
marriedTo(william, catherine).
marriedTo(catherine, william).

% Children
childOf(george, william).
childOf(george, catherine).
childOf(charlotte, william).
childOf(charlotte, catherine).
childOf(william, diana).
childOf(william, charles).
childOf(harry, diana).
childOf(harry, charles).
childOf(charles, elizabeth).
childOf(charles, phillip).

% Sibiling Rule, if the two share both parents ( I added this to make things easier)
siblingOf(C1, C2) :- childOf(C1, P1), childOf(C2, P1), childOf(C1, P2), childOf(C2, P2), (P1 \= P2), (C1 \= C2).

% Auntie Rule A is the aunt of X. An auntie is someone who is the female sibling of your parent
auntOf(A, X) :- female(A), childOf(X, P), siblingOf(A, P).

% Grandchild Rule, GC is the grandchild of GP
grandchildOf(GC, GP) :- childOf(GC, P), childOf(P, GP).

% SM is a stepmother of C if C is not a Child of a parent, but the SM is married to the parent
stepmotherOf(SM, C) :- childOf(C, P), marriedTo(P, SM), not(childOf(C, SM)), female(SM).

% N is a nephew of X if X is male, and a sibiling of N's parent
nephewOf(N, X) :- male(X), childOf(N, P), siblingOf(X, P).

% MIL is the mother in law of X if MIL is married to a parent of X, but is not a parent P of X themselves
motherInLawOf(MIL, X) :- female(MIL), marriedTo(MIL, P), childOf(X, P), not(childOf(X, MIL)).

% BIL is a brother in law of X if BIL is male and X is married to sibling S of BIL
brotherinLawOf(BIL, X) :- male(BIL), siblingOf(BIL, S), marriedTo(S, X).

% A is an Ancestor of X if X is the child of a grandchild of some person P
ancestorOf(A, X) :- grandchildOf(X, P), childOf(P, A).



