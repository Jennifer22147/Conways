/*
 * Created on May 24, 2004
 *
 * Latest update on April 21, 2011
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.

/**
*draws everything every update
*in charge of the display and manages the cells
*a display object is one game
*/
public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cell = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 5;
	private final int CELL_HEIGHT = 5;

	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private StartButton startStop;
	private ClearButton clear;
	private NextButton next;
	private boolean paintloop = false;

	/**
	*creates a display object and sets the width and the height
	*@param width width of the board
	*@param height height of the board
	*/
	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}

	/**
	*sets game up by making boundaries and making things visible
	*/
	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);

		// Example of setting up a button.
		// See the StartButton class nested below.
		startStop = new StartButton();
		startStop.setBounds(100, 550, 100, 36);
		clear = new ClearButton();
		clear.setBounds(300, 550, 100, 36);
		next = new NextButton();
		next.setBounds(500, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		add(clear);
		clear.setVisible(true);
		add(next);
		next.setVisible(true);
		repaint();
	}

	/**
	*paints the board
	*@param g object type Graphics that does drawing
	*/
	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 5; // change to your liking

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			repaint();
		}
	}

	/**
	*runs once at the beginning of the game
	*creates objects and can set initial cells
	*/
	public void initCells() {
		//fills each element of the array with a new object
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}

	/**
	*draws the grid
	*called every update of the game
	*/
	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}

	/**
	*drawing the cells
	*called every update
	*/
	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}

	/**
	*draws the buttons
	*called every update
	*/
	private void drawButtons() {
		startStop.repaint();
		clear.repaint();
		next.repaint();
	}
	/**
	*programs the clear button
	*goes through every cell and sets it to dead
	*repaints the board
	*/
	public void clear() {
		for (int g = 0; g < COLS; g++) {
			for (int h = 0; h < ROWS; h++) {
				System.out.println(cell[h][g].getAlive());
				cell[h][g].setAlive(false);
				cell[h][g].setAliveNextTurn(false);
				repaint();

			}

		}
	}

	/**
	*calculates the cells alive for the next generation and repaints the board accordingly
	*/
	private void nextGeneration() {
		// Decides whether to kill a cell, keep it alive, or bring it to life
		for (int j = 0; j < COLS; j++) {
			for (int i = 0; i < ROWS ; i++) {
				cell[i][j].calcNeighbors(cell); // updates number of neighbors for each cell
				if(cell[i][j].getAlive()==true){ //logic for when cell is alive
					if(cell[i][j].getNeighbors()<4 && cell[i][j].getNeighbors()>1){
						cell[i][j].setAliveNextTurn(true);
					}
					else  {
						cell[i][j].setAliveNextTurn(false);
					}
				}
				else{
					if(cell[i][j].getNeighbors()==3){
						cell[i][j].setAliveNextTurn(true);
					}
				}
			}
		}

		for (int a = 0; a < COLS; a++) {
			for (int b = 0; b < ROWS ; b++) {

				cell[b][a].setAlive(cell[b][a].getAliveNextTurn());
				
			}
		}
		repaint(); //moved repaint to bottom of next generation because it repaints the whole board 
	}

	/**
	*calculates which cell is being clicked and makes sure it is in boundaries
	*sets the cell to opposite of the current state
	*@param arg0 object that knows where the mouse is 
	*/
	public void mouseClicked(MouseEvent arg0) {
		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);
		if((CellX>=0 && CellX<=COLS-1) && (CellY>=0 && CellY<=ROWS-1)){

			getCell(arg0).setAlive(!getCell(arg0).getAlive()); //makes cell go to opposite state

			repaint();

		}				
	}


	//getting cell mouse is hovered on
	private Cell getCell(MouseEvent arg0){

		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);
		return cell[CellY][CellX];
	}


	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}

	/**
	*calculates the cells that the mouse is dragged over and sets them to the opposite state
	*@param arg0 object that knows where the mouse is
	*/
	public void mouseDragged(MouseEvent arg0) {

		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);

		if((CellX>=0 && CellX<=COLS-1) && (CellY>=0 && CellY<=ROWS-1)){



			getCell(arg0).setAlive(true);

			repaint();
		}

	}


	public void mouseMoved(MouseEvent arg0) {
		/*Point point = arg0.getPoint();

			if (arg0.getX() >= 25 && arg0.getY() >=40){
				int column = (arg0.getX()-25)/5;
				int row = (arg0.getY()-40)/5;

					if(column >= 0 && row>+0 && column<100 && row <80){

					}
			}
		 */
	}


	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {
			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
				nextGeneration();
			} else {
				togglePaintLoop();
				setText("Start");

			}	 

			repaint();
		}
	}




	private class ClearButton extends JButton implements ActionListener {
		ClearButton() {
			super("Clear");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("clicked");
			clear();

		}




	}

	private class NextButton extends JButton implements ActionListener {
		NextButton() {
			super("Next");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {

			if (paintloop==false) {
				nextGeneration();
			}

			repaint();
		}
	}




}