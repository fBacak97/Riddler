# Riddler
NYTimes 5x5 Crossword Solver via Constraint Satisfaction Approach

A program that uses constraint satisfaction to solve 5x5 crosswords provided by NYTimes daily. \n
Only uses Google Search to find possible solutions excluding crossword sites.
With all 10 answers found in the candidate solution set, Riddler is guaranteed to find the perfect solution. 
However, there is no search algorith that would always make this possible.
Therefore Riddler uses a subset of all constraints picked randomly which reduces the accuracy and increases the runtime as the candidate solution sets become less restricted with limited constraints.
With the addition of proper search algorithms Riddler can become the perfect crossword solver. 
