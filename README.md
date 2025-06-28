# Java Sudoku Game + Solver

A Sudoku game and solver written in Java. You can either input your own puzzle or play one that's randomly generated.

## Features
- Solves any valid 9x9 Sudoku puzzle
- Generates full Sudoku boards with difficulty masking (explained below)
  (You choose a number from 1-10 --> the higher the number, the fewer cells are revealed)
- Colored terminal output:
  - **When solving a randomly generated puzzle:**
    - **White** = original/pre-filled number  
    - **Green** = your correct guess  
    - **Red** = your incorrect guess  
  - **When the computer solves your inputted puzzle:**
    - **White** = original/pre-filled number  
    - **Green** = computer's answer
- Code is decently organized, with each method labeled and separated for readability

## File Structure
|-- Main.java 


|-- SudokuSolver.java 


|-- GenerateSudoku.java 
