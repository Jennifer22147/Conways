// Created on Dec 1, 2004


import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	private int myX, myY; // x,y position on grid
	private boolean myAlive; // alive (true) or dead (false)
	private int myNeighbors; // count of neighbors with respect to x,y
	private boolean myAliveNextTurn; // Used for state in next iteration
	private Color myColor; // Based on alive/dead rules
	private final Color DEFAULT_ALIVE = Color.ORANGE;
	final Color DEFAULT_DEAD = Color.GRAY;

	public Cell(int x, int y) {
		this(x, y, false, Color.GRAY);
	}

	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		myX = col;
		myY = row;
	}

	public boolean getAlive() {
		return myAlive;
	}

	public int getX() {
		return myX;
	}

	public int getY() {
		return myY;
	}

	public Color getColor() {
		return myColor;
	}

	public void setAlive(boolean alive) {
		if (alive) {
			setAlive(true, DEFAULT_ALIVE);
		} else {
			setAlive(false, DEFAULT_DEAD);
		}
	}

	public void setAlive(boolean alive, Color color) {
		myColor = color;
		myAlive = alive;
	}

	public void setAliveNextTurn(boolean alive) {
		myAliveNextTurn = alive;
	}

	public boolean getAliveNextTurn() {
		return myAliveNextTurn;
	}

	public void setColor(Color color) {
		myColor = color;
	}
	
	public void setNeighbors(int neighbors){
		myNeighbors = neighbors;
	}

	public int getNeighbors() {
		return myNeighbors;
	}
	
	public void setNeighbors(int neighbors){	
		myNeighbors= neighbors;
	}

	public void calcNeighbors(Cell[][] cell) {
		int myNeighbors = 0;
		
		for (int i = getY() -1; i <= getY() + 1; i++){
			
			if (i== -1 || i == 80){
				continue; 
			}
			
			
			for (int j = getX() -1; j <= getX() + 1; j++ ){
				
				if (j == -1 || j == 100){
					continue;
				}
				
				if (cell[i][j].getAlive()){
					myNeighbors++;
				}
				
			}
			
		}
		
		if (cell[getY()][getX()].getAlive()){
			myNeighbors--;
		}
<<<<<<< HEAD
        System.out.println(myNeighbors);	
        cell[getY()][getX()].setNeighbors(myNeighbors);
        
=======
       cell[getY()][getX()].setNeighbors(myNeighbors);
>>>>>>> ca3216c48fdeeea8b2085caad09d4cbd7acbceb8
	}
	
	
	
		
		
		
		
		
	
	
	public void draw(int x_offset, int y_offset, int width, int height,
			Graphics g) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);
	}
}