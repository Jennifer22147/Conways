Conway's Game of Life
Amelia Mao
Conways is a game. 
Rules:
For a space that is 'populated':
Each cell with one or no neighbors dies, as if by solitude.
Each cell with four or more neighbors dies, as if by overpopulation.
Each cell with two or three neighbors survives.
For a space that is 'empty' or 'unpopulated'
Each cell with three neighbors becomes populated.

Overview of Code:
Main: sets complete Display Size, Title and Size of Game
Display: sets cell size, number of rows and columns, all buttons, mouse events, and paints cells if alive
Cell: sets cell and neighbors alive or dead next turn, gets x and y positions, calculates neighbors

Resources:
For Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
For Mouse Events:
https://docs.oracle.com/javase/7/docs/api/java/awt/event/MouseEvent.html